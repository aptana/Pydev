/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package org.python.pydev.plugin.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.python.pydev.core.docutils.StringUtils;
import org.python.pydev.plugin.PydevPlugin;

public class PydevRootPrefs extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	public static final String PHONE_HOME = "PHONE_HOME";
	public static final boolean DEFAULT_PHONE_HOME = true;

	private BooleanFieldEditor phoneHome;

    public PydevRootPrefs() {
        setPreferenceStore(PydevPlugin.getDefault().getPreferenceStore());
        setDescription(StringUtils.format("PyDev version: %s", PydevPlugin.version)); 
    }

    protected void createFieldEditors() {
        Composite p = getFieldEditorParent();
        phoneHome = new BooleanFieldEditor(
        		PHONE_HOME, "Phone home (critical for usage reporting!)", BooleanFieldEditor.SEPARATE_LABEL, p);
        addField(phoneHome);
	}

    public void init(IWorkbench workbench) {
    }
}
