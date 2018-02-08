/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler 
 */

package org.python.pydev.refactoring.tests.codegenerator;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.python.pydev.refactoring.tests.codegenerator.constructorfield.ConstructorFieldTestSuite;
import org.python.pydev.refactoring.tests.codegenerator.generateproperties.GeneratePropertiesTestSuite;
import org.python.pydev.refactoring.tests.codegenerator.overridemethods.OverrideMethodsTestSuite;

public final class AllTests {
    private AllTests() {
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("Codegenerator Tests");
        // $JUnit-BEGIN$
        suite.addTest(ConstructorFieldTestSuite.suite());
        suite.addTest(OverrideMethodsTestSuite.suite());
        suite.addTest(GeneratePropertiesTestSuite.suite());
        // $JUnit-END$
        return suite;
    }

}
