package org.python.pydev.outline;

import java.util.ArrayList;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.python.pydev.editor.PyEdit;
import org.python.pydev.editor.model.IModelListener;
import org.python.pydev.parser.jython.SimpleNode;
import org.python.pydev.parser.jython.ast.Attribute;
import org.python.pydev.parser.jython.ast.ClassDef;
import org.python.pydev.parser.jython.ast.FunctionDef;
import org.python.pydev.parser.visitors.scope.ASTEntryWithChildren;
import org.python.pydev.parser.visitors.scope.OutlineCreatorVisitor;

/**
 * ParsedModel represents a python file, parsed for OutlineView display
 * It takes PyParser, and converts it into a tree of ParsedItems
 */
public class ParsedModel implements IOutlineModel {

    PyEdit editor;
    PyOutlinePage outline;
    IModelListener modelListener;
    
    ParsedItem root = null; // A list of top nodes in this document. Used as a tree root

    /**
     * @param outline - If not null, view to notify when parser changes
     */
    public ParsedModel(PyOutlinePage outline, PyEdit editor) {
        this.editor = editor;
        this.outline = outline;

        // The notifications are only propagated to the outline page
        //
        // Tell parser that we want to know about all the changes
        // make sure that the changes are propagated on the main thread
        modelListener = new IModelListener() {
            public void modelChanged(final SimpleNode ast) {
                Display.getDefault().asyncExec( new Runnable() {
                    public void run() {
                        OutlineCreatorVisitor visitor = OutlineCreatorVisitor.create(ast);
                        setRoot(new ParsedItem(visitor.getAll().toArray(new ASTEntryWithChildren[0])));
                    }
                });             
            }
        };
        OutlineCreatorVisitor visitor = OutlineCreatorVisitor.create(editor.getAST());
        root = new ParsedItem(visitor.getAll().toArray(new ASTEntryWithChildren[0]));
        editor.addModelListener(modelListener);
    }

    public void dispose() {
        editor.removeModelListener(modelListener);
    }
    
    public Object getRoot() {
        return root;
    }

    // patchRootHelper makes oldItem just like the newItem
    //   the differnce between the two is 
    private void patchRootHelper(ParsedItem oldItem, ParsedItem newItem, 
            ArrayList<ParsedItem> itemsToRefresh, ArrayList<ParsedItem> itemsToUpdate) {
        
        ParsedItem[] newChildren = newItem.getChildren();
        ParsedItem[] oldChildren = oldItem.getChildren();
        
        // stuctural change, different number of children, can stop recursion
        if (newChildren.length != oldChildren.length) {
            oldItem.astChildrenEntries = newItem.astChildrenEntries;
            oldItem.astThis = newItem.astThis;
            oldItem.name = null;
            oldItem.children = null; // forces reinitialization
            itemsToRefresh.add(oldItem);
            
        }else {
            
            // Number of children is the same, fix up all the children
            for (int i=0; i<oldChildren.length; i++) {
                patchRootHelper(oldChildren[i],newChildren[i],itemsToRefresh, itemsToUpdate);
            }
            
            // see if the node needs redisplay
            String oldTitle = oldItem.toString();
            String newTitle = newItem.toString();
            if (!oldTitle.equals(newTitle)){
                itemsToUpdate.add(oldItem);
            }
            
            oldItem.astThis = newItem.astThis;
            oldItem.name = null;
        }
    }
    /*
     * Replaces current root
     */
    public void setRoot(ParsedItem newRoot) {
        // We'll try to do the 'least flicker replace'
        // compare the two root structures, and tell outline what to refresh
        if (root != null) {
            ArrayList<ParsedItem> itemsToRefresh = new ArrayList<ParsedItem>();
            ArrayList<ParsedItem> itemsToUpdate = new ArrayList<ParsedItem>();
            patchRootHelper(root, newRoot, itemsToRefresh, itemsToUpdate);
            if (outline != null) {
                if(outline.isDisposed()){
                    return;
                }
                outline.updateItems(itemsToUpdate.toArray());
                outline.refreshItems(itemsToRefresh.toArray());
            }
            
        }else {
            System.out.println("No old model root?");
        }
    }
    
    /*
     */
    public SimpleNode getSelectionPosition(StructuredSelection sel) {
        if(sel.size() == 1) { // only sync the editing view if it is a single-selection
            Object firstElement = sel.getFirstElement();
            ASTEntryWithChildren p = ((ParsedItem)firstElement).astThis;
            SimpleNode node = p.node;
            if(node instanceof ClassDef){
                ClassDef def = (ClassDef) node;
                node = def.name;
                
            }else if(node instanceof Attribute){
                Attribute attribute = (Attribute) node;
                node = attribute.attr;
                
            }else if(node instanceof FunctionDef){
                FunctionDef def = (FunctionDef) node;
                node = def.name;
            }
            return node;
        }
        return null;
    }
}