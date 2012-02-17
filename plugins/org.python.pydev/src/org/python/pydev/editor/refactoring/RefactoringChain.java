package org.python.pydev.editor.refactoring;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.python.pydev.core.Tuple;
import org.python.pydev.editor.codecompletion.revisited.visitors.AssignDefinition;
import org.python.pydev.editor.model.ItemPointer;
import org.python.pydev.parser.visitors.scope.ASTEntry;


/**
 * An ordered collection of refactorers. Refactoring operations are attempted for each instance
 * until one completes successfully.
 * 
 * Currently, only findDefinition() calls are chained; all other calls will be invoked on at most
 * one refactorer instance. For calls to be usefully chained, each call must return some indication
 * of whether it is considered to have failed or succeeded. Clarification of IPyRefactoring and
 * IPyRefactoring2 semantics would allow for chaining to be extended to additional methods.
 * 
 * It is prudent to add refactorers with a high success rate to the front of the chain, to minimize
 * extra processing.
 * 
 * @author Haw-Bin Chai
 */
public class RefactoringChain implements IPyRefactoring, IPyRefactoring2 {
    // LinkedHashSet's preserve insertion order. Refactorer instances may not appear more than once
    // in each collection.
    private LinkedHashSet<IPyRefactoring> refactorers;
    private LinkedHashSet<IPyRefactoring2> refactorers2;

    public RefactoringChain() {
        refactorers = new LinkedHashSet<IPyRefactoring>();
        refactorers2 = new LinkedHashSet<IPyRefactoring2>();
    }
    
    /**
     * Initializes the chain with the given refactorer.
     * 
     * @param defaultRefactorer
     */
    public RefactoringChain(Object defaultRefactorer) {
        this();
        addRefactorer(defaultRefactorer);
    }
    
    /**
     * Calls findClassHierarchy() on the first composed IPyRefactoring2. Returns null if
     * there is no such refactoring instance to use.
     */
    @Override
    public HierarchyNodeModel findClassHierarchy(RefactoringRequest request,
            boolean findOnlyParents) {
        Iterator<IPyRefactoring2> it = refactorers2.iterator();
        if (it.hasNext()) {
            return it.next().findClassHierarchy(request, findOnlyParents);
        }
        return null;
    }

    /**
     * Calls areAllInSameClassHierarchy on the first composed IPyRefactoring2. Returns false if
     * there is no such refactoring instance to use.
     */
    @Override
    public boolean areAllInSameClassHierarchy(List<AssignDefinition> defs) {
        Iterator<IPyRefactoring2> it = refactorers2.iterator();
        if (it.hasNext()) {
            return it.next().areAllInSameClassHierarchy(defs);
        }
        return false;
    }

    /**
     * Calls findAllOccurrences on the first composed IPyRefactoring2. Returns null if there is no
     * such refactoring instance to use.
     */
    @Override
    public Map<Tuple<String, File>, HashSet<ASTEntry>> findAllOccurrences(
            RefactoringRequest req) throws OperationCanceledException,
            CoreException {
        Iterator<IPyRefactoring2> it = refactorers2.iterator();
        if (it.hasNext()) {
            return it.next().findAllOccurrences(req);
        }
        return null;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * Calls rename() on the first composed IPyRefactoring. Returns null if there is no such
     * refactoring instance to use.
     */
    @Override
    public String rename(RefactoringRequest request) {
        Iterator<IPyRefactoring> it = refactorers.iterator();
        if (it.hasNext()) {
            return it.next().rename(request);
        }
        return null;
    }

    /**
     * Calls findDefinition() on EACH composed IPyRefactoring until a non-empty array is returned.
     */
    @Override
    public ItemPointer[] findDefinition(RefactoringRequest request)
            throws TooManyMatchesException {
        for (IPyRefactoring refactorer : refactorers) {
            ItemPointer[] items = refactorer.findDefinition(request);
            if (items.length > 0) {
                return items;
            }
        }
        return new ItemPointer[0];
    }
    
    /**
     * Adds refactorer to the internal collections of composed refactorers, if it is an instance of
     * IPyRefactoring or IPyRefactoring2.
     * 
     * @param refactorer
     */
    public void addRefactorer(Object refactorer) {
        if (refactorer instanceof IPyRefactoring) {
            refactorers.add((IPyRefactoring) refactorer);
        }
        if (refactorer instanceof IPyRefactoring2) {
            refactorers2.add((IPyRefactoring2) refactorer);
        }
    }
    
    public void addAllRefactorers(List<Object> refactorers) {
        for (Object refactorer : refactorers) {
            addRefactorer(refactorer);
        }
    }
    
    /**
     * @return true if at least one of each of IPyRefactoring and IPyRefactoring2 have been added
     *         to this chain, and false otherwise.
     */
    public boolean hasRefactorers() {
        return !refactorers.isEmpty() && !refactorers2.isEmpty();
    }
    
    public void clear() {
        refactorers.clear();
        refactorers2.clear();
    }
}
