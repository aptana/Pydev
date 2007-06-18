/*
 * Created on May 24, 2005
 *
 * @author Fabio Zadrozny
 */
package org.python.pydev.editor.codecompletion.revisited;

import java.util.Collection;
import java.util.List;
import java.util.SortedMap;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.python.pydev.core.IInterpreterInfo;
import org.python.pydev.core.IInterpreterManager;
import org.python.pydev.core.IModule;
import org.python.pydev.core.IPythonNature;
import org.python.pydev.core.ISystemModulesManager;
import org.python.pydev.core.IToken;
import org.python.pydev.core.ModulesKey;
import org.python.pydev.core.cache.LRUCache;
import org.python.pydev.editor.codecompletion.revisited.modules.AbstractModule;
import org.python.pydev.editor.codecompletion.revisited.modules.CompiledModule;
import org.python.pydev.editor.codecompletion.revisited.modules.EmptyModule;
import org.python.pydev.editor.codecompletion.revisited.modules.SourceModule;

/**
 * @author Fabio Zadrozny
 */
public class SystemModulesManager extends ModulesManager implements ISystemModulesManager{

    private static final long serialVersionUID = 2L;
    private String[] builtins;
    /**
     * The system modules manager may have a nature if we create a SystemASTManager
     */
    private transient IPythonNature nature;

    /**
     * @param forcedLibs
     */
    public SystemModulesManager(Collection<String> forcedLibs) {
        regenerateForcedBuilltins(forcedLibs);
    }

    /** 
     * @see org.python.pydev.core.ISystemModulesManager#regenerateForcedBuilltins(java.util.Collection)
     */
    public void regenerateForcedBuilltins(Collection<String> forcedLibs){
        this.builtins = (String[]) forcedLibs.toArray(new String[0]);
    }
    
    public String[] getBuiltins(String defaultSelectedInterpreter) {
    	return getBuiltins();
    }
    
    /** 
     * @see org.python.pydev.core.ISystemModulesManager#getBuiltins()
     */
    public String[] getBuiltins() {
        return this.builtins;
    }

    /** 
     * @see org.python.pydev.core.ISystemModulesManager#setBuiltins(java.util.Collection)
     */
    public void setBuiltins(Collection<String> forcedLibs) {
        regenerateForcedBuilltins(forcedLibs);
    }

	public void setPythonNature(IPythonNature nature) {
		this.nature = nature;
	}

	public IPythonNature getNature() {
	    return this.nature;
	}

	public ISystemModulesManager getSystemModulesManager() {
		return this; //itself
	}

	public IModule getModule(String name, IPythonNature nature, boolean checkSystemManager, boolean dontSearchInit) {
		return getModule(name, nature, dontSearchInit);
	}


	public String resolveModule(String full, boolean checkSystemManager) {
		return super.resolveModule(full);
	}

	public List<String> getCompletePythonPath(String interpreter) {
	    return super.getPythonPath();
   }
    
	public List<String> getCompletePythonPath(String interpreter, IPythonNature nature2) {
        if(interpreter == null){
		    return super.getPythonPath();
        }else{
            IInterpreterManager manager = nature2.getRelatedInterpreterManager();
            IInterpreterInfo info = manager.getInterpreterInfo(interpreter, new NullProgressMonitor());
            return info.getPythonPath();
        }
	}

	public IModule getRelativeModule(String name, IPythonNature nature) {
		return super.getModule(name, nature, true);
	}

    

    protected void onChangePythonpath(String defaultSelectedInterpreter, SortedMap<ModulesKey, ModulesKey> keys) {
        //create the builtin modules
        String[] builtins = getBuiltins(defaultSelectedInterpreter);
        if(builtins != null){
            for (int i = 0; i < builtins.length; i++) {
                String name = builtins[i];
                final ModulesKey k = new ModulesKey(name, null);
                keys.put(k, k);
            }
        }
    }

    /**
     * This is a cache with the name of a builtin pointing to itself (so, it works basically as a set), it's used
     * so that when we find a builtin that does not have a __file__ token we do not try to recreate it again later.
     */
    private LRUCache<String, String> builtinsNotConsidered; 
    
    private LRUCache<String, String> getBuiltinsNotConsidered(){
        if(builtinsNotConsidered == null){
            builtinsNotConsidered = new LRUCache<String, String>(500);
        }
        return builtinsNotConsidered;
    }
    
    /**
     * @return true if there is a token that has rep as its representation.
     */
    private boolean contains(IToken[] tokens, String rep) {
        for (IToken token : tokens) {
            if(token.getRepresentation().equals(rep)){
                return true;
            }
        }
        return false;
    }

    protected AbstractModule getBuiltinModule(String name, IPythonNature nature, boolean dontSearchInit) {
        AbstractModule n = null;
        
        //check for supported builtins these don't have files associated.
        //they are the first to be passed because the user can force a module to be builtin, because there
        //is some information that is only useful when you have builtins, such as os and wxPython (those can
        //be source modules, but they have so much runtime info that it is almost impossible to get useful information
        //from statically analyzing them).
        String[] builtins = getBuiltins();
        if(builtins == null){
            //still on startup
            return null;
        }
        
        //for temporary access (so that we don't generate many instances of it)
        ModulesKey keyForCacheAccess = new ModulesKey(null, null);
        
        boolean foundStartingWithBuiltin = false;
        for (int i = 0; i < builtins.length; i++) {
            String forcedBuiltin = builtins[i];
            if (name.startsWith(forcedBuiltin)) {
                if(name.length() > forcedBuiltin.length() && name.charAt(forcedBuiltin.length()) == '.'){
                    foundStartingWithBuiltin = true;
                    
                    keyForCacheAccess.name = name;
                    n = cache.getObj(keyForCacheAccess, this);
                    
                    if(n == null && dontSearchInit == false){
                        keyForCacheAccess.name = new StringBuffer(name).append(".__init__").toString();
                        n = cache.getObj(keyForCacheAccess, this);
                    }
                    
                    if(n instanceof EmptyModule || n instanceof SourceModule){ //it is actually found as a source module, so, we have to 'coerce' it to a compiled module
                        n = new CompiledModule(name, IToken.TYPE_BUILTIN, nature.getAstManager());
                        doAddSingleModule(new ModulesKey(n.getName(), null), n);
                        return n;
                    }
                }
                
                if(name.equals(forcedBuiltin)){
                    
                    keyForCacheAccess.name = name;
                    n = cache.getObj(keyForCacheAccess, this);
                    
                    if(n == null || n instanceof EmptyModule || n instanceof SourceModule){ //still not created or not defined as compiled module (as it should be)
                        n = new CompiledModule(name, IToken.TYPE_BUILTIN, nature.getAstManager());
                        doAddSingleModule(new ModulesKey(n.getName(), null), n);
                        return n;
                    }
                }
                if(n instanceof CompiledModule){
                    return n;
                }
            }
        }
        if(foundStartingWithBuiltin){
            LRUCache<String,String> notConsidered = getBuiltinsNotConsidered();
            if(notConsidered.getObj(name) != null){
                return null;
            }
            
            //ok, just add it if it is some module that actually exists
            n = new CompiledModule(name, IToken.TYPE_BUILTIN, nature.getAstManager());
            IToken[] globalTokens = n.getGlobalTokens();
            //if it does not contain the __file__, this means that it's not actually a module
            //(but may be a token from a compiled module, so, clients wanting it must get the module
            //first and only then go on to this token).
            //done: a cache with those tokens should be kept, so that we don't actually have to create
            //the module to see its return values (because that's slow)
            if(globalTokens.length > 0 && contains(globalTokens, "__file__")){
                doAddSingleModule(new ModulesKey(name, null), n);
                return n;
            }else{
                notConsidered.add(name, name);
                return null;
            }
        }
        return null;
    }


    /**
     * In the system modules manager, we also have to check for the builtins
     */
    @Override
    public IModule getModule(String name, IPythonNature nature, boolean dontSearchInit) {
        AbstractModule n = getBuiltinModule(name, nature, dontSearchInit);
        if(n != null){
            return n;
        }
        
        return super.getModule(name, nature, dontSearchInit);
    }

    public IModule getModuleWithoutBuiltins(String name, IPythonNature nature, boolean dontSearchInit) {
        return super.getModule(name, nature, dontSearchInit);
    }

}