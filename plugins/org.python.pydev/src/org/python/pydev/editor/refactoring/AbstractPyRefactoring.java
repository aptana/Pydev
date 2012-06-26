/**
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
/*
 * Created on May 5, 2005
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.editor.refactoring;

import org.python.pydev.core.ExtensionHelper;

/**
 * @author Fabio Zadrozny
 */
public abstract class AbstractPyRefactoring implements IPyRefactoring {
    /**
     * Instead of making all static, let's use a singleton... it may be
     * useful...
     */
    private volatile static RefactoringChain pyRefactoring;

    /**
     * @return the pyrefactoring instance that is available (can be some plugin
     *         contribution).
     */
    public synchronized static IPyRefactoring getPyRefactoring() {
        // The default refactoring implementation is placed at the front of the chain. Other user
        // refactoring implementations, if any, are added afterwards.
        if (AbstractPyRefactoring.pyRefactoring == null) {
            RefactoringChain chain = new RefactoringChain();
            chain.addRefactorer(ExtensionHelper.getParticipant(ExtensionHelper.PYDEV_REFACTORING));

            if (!chain.hasRefactorers()) {
                throw new RuntimeException(
                        "Refactoring engine not in place! com.python.pydev.refactoring plugin not in place?");
            }

            chain.addAllRefactorers(ExtensionHelper
                    .getParticipants(ExtensionHelper.PYDEV_USER_REFACTORING));

            AbstractPyRefactoring.pyRefactoring = chain;
        }

        return AbstractPyRefactoring.pyRefactoring;
    }

    /**
     * Use only for testing!!!
     * 
     * @param refactorer
     */
    public synchronized static void setPyRefactoring(IPyRefactoring refactorer) {
        pyRefactoring.clear();
        pyRefactoring.addRefactorer(refactorer);
    }
}
