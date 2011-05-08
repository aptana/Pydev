/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
/*
 * @author: fabioz
 * Created: January 2004
 */
 
package org.python.pydev.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.ui.texteditor.ITextEditorExtension2;
import org.python.pydev.core.docutils.PySelection;
import org.python.pydev.core.log.Log;
import org.python.pydev.core.structure.FastStringBuffer;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.editor.autoedit.DefaultIndentPrefs;
import org.python.pydev.editor.preferences.PydevEditorPrefs;
import org.python.pydev.plugin.PydevPlugin;
import org.python.pydev.plugin.preferences.PydevPrefs;

/**
 * @author Fabio Zadrozny
 * 
 * Superclass of all our actions. Contains utility functions.
 * 
 * Subclasses should implement run(IAction action) method.
 */
public abstract class PyAction extends Action implements IEditorActionDelegate {
	
	protected PyAction() {
		super();
	}

	protected PyAction(String text, int style){
		super(text, style);
	}
	
    public static Shell getShell() {
        IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
        if(activeWorkbenchWindow == null){
            PydevPlugin.log("Error. Not currently with thread access (so, there is no activeWorkbenchWindow available)");
            return null;
        }
        return activeWorkbenchWindow.getShell();
    }

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow activeWorkbenchWindow = workbench.getActiveWorkbenchWindow();
		return activeWorkbenchWindow;
	}
    

    // Always points to the current editor
    protected volatile IEditorPart targetEditor;

    public void setEditor(IEditorPart targetEditor) {
        this.targetEditor = targetEditor;
    }
    
    /**
     * This is an IEditorActionDelegate override
     */
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        setEditor(targetEditor);
    }

    /**
     * Activate action  (if we are getting text)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        action.setEnabled(true);
    }

    public static String getDelimiter(IDocument doc){
        return PySelection.getDelimiter(doc);
    }
    
    /**
     * This function returns the text editor.
     */
    protected ITextEditor getTextEditor() {
        if (targetEditor instanceof ITextEditor) {
            return (ITextEditor) targetEditor;
        } else {
            throw new RuntimeException("Expecting text editor. Found:"+targetEditor.getClass().getName());
        }
    }

    /**
     * @return python editor.
     */
    protected PyEdit getPyEdit() {
        if (targetEditor instanceof PyEdit) {
            return (PyEdit) targetEditor;
        } else {
            throw new RuntimeException("Expecting PyEdit editor. Found:"+targetEditor.getClass().getName());
        }
    }
    
    
    /**
     * @return true if the contents of the editor may be changed. Clients MUST call this before actually
     * modifying the editor.
     */
	protected boolean canModifyEditor() {
		ITextEditor editor = getTextEditor();
		
		if (editor instanceof ITextEditorExtension2) {
			return ((ITextEditorExtension2) editor).isEditorInputModifiable();
			
		} else if (editor instanceof ITextEditorExtension) {
			return !((ITextEditorExtension) editor).isEditorInputReadOnly();
			
		} else if (editor != null) {
			return editor.isEditable();
			
		} 

		//If we don't have the editor, let's just say it's ok (working on document).
		return true;
	}

    /**
     * Helper for setting caret
     * @param pos
     * @throws BadLocationException
     */
    protected void setCaretPosition(int pos) throws BadLocationException {
        getTextEditor().selectAndReveal(pos, 0);
    }

    /**
     * Are we in the first char of the line with the offset passed?
     * @param doc
     * @param cursorOffset
     */
    protected void isInFirstVisibleChar(IDocument doc, int cursorOffset) {
        try {
            IRegion region = doc.getLineInformationOfOffset(cursorOffset);
            int offset = region.getOffset();
            String src = doc.get(offset, region.getLength());
            if ("".equals(src))
                return;
            int i = 0;
            while (i < src.length()) {
                if (!Character.isWhitespace(src.charAt(i))) {
                    break;
                }
                i++;
            }
            setCaretPosition(offset + i - 1);
        } catch (BadLocationException e) {
            beep(e);
            return;
        }
    }


    /**
     * Returns the position of the last non whitespace char in the current line.
     * @param doc
     * @param cursorOffset
     * @return position of the last character of the line (returned as an absolute
     *            offset)
     * 
     * @throws BadLocationException
     */
    protected int getLastCharPosition(IDocument doc, int cursorOffset)
        throws BadLocationException {
        IRegion region;
        region = doc.getLineInformationOfOffset(cursorOffset);
        int offset = region.getOffset();
        String src = doc.get(offset, region.getLength());

        int i = src.length();
        boolean breaked = false;
        while (i > 0 ) {
            i--;
            //we have to break if we find a character that is not a whitespace or a tab.
            if (   Character.isWhitespace(src.charAt(i)) == false && src.charAt(i) != '\t'  ) {
                breaked = true;
                break;
            }
        }
        if (!breaked){
            i--;
        }
        return (offset + i);
    }

    /**
     * Goes to first char of the line.
     * @param doc
     * @param cursorOffset
     */
    protected void gotoFirstChar(IDocument doc, int cursorOffset) {
        try {
            IRegion region = doc.getLineInformationOfOffset(cursorOffset);
            int offset = region.getOffset();
            setCaretPosition(offset);
        } catch (BadLocationException e) {
            beep(e);
        }
    }

    /**
     * Goes to the first visible char.
     * @param doc
     * @param cursorOffset
     */
    protected void gotoFirstVisibleChar(IDocument doc, int cursorOffset) {
        try {
            setCaretPosition(PySelection.getFirstCharPosition(doc, cursorOffset));
        } catch (BadLocationException e) {
            beep(e);
        }
    }

    /**
     * Goes to the first visible char.
     * @param doc
     * @param cursorOffset
     */
    protected boolean isAtFirstVisibleChar(IDocument doc, int cursorOffset) {
        try {
            return PySelection.getFirstCharPosition(doc, cursorOffset) == cursorOffset;
        } catch (BadLocationException e) {
            return false;
        }
    }

    //================================================================
    // HELPER FOR DEBBUGING... 
    //================================================================

    /*
     * Beep...humm... yeah....beep....ehehheheh
     */
    protected static void beep(Exception e) {
        try{
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay().beep();
        }catch(IllegalStateException x){
            //ignore, workbench has still not been created
        }
        Log.debug(e);
    }


    /**
     * 
     */
    public static String getLineWithoutComments(String sel) {
        return sel.replaceAll("#.*", "");
    }
    
    /**
     * 
     */
    public static String getLineWithoutComments(PySelection ps) {
        return getLineWithoutComments(ps.getCursorLineContents());
    }


    /**
     * Counts the number of occurences of a certain character in a string.
     * 
     * @param line the string to search in
     * @param c the character to search for
     * @return an integer (int) representing the number of occurences of this character
     */
    public static int countChars(char c, String line) {
        int ret = 0;
        int len = line.length();
        for (int i = 0; i < len; i++) {
            if(line.charAt(i) == c){
                ret += 1;
            }
        }
        return ret;
    }
    
    /**
     * Counts the number of occurences of a certain character in a string.
     * 
     * @param line the string to search in
     * @param c the character to search for
     * @return an integer (int) representing the number of occurences of this character
     */
    public static int countChars(char c, StringBuffer line) {
        int ret = 0;
        int len = line.length();
        for (int i = 0; i < len; i++) {
            if(line.charAt(i) == c){
                ret += 1;
            }
        }
        return ret;
    }
    
    /**
     * Counts the number of occurences of a certain character in a string.
     * 
     * @param line the string to search in
     * @param c the character to search for
     * @return an integer (int) representing the number of occurences of this character
     */
    public static int countChars(char c, FastStringBuffer line) {
        int ret = 0;
        int len = line.length();
        for (int i = 0; i < len; i++) {
            if(line.charAt(i) == c){
                ret += 1;
            }
        }
        return ret;
    }
    


    public static String lowerChar(String s, int pos){
        char[] ds = s.toCharArray(); 
        ds[pos] = Character.toLowerCase(ds[pos]);
        return new String(ds);
    }

    /**
     * @param string
     * @param j
     * @return
     */
    public static boolean stillInTok(String string, int j) {
        char c = string.charAt(j);
    
        return c != '\n' && c != '\r' && c != ' ' && c != '.' && c != '(' && c != ')' && c != ',' && c != ']' && c != '[' && c != '#';
    }

    /**
     * 
     * @return indentation string (always recreated) 
     */
    public static String getStaticIndentationString(PyEdit edit) {
        try {
            int tabWidth = DefaultIndentPrefs.getStaticTabWidth();
            boolean useSpaces = PydevPrefs.getPreferences().getBoolean(PydevEditorPrefs.SUBSTITUTE_TABS);
            boolean forceTabs = false;
            if (edit != null){
                forceTabs = edit.getIndentPrefs().getForceTabs();
            }
            
            String identString;

            if (useSpaces && !forceTabs){
                identString = PyAction.createStaticSpaceString(tabWidth);
            }else{
                identString = "\t";
            }
            
            return identString;
        } catch (Exception e) {
            
            PydevPlugin.log(e, false); //no need te print it to the console - happens regularly whed doing unit-tests without the eclipse env
            return "    "; //default
        }
    }

    private static String createStaticSpaceString(int tabWidth) {
        FastStringBuffer b = new FastStringBuffer(tabWidth);
        while (tabWidth-- > 0){
            b.append(" ");
        }
        return b.toString();
    }
    
    

    /**
     * @param ps the selection that contains the document
     */
    protected void revealSelEndLine(PySelection ps) {
        // Put cursor at the first area of the selection
        int docLen = ps.getDoc().getLength()-1;
        IRegion endLine = ps.getEndLine();
        if(endLine != null){
            int curOffset = endLine.getOffset();
            getTextEditor().selectAndReveal(curOffset<docLen?curOffset:docLen, 0);
        }
    }
}
