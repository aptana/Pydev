/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.python.pydev.debug.newconsole;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.python.pydev.core.log.Log;
import org.python.pydev.dltk.console.IScriptConsoleShell;
import org.python.pydev.dltk.console.ui.IScriptConsoleViewer;
import org.python.pydev.dltk.console.ui.ScriptConsoleTextHover;

public class PydevConsoleTextHover extends ScriptConsoleTextHover {

    private IScriptConsoleShell interpreterShell;

    public PydevConsoleTextHover(IScriptConsoleShell interpreterShell) {
        this.interpreterShell = interpreterShell;
    }

    protected String getHoverInfoImpl(IScriptConsoleViewer viewer, IRegion hoverRegion) {
        try {
            IDocument document = viewer.getDocument();
            int cursorPosition = hoverRegion.getOffset();

            return interpreterShell.getDescription(document, cursorPosition);
        } catch (Exception e) {
            Log.log(e);
            return null;
        }
    }
}
