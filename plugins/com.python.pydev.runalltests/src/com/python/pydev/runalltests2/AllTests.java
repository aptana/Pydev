/**
 * Copyright (c) 2005-2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Eclipse Public License (EPL).
 * Please see the license.txt included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.python.pydev.runalltests2;

//reference: http://www.eclipsezone.com/eclipse/forums/t65337.html
import java.lang.reflect.Modifier;
import java.util.Enumeration;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit3.runner.ClassPathTestCollector;
import junit3.runner.TestCollector;

public class AllTests {

    private static class ClassFileDetector extends ClassPathTestCollector {
        @Override
        protected boolean isTestClass(String classFileName) {
            return classFileName.endsWith(SUFFIX + ".class") && isValidTest(classNameFromFile(classFileName));
        }
    }

    public static final String SUFFIX = "Test";

    private static void addTestsToSuite(TestCollector collector, TestSuite suite) {
        Enumeration<String> e = collector.collectTests();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            try {
                suite.addTestSuite((Class<? extends TestCase>) Class.forName(name));
            } catch (ClassNotFoundException e1) {
                System.err.println("Cannot load test: " + e1);
            }
        }
    }

    private static boolean isValidTest(String name) {
        try {
            return name.endsWith(SUFFIX) && ((Class.forName(name).getModifiers() & Modifier.ABSTRACT) == 0);
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            return false;
        }
    }

    public static Test suite() {

        BundleTestCollector testCollector = new BundleTestCollector();
        TestSuite suite = new TestSuite(AllTests.class.getName());
        testCollector.collectTests(suite, "org.python", "org.python", "*Test");
        testCollector.collectTests(suite, "com.python", "com.python", "*Test");
        testCollector.collectTests(suite, "com.aptana.interactive_console", "com.aptana.interactive_console", "*Test");
        testCollector.collectTests(suite, "com.aptana.js.interactive_console", "com.aptana.js.interactive_console",
                "*Test");
        // add more lines collectTests(...) calls if necessary
        if (suite.countTestCases() == 0) {
            throw new Error("There are no test cases to run");
        } else {
            return suite;
        }
    }
}
