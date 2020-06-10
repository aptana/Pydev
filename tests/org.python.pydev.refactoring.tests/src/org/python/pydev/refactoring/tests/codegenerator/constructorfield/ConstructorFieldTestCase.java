/* 
 * Copyright (C) 2006, 2007  Dennis Hunziker, Ueli Kistler 
 */

package org.python.pydev.refactoring.tests.codegenerator.constructorfield;

import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.python.pydev.core.MisconfigurationException;
import org.python.pydev.refactoring.ast.adapters.IClassDefAdapter;
import org.python.pydev.refactoring.ast.adapters.ModuleAdapter;
import org.python.pydev.refactoring.codegenerator.constructorfield.edit.ConstructorMethodEdit;
import org.python.pydev.refactoring.tests.core.AbstractIOTestCase;

import com.thoughtworks.xstream.XStream;

public class ConstructorFieldTestCase extends AbstractIOTestCase {

    public ConstructorFieldTestCase(String name) {
        super(name);
    }

    @Override
    public void runTest() throws Throwable {
        MockupConstructorFieldConfig config = initConfig();

        MockupConstructorFieldRequestProcessor requestProcessor = setupRequestProcessor(config);

        IDocument refactoringDoc = applyConstructorUsingFields(requestProcessor);

        this.setTestGenerated(refactoringDoc.get());
        String expected = getExpected();
        String generated = getGenerated();
        assertContentsEqual(expected, generated);
    }

    private IDocument applyConstructorUsingFields(MockupConstructorFieldRequestProcessor requestProcessor)
            throws BadLocationException, MalformedTreeException, MisconfigurationException {
        ConstructorMethodEdit constructorEdit = new ConstructorMethodEdit(requestProcessor.getRefactoringRequests()
                .get(0));

        IDocument refactoringDoc = new Document(data.source);
        constructorEdit.getEdit().apply(refactoringDoc);
        return refactoringDoc;
    }

    private MockupConstructorFieldRequestProcessor setupRequestProcessor(MockupConstructorFieldConfig config)
            throws Throwable {
        ModuleAdapter module = this.createModuleAdapterFromDataSource();
        List<IClassDefAdapter> classes = module.getClasses();
        assertTrue(classes.size() > 0);

        MockupConstructorFieldRequestProcessor requestProcessor = new MockupConstructorFieldRequestProcessor(module,
                config);
        return requestProcessor;
    }

    private MockupConstructorFieldConfig initConfig() {
        MockupConstructorFieldConfig config = null;
        XStream xstream = new XStream();
        xstream.alias("config", MockupConstructorFieldConfig.class);

        if (data.config.length() > 0) {
            config = (MockupConstructorFieldConfig) xstream.fromXML(data.getConfigContents());
        } else {
            fail("Could not unserialize configuration");
        }
        return config;
    }
}
