/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package org.python.pydev.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.REF;
import org.python.pydev.core.Tuple;
import org.python.pydev.core.bundle.BundleInfo;
import org.python.pydev.core.bundle.IBundleInfo;
import org.python.pydev.core.bundle.ImageCache;
import org.python.pydev.core.callbacks.ICallback;
import org.python.pydev.core.log.Log;
import org.python.pydev.dltk.console.ui.ScriptConsoleUIConstants;
import org.python.pydev.editor.codecompletion.shell.AbstractShell;
import org.python.pydev.logging.ping.AsyncLogPing;
import org.python.pydev.logging.ping.ILogPing;
import org.python.pydev.plugin.nature.PythonNature;
import org.python.pydev.plugin.nature.SystemPythonNature;
import org.python.pydev.plugin.preferences.PydevPrefs;
import org.python.pydev.ui.ColorCache;
import org.python.pydev.ui.interpreters.IronpythonInterpreterManager;
import org.python.pydev.ui.interpreters.JythonInterpreterManager;
import org.python.pydev.ui.interpreters.PythonInterpreterManager;


/**
 * The main plugin class - initialized on startup - has resource bundle for internationalization - has preferences
 */
public class PydevPlugin extends AbstractUIPlugin implements Preferences.IPropertyChangeListener {
    
    public static final String version = "REPLACE_VERSION";
    
    // ----------------- SINGLETON THINGS -----------------------------
    public static IBundleInfo info;
    public static IBundleInfo getBundleInfo(){
        if(PydevPlugin.info == null){
            PydevPlugin.info = new BundleInfo(PydevPlugin.getDefault().getBundle());
        }
        return PydevPlugin.info;
    }
    public static void setBundleInfo(IBundleInfo b){
        PydevPlugin.info = b;
    }
    // ----------------- END BUNDLE INFO THINGS --------------------------
    
    private static IInterpreterManager pythonInterpreterManager;
    public static void setPythonInterpreterManager(IInterpreterManager interpreterManager) {
        PydevPlugin.pythonInterpreterManager = interpreterManager;
    }
    public static IInterpreterManager getPythonInterpreterManager() {
        return getPythonInterpreterManager(false);
    }
    public static IInterpreterManager getPythonInterpreterManager(boolean haltOnStub) {
        return pythonInterpreterManager;
    }

    
    
    
    private static IInterpreterManager jythonInterpreterManager;
    public static void setJythonInterpreterManager(IInterpreterManager interpreterManager) {
        PydevPlugin.jythonInterpreterManager = interpreterManager;
    }
    public static IInterpreterManager getJythonInterpreterManager() {
        return getJythonInterpreterManager(false);
    }
    public static IInterpreterManager getJythonInterpreterManager(boolean haltOnStub) {
        return jythonInterpreterManager;
    }
    
    
    private static IInterpreterManager ironpythonInterpreterManager;
    public static void setIronpythonInterpreterManager(IInterpreterManager interpreterManager) {
        PydevPlugin.ironpythonInterpreterManager = interpreterManager;
    }
    public static IInterpreterManager getIronpythonInterpreterManager() {
        return getIronpythonInterpreterManager(false);
    }
    public static IInterpreterManager getIronpythonInterpreterManager(boolean haltOnStub) {
        return ironpythonInterpreterManager;
    }
    
    public static IInterpreterManager[] getAllInterpreterManagers() {
        return new IInterpreterManager[]{
                PydevPlugin.getPythonInterpreterManager(),
                PydevPlugin.getJythonInterpreterManager(),
                PydevPlugin.getIronpythonInterpreterManager()
        };
    }

    // ----------------- END SINGLETON THINGS --------------------------

    /**
     * returns the interpreter manager for a given nature
     * @param nature the nature from where we want to get the associated interpreter manager
     * 
     * @return the interpreter manager
     */
    public static IInterpreterManager getInterpreterManager(IPythonNature nature) {
        try {
            switch(nature.getInterpreterType()){
                case IInterpreterManager.INTERPRETER_TYPE_JYTHON:
                    return jythonInterpreterManager;
                case IInterpreterManager.INTERPRETER_TYPE_PYTHON:
                    return pythonInterpreterManager;
                case IInterpreterManager.INTERPRETER_TYPE_IRONPYTHON:
                    return ironpythonInterpreterManager;
                default:
                    throw new RuntimeException("Unable to get the interpreter manager for unknown interpreter type: "+nature.getInterpreterType());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    private static PydevPlugin plugin; //The shared instance.

    private ColorCache colorCache;

    private ResourceBundle resourceBundle; //Resource bundle.

    public static final String DEFAULT_PYDEV_SCOPE = "org.python.pydev";
    
    private boolean isAlive;

	private ILogPing asyncLogPing;
	
	public static ILogPing getAsyncLogPing() {
		return getDefault().asyncLogPing;
	}


    /**
     * The constructor.
     */
    public PydevPlugin() {
        super();
        plugin = this;
    }

    public void start(BundleContext context) throws Exception {
    	this.isAlive = true;
        super.start(context);
        try {
            resourceBundle = ResourceBundle.getBundle("org.python.pydev.PyDevPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
        final Preferences preferences = plugin.getPluginPreferences();
        preferences.addPropertyChangeListener(this);
        
        //set them temporarily
        //setPythonInterpreterManager(new StubInterpreterManager(true));
        //setJythonInterpreterManager(new StubInterpreterManager(false));
        
        //changed: the interpreter manager is always set in the initialization (initialization 
        //has some problems if that's not done).
        setPythonInterpreterManager(new PythonInterpreterManager(preferences));
        setJythonInterpreterManager(new JythonInterpreterManager(preferences));
        setIronpythonInterpreterManager(new IronpythonInterpreterManager(preferences));
        
        handlePing();

        //restore the nature for all python projects -- that's done when the project is set now.
//        new Job("PyDev: Restoring projects python nature"){
//
//            protected IStatus run(IProgressMonitor monitor) {
//                try{
//                    
//                    IProject[] projects = getWorkspace().getRoot().getProjects();
//                    for (int i = 0; i < projects.length; i++) {
//                        IProject project = projects[i];
//                        try {
//                            if (project.isOpen() && project.hasNature(PythonNature.PYTHON_NATURE_ID)) {
//                                PythonNature.addNature(project, monitor, null, null);
//                            }
//                        } catch (Exception e) {
//                            PydevPlugin.log(e);
//                        }
//                    }
//                }catch(Throwable t){
//                    t.printStackTrace();
//                }
//                return Status.OK_STATUS;
//            }
//            
//        }.schedule();
        
    }
	private void handlePing() {
		try {
			File base;
			try {
				IPath stateLocation = plugin.getStateLocation();
			    String osString = stateLocation.toOSString();
			    if (osString.length() > 0) {
			        char c = osString.charAt(osString.length() - 1);
			        if (c != '\\' && c != '/') {
			            osString += '/';
			        }
			    }
			    base = new File(osString);
			    if(!base.exists()){
			    	base.mkdirs();
			    }
			} catch (Exception e) {
			    //it may fail in tests... (save it in default folder in this cases)
			    PydevPlugin.log(IStatus.ERROR, "Error getting persisting folder", e, false);
			    base = new File(".");
			}
			File file = new File(base, "ping.log");
			
			asyncLogPing = new AsyncLogPing(REF.getFileAbsolutePath(file));
		} catch (Exception e) {
			Log.log(e);
			
			//Cannot fail: create empty stub!
			asyncLogPing = new ILogPing() {
				
				public void stop() {
				}
				
				public void send() {
				}
				
				public void addPingStartPlugin() {
				}
				
				public void addPingOpenEditor() {
				}
			};
		}
		
		if(!Platform.inDevelopmentMode() || ILogPing.FORCE_SEND_WHEN_IN_DEV_MODE){
			Job job = new Job("Sending Ping...") //$NON-NLS-1$
			{
				@Override
				protected IStatus run(IProgressMonitor monitor)
				{
					asyncLogPing.addPingStartPlugin();
					asyncLogPing.send();
					schedule(1000 * 60 * 60 * 24); //Reschedule for another ping in 24 hours
					return Status.OK_STATUS;
				}
			};
			job.setSystem(true);
			job.setPriority(Job.BUILD);
			job.schedule();
		}
	}
    
    private Set<String> erasePrefixes = new HashSet<String>();
    
    public File getTempFile(String prefix) {
        erasePrefixes.add(prefix);
        IPath stateLocation = getStateLocation();
        File file = stateLocation.toFile();
        File tempFileAt = REF.getTempFileAt(file, prefix);
        return tempFileAt;
    }

    /**
     * This is called when the plugin is being stopped.
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        IPath stateLocation = getStateLocation();
        File file = stateLocation.toFile();
        for(String prefix:erasePrefixes){
            REF.clearTempFilesAt(file, prefix);
        }
    	try {
			asyncLogPing.stop();
		} catch (Exception e1) {
			Log.log(e1);
		}
        this.isAlive = false;
        try {
            //stop the running shells
            AbstractShell.shutdownAllShells();

            Preferences preferences = plugin.getPluginPreferences();
            preferences.removePropertyChangeListener(this);
            
            //save the natures (code completion stuff) -- and only the ones initialized 
            //(no point in getting the ones not initialized)
            for(PythonNature nature:PythonNature.getInitializedPythonNatures()){
                try {
                    nature.saveAstManager();
                } catch (Exception e) {
                    PydevPlugin.log(e);
                }
            }
        } finally{
            super.stop(context);
        }
    }

    public static boolean isAlive(){
    	PydevPlugin p = plugin;
    	if(p == null){
    		return false;
    	}
    	return p.isAlive;
    }
    
    public static PydevPlugin getDefault() {
        return plugin;
    }

    public static String getPluginID() {
        if(PydevPlugin.getDefault() == null){
            return "PyDevPluginID(null plugin)";
        }
        return PydevPlugin.getBundleInfo().getPluginID();
    }

    /**
     * Returns the workspace instance.
     */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    public static Status makeStatus(int errorLevel, String message, Throwable e) {
        return new Status(errorLevel, getPluginID(), errorLevel, message, e);
    }

    /**
     * Returns the string from the plugin's resource bundle, or 'key' if not found.
     */
    public static String getResourceString(String key) {
        ResourceBundle bundle = plugin.getResourceBundle();
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }
    
    
    public void propertyChange(Preferences.PropertyChangeEvent event) {
        //        System.out.println( event.getProperty()
        //         + "\n\told setting: "
        //         + event.getOldValue()
        //         + "\n\tnew setting: "
        //         + event.getNewValue());
    }

    public static void log(String message, Throwable e) {
        log(IStatus.ERROR, message, e);
    }
    
    public static void log(int errorLevel, String message, Throwable e) {
        log(errorLevel, message, e, true);
    }
    public static void log(String message, Throwable e, boolean printToConsole) {
        log(IStatus.ERROR, message, e, printToConsole);
    }

    public static void logInfo(Throwable e) {
        log(IStatus.INFO, e.getMessage(), e, true);
    }

    /**
     * @param errorLevel IStatus.[OK|INFO|WARNING|ERROR]
     */
    public static void log(int errorLevel, String message, Throwable e, boolean printToConsole) {
        if(Log.isDebugging() && printToConsole){
            if(errorLevel == IStatus.ERROR){
                System.out.println("Error received...");
            }else{
                System.out.println("Log received...");
            }
            System.out.println(message);
            System.err.println(message);
            if(e != null){
                e.printStackTrace();
            }
        }
        
        if (getDefault() != null) {
            try {
                Status s = new Status(errorLevel, getPluginID(), errorLevel, message, e);
                getDefault().getLog().log(s);
            } catch (Throwable e1) {
                //logging should never fail!
            }
        }
    }

    public static void log(IStatus status) {
        getDefault().getLog().log(status);
    }
    
    public static void log(Throwable e) {
        log(e, true);
    }
    
    public static void log(Throwable e, boolean printToConsole) {
        log(IStatus.ERROR, e.getMessage() != null ? e.getMessage() : "No message gotten.", e, printToConsole);
    }

    public static void logInfo(String msg) {
        IStatus s = PydevPlugin.makeStatus(IStatus.INFO, msg, null);
        PydevPlugin plug = getDefault();
        if(plug == null){//testing mode
            System.out.println(msg);
        }else{
            plug.getLog().log(s);
        }
    }
    
    public static CoreException log(String msg) {
        IStatus s = PydevPlugin.makeStatus(IStatus.ERROR, msg, new RuntimeException(msg));
        CoreException e = new CoreException(s);
        PydevPlugin.log(e);
        return e;
    }

    /**
     * @return the script to get the variables.
     * 
     * @throws CoreException
     */
    public static File getScriptWithinPySrc(String targetExec) throws CoreException {
        IPath relative = new Path("PySrc").addTrailingSeparator().append(targetExec);
        return PydevPlugin.getBundleInfo().getRelativePath(relative);
    }

    /**
     * @return the cache that should be used to access images within the pydev plugin.
     */
    public static ImageCache getImageCache(){
        return PydevPlugin.getBundleInfo().getImageCache();
    }
    
    
    //Images for the console
    private static final String[][] IMAGES = new String[][] { { "icons/save.gif", //$NON-NLS-1$
            ScriptConsoleUIConstants.SAVE_SESSION_ICON }, { "icons/terminate.gif", //$NON-NLS-1$
            ScriptConsoleUIConstants.TERMINATE_ICON } };

    @Override
    protected void initializeImageRegistry(ImageRegistry registry) {
        for (int i = 0; i < IMAGES.length; ++i) {
            URL url = getDefault().getBundle().getEntry(IMAGES[i][0]);
            registry.put(IMAGES[i][1], ImageDescriptor.createFromURL(url));
        }
    }

    public ImageDescriptor getImageDescriptor(String key) {
        return getImageRegistry().getDescriptor(key);
    }
    //End Images for the console

    
    /**
     * @param file the file we want to get info on.
     * @return a tuple with the nature to be used and the name of the module represented by the file in that scenario.
     */
    public static Tuple<SystemPythonNature, String> getInfoForFile(File file){
        String modName = null;
        IInterpreterManager pythonInterpreterManager = getPythonInterpreterManager(false);
        IInterpreterManager jythonInterpreterManager = getJythonInterpreterManager(false);
        if(pythonInterpreterManager == null || jythonInterpreterManager == null){
            return null;
        }
    
        SystemPythonNature systemPythonNature = null;
        SystemPythonNature pySystemPythonNature = null;
        SystemPythonNature jySystemPythonNature = null;
        
        try {
            systemPythonNature = new SystemPythonNature(pythonInterpreterManager);
            pySystemPythonNature = systemPythonNature;
            modName = systemPythonNature.resolveModule(file);
        } catch (Exception e) {
            // that's ok
        }
        if(modName == null){
            try {
                systemPythonNature = new SystemPythonNature(jythonInterpreterManager);
                jySystemPythonNature = systemPythonNature;
                modName = systemPythonNature.resolveModule(file);
            } catch (Exception e) {
                // that's ok
            }
        }
        if(modName != null){
            return new Tuple<SystemPythonNature, String>(systemPythonNature, modName);
        }else{
            //unable to discover it
            try {
                // the default one is python (actually, this should never happen, but who knows)
                pythonInterpreterManager.getDefaultInterpreter();
                modName = getModNameFromFile(file);
                return new Tuple<SystemPythonNature, String>(pySystemPythonNature, modName);
            } catch (Exception e) {
                //the python interpreter manager is not valid or not configured
                try {
                    // the default one is jython
                    jythonInterpreterManager.getDefaultInterpreter();
                    modName = getModNameFromFile(file);
                    return new Tuple<SystemPythonNature, String>(jySystemPythonNature, modName);
                } catch (Exception e1) {
                    // ok, nothing to do about it, no interpreter is configured
                    return null;
                }
            }
        }
    }
    
    /**
     * This is the last resort (should not be used anywhere else).
     */
    private static String getModNameFromFile(File file) {
        if(file == null){
            return null;
        }
        String name = file.getName();
        int i = name.indexOf('.');
        if (i != -1){
            return name.substring(0, i);
        }
        return name;
    }

    /**
     * Given a resource get the string in the filesystem for it.
     */
    public static String getIResourceOSString(IResource f) {
        IPath rawLocation = f.getRawLocation();
        if(rawLocation == null){
            return null; //yes, we could have a resource that was deleted but we still have it's representation...
        }
        String fullPath = rawLocation.toOSString();
        //now, we have to make sure it is canonical...
        File file = new File(fullPath);
        if(file.exists()){
            return REF.getFileAbsolutePath(file);
        }else{
            //it does not exist, so, we have to check its project to validate the part that we can
            IProject project = f.getProject();
            IPath location = project.getLocation();
            File projectFile = location.toFile();
            if(projectFile.exists()){
                String projectFilePath = REF.getFileAbsolutePath(projectFile);
                
                if(fullPath.startsWith(projectFilePath)){
                    //the case is all ok
                    return fullPath;
                }else{
                    //the case appears to be different, so, let's check if this is it...
                    if(fullPath.toLowerCase().startsWith(projectFilePath.toLowerCase())){
                        String relativePart = fullPath.substring(projectFilePath.length());
                        
                        //at least the first part was correct
                        return projectFilePath+relativePart;
                    }
                }
            }
        }
        
        //it may not be correct, but it was the best we could do...
        return fullPath;
    }
    
    /**
     * Writes to the workspace a given object (in the given filename)
     */
    public static void writeToWorkspaceMetadata(Object obj, String fileName) {
        Bundle bundle = Platform.getBundle("org.python.pydev");
        IPath path = Platform.getStateLocation( bundle );       
        path = path.addTrailingSeparator();
        path = path.append(fileName);
        try {
            FileOutputStream out = new FileOutputStream(path.toFile());
            REF.writeToStreamAndCloseIt(obj, out);
            
        } catch (Exception e) {
            PydevPlugin.log(e);
            throw new RuntimeException(e);
        }               
    }

    /**
     * Loads from the workspace metadata a given object (given the filename)
     */
    public static Object readFromWorkspaceMetadata(String fileName) {
        Bundle bundle = Platform.getBundle("org.python.pydev");
        IPath path = Platform.getStateLocation( bundle );       
        path = path.addTrailingSeparator();
        path = path.append(fileName);
        
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(path.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return REF.readFromInputStreamAndCloseIt(new ICallback<Object, ObjectInputStream>(){

            public Object call(ObjectInputStream arg) {
                try{
                    return arg.readObject();
                }catch(Exception e){
                    throw new RuntimeException(e);
                }
            }}, 
            
            fileInputStream);
    }
    /**
     * @return
     */
    public static ColorCache getColorCache() {
        PydevPlugin plugin = getDefault();
        if(plugin.colorCache == null){
            plugin.colorCache = new ColorCache(PydevPrefs.getChainedPrefStore()) {
            };
        }
        return plugin.colorCache;
    }

}