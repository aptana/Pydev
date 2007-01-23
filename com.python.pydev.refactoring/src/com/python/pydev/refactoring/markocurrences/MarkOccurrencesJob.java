/*
 * Created on Apr 29, 2006
 */
package com.python.pydev.refactoring.markocurrences;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.AssertionFailedException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ISynchronizable;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.python.pydev.core.Tuple3;
import org.python.pydev.core.docutils.PySelection;
import org.python.pydev.core.log.Log;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.editor.actions.refactoring.PyRefactorAction;
import org.python.pydev.editor.codefolding.PySourceViewer;
import org.python.pydev.editor.refactoring.AbstractPyRefactoring;
import org.python.pydev.editor.refactoring.IPyRefactoring;
import org.python.pydev.editor.refactoring.RefactoringRequest;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.visitors.scope.ASTEntry;

import com.python.pydev.PydevPlugin;
import com.python.pydev.refactoring.refactorer.RefactorerRequestConstants;
import com.python.pydev.refactoring.ui.MarkOccurrencesPreferencesPage;
import com.python.pydev.refactoring.wizards.rename.PyRenameEntryPoint;


/**
 * This is a 'low-priority' thread. It acts as a singleton. Requests to mark the ocurrences
 * will be forwarded to it, so, it should sleep for a while and then check for a request.
 * 
 * If the request actually happened, it will go on to process it, otherwise it will sleep some more.
 * 
 * @author Fabio
 */
public class MarkOccurrencesJob extends Job{

    private static final boolean DEBUG = false;
    private static MarkOccurrencesJob singleton;
    
    /**
     * Make it thread safe
     */
    private static volatile long lastRequestTime = -1;

    /**
     * This is the editor to be analyzed
     */
    private WeakReference<PyEdit> editor;
    
    /**
     * This is the request time for this job
     */
    private long currRequestTime = -1;
    
    private MarkOccurrencesJob(WeakReference<PyEdit> editor) {
    	super("MarkOccurrencesJob");
    	setPriority(Job.BUILD);
    	setSystem(true);
    	this.editor = editor;
        currRequestTime = System.currentTimeMillis();
    }

    
    @SuppressWarnings("unchecked")
    public IStatus run(IProgressMonitor monitor) {
        if(currRequestTime == -1){
            return Status.OK_STATUS;
        }
        if(currRequestTime == lastRequestTime){
            return Status.OK_STATUS;
        }
        lastRequestTime = currRequestTime;
        
        try {
            final PyEdit pyEdit = editor.get();
            
            if(pyEdit == null || monitor.isCanceled()){
                return Status.OK_STATUS;
            }
            try{
	            IDocumentProvider documentProvider = pyEdit.getDocumentProvider();
	            if(documentProvider == null || monitor.isCanceled()){
	            	return Status.OK_STATUS;
	            }
	            
	            IAnnotationModel annotationModel= documentProvider.getAnnotationModel(pyEdit.getEditorInput());
	            if(annotationModel == null || monitor.isCanceled()){
	            	return Status.OK_STATUS;
	            }
	            

	            Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean> ret = checkAnnotations(pyEdit, documentProvider, monitor);
	            if(pyEdit.cache == null || monitor.isCanceled()){ //disposed (cannot add or remove annotations)
	            	return Status.OK_STATUS;
	            }
                
                PySourceViewer viewer = pyEdit.getPySourceViewer();
                if(viewer == null || monitor.isCanceled()){
                    return Status.OK_STATUS;
                }
                if(viewer.getIsInToggleCompletionStyle() || monitor.isCanceled()){
                    return Status.OK_STATUS;
                }
                
	            if(ret.o3){
	            	if(!addAnnotations(pyEdit, annotationModel, ret.o1, ret.o2)){
	            		//something went wrong, so, let's remove the occurrences
	            		removeOccurenceAnnotations(annotationModel, pyEdit);
	            	}
	            }else{
	            	removeOccurenceAnnotations(annotationModel, pyEdit);
	            }
            } catch (OperationCanceledException e) {
                throw e;//rethrow this error...
            } catch (AssertionFailedException e) {
                String message = e.getMessage();
                if(message.indexOf("The file:") != -1 && message.indexOf("does not exist.") != -1){
                    //don't even report it (the file was probably removed while we were doing the analysis)
                }else{
                    Log.log(e);
                    Log.log("Error while analyzing the file:"+pyEdit.getIFile());
                }
            } catch (Throwable e) {
            	Log.log(e);
            	Log.log("Error while analyzing the file:"+pyEdit.getIFile());
            }
            
        } catch (Throwable e) {
            Log.log(e);
        }
        return Status.OK_STATUS;
    }

    /**
     * @return a tuple with the refactoring request, the processor and a boolean indicating if all pre-conditions succedded.
     */
    private Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean> checkAnnotations(PyEdit pyEdit, 
    		IDocumentProvider documentProvider, IProgressMonitor monitor) throws BadLocationException, OperationCanceledException, CoreException {
        if(!MarkOccurrencesPreferencesPage.useMarkOccurrences()){
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }

        //now, let's see if the editor still has a document (so that we still can add stuff to it)
        IEditorInput editorInput = pyEdit.getEditorInput();
        if(editorInput == null){
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }
        
        if(documentProvider.getDocument(editorInput) == null){
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }
        
        if(pyEdit.getSelectionProvider() == null){
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }
        
        //ok, the editor is still there wit ha document... move on
        PyRefactorAction pyRefactorAction = getRefactorAction(pyEdit);
        
        final RefactoringRequest req = getRefactoringRequest(pyEdit, pyRefactorAction);
        
        if(req == null){
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }
        
        PyRenameEntryPoint processor = new PyRenameEntryPoint(req);
        //to see if a new request was not created in the meantime (in which case this one will be cancelled)
        if (currRequestTime != lastRequestTime || monitor.isCanceled()) {
        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
        }
        
        try{
	        processor.checkInitialConditions(monitor);
	        if (currRequestTime != lastRequestTime || monitor.isCanceled()) {
	        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
	        }
	        
	        processor.checkFinalConditions(monitor, null);
	        if (currRequestTime != lastRequestTime || monitor.isCanceled()) {
	        	return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(null,null,false);
	        }
	        
	        //ok, pre-conditions suceeded
			return new Tuple3<RefactoringRequest,PyRenameEntryPoint,Boolean>(req,processor,true);
        }catch(Throwable e){
        	Log.log("Error in occurrences while analyzing modName:"+req.moduleName+" initialName:"+req.initialName);
        	throw new RuntimeException(e);
        }
	}

	/**
	 * @return true if the annotations were removed and added without any problems and false otherwise
     */
    private synchronized boolean addAnnotations(final PyEdit pyEdit, IAnnotationModel annotationModel, final RefactoringRequest req, PyRenameEntryPoint processor) throws BadLocationException {
        //add the annotations
        synchronized (getLockObject(annotationModel)) {
            List<ASTEntry> occurrences = processor.getOcurrences();
            if(occurrences != null){
            	Map<String, Object> cache = pyEdit.cache;
            	if(cache == null){
            		return false;
            	}
            	
                IDocument doc = pyEdit.getDocument();
                ArrayList<Annotation> annotations = new ArrayList<Annotation>();
                Map<Annotation, Position> toAddAsMap = new HashMap<Annotation, Position>();                
                
                for (ASTEntry entry : occurrences) {
                	SimpleNode node = entry.getNameNode();
                    IRegion lineInformation = doc.getLineInformation(node.beginLine-1);
                    
                    try {
                        Annotation annotation = new Annotation(PydevPlugin.OCCURRENCE_ANNOTATION_TYPE, false, "occurrence");
                        Position position = new Position(lineInformation.getOffset() + node.beginColumn - 1, req.initialName.length());
                        toAddAsMap.put(annotation, position);
                        annotations.add(annotation);
						
                    } catch (Exception e) {
                        Log.log(e);
                    }
                }
                
                //get the ones to remove
                List<Annotation> toRemove = PydevPlugin.getOccurrenceAnnotationsInPyEdit(pyEdit);
                
                //replace them
                IAnnotationModelExtension ext = (IAnnotationModelExtension) annotationModel;
                ext.replaceAnnotations(toRemove.toArray(new Annotation[0]), toAddAsMap);

                //put them in the pyEdit
				cache.put(PydevPlugin.ANNOTATIONS_CACHE_KEY, annotations);
            }else{
                if(DEBUG){
                    System.out.println("Occurrences == null");
                }
                return false;
            }
        }
        return true;
    }

    public static RefactoringRequest getRefactoringRequest(final PyEdit pyEdit, PyRefactorAction pyRefactorAction) throws BadLocationException {
    	return getRefactoringRequest(pyEdit, pyRefactorAction, null);
    }
    
    /**
     * @param pyEdit the editor where we should look for the occurrences
     * @param pyRefactorAction the action that will return the initial refactoring request
     * @param ps the pyselection used (if null it will be created in this method)
     * @return a refactoring request suitable for finding the locals in the file
     * @throws BadLocationException
     */
	public static RefactoringRequest getRefactoringRequest(final PyEdit pyEdit, PyRefactorAction pyRefactorAction, PySelection ps) throws BadLocationException {
        final RefactoringRequest req = pyRefactorAction.getRefactoringRequest();
        req.ps = PySelection.createFromNonUiThread(pyEdit);
        
        if(req.ps == null){
        	return null;
        }
        
        req.fillInitialNameAndOffset();
        req.inputName = "foo";
        req.setAdditionalInfo(RefactorerRequestConstants.FIND_DEFINITION_IN_ADDITIONAL_INFO, false);
        req.setAdditionalInfo(RefactorerRequestConstants.FIND_REFERENCES_ONLY_IN_LOCAL_SCOPE, true);
        return req;
    }

    /**
     * @param pyEdit the editor that will have this action
     * @return the action (with the pyedit attached to it)
     */
    public static PyRefactorAction getRefactorAction(PyEdit pyEdit) {
        PyRefactorAction pyRefactorAction = new PyRefactorAction(){
   
            @Override
            protected IPyRefactoring getPyRefactoring() {
                return AbstractPyRefactoring.getPyRefactoring();
            }
   
            @Override
            protected String perform(IAction action, String name, IProgressMonitor monitor) throws Exception {
                throw new RuntimeException("Perform should not be called in this case.");
            }
   
            @Override
            protected String getInputMessage() {
                return null;
            }
            
        };
        pyRefactorAction.setEditor(pyEdit);
        return pyRefactorAction;
    }

    /**
     * @param annotationModel
     */
    @SuppressWarnings("unchecked")
	private synchronized void removeOccurenceAnnotations(IAnnotationModel annotationModel, PyEdit pyEdit) {
        //remove the annotations
        synchronized(getLockObject(annotationModel)){
        	Map<String, Object> cache = pyEdit.cache;
        	if(cache == null){
        		return;
        	}
        	
            Iterator<Annotation> annotationIterator = PydevPlugin.getOccurrenceAnnotationsInPyEdit(pyEdit).iterator();
            while(annotationIterator.hasNext()){
                annotationModel.removeAnnotation(annotationIterator.next());
            }
			cache.put(PydevPlugin.ANNOTATIONS_CACHE_KEY, null);
        }
        //end remove the annotations
    }

    
    /**
     * Gotten from JavaEditor#getLockObject
     */
    private Object getLockObject(IAnnotationModel annotationModel) {
        if (annotationModel instanceof ISynchronizable)
            return ((ISynchronizable)annotationModel).getLockObject();
        else
            return annotationModel;
    }

    /**
     * This is the function that should be called when we want to schedule a request for 
     * a mark occurrences job.
     */
    public static synchronized void scheduleRequest(WeakReference<PyEdit> editor2) {
        MarkOccurrencesJob j = singleton;
        if(j != null){
        	synchronized (j) {
        		j.cancel();
        		singleton = null;
			}
        }
        singleton = new MarkOccurrencesJob(editor2);
        singleton.schedule(750);
    }


}
