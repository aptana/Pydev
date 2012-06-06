package org.python.pydev.jython;

import org.eclipse.ui.IStartup;

/**
 * This class exists solely so that a different class than 
 * the activator can be provided to do the early startup.
 * 
 * See the documentation for earlyStartup as to why
 * the earlyStartup should not be in JythonPlugin 
 * directly.
 */
public class JythonPluginEarlyStartup implements IStartup {

	public void earlyStartup() {
		JythonPlugin.getDefault().createPyDevScriptingConsole();
	}

}
