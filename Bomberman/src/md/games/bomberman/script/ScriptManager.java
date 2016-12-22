/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import nt.lpl.LPLClassLoader;
import nt.lpl.LPLCompiler;
import nt.lpl.LPLEnvironment;
import nt.lpl.LPLGlobals;
import nt.lpl.LPLRuntimeException;
import nt.lpl.compiler.LPLCompilerException;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;
import static nt.lpl.types.LPLValue.FALSE;
import static nt.lpl.types.LPLValue.NULL;
import static nt.lpl.types.LPLValue.TRUE;
import static nt.lpl.types.LPLValue.UNDEFINED;

/**
 *
 * @author Asus
 */
public final class ScriptManager implements SerializableObject
{
    private final HashMap<String, Script> scripts;
    private final LPLEnvironment env;
    private LPLObject currentExecutor = Script.OBJECT_INVALID;
    
    public ScriptManager()
    {
        scripts = new HashMap<>();
        env = new LPLEnvironment();
    }
    
    public final Script createScript(String name) throws IllegalArgumentException
    {
        if(scripts.containsKey(name))
            throw new IllegalArgumentException("Script " + name + " already exists");
        Script script = new Script(this,name);
        scripts.put(name,script);
        return script;
    }
    
    public final Script getScript(String name)
    {
        return scripts.get(name);
    }
    
    public final void compileScript(String script) throws LPLCompilerException
    {
        Script s = scripts.get(script);
        if(s == null)
            throw new IllegalArgumentException("Script " + script + " not found");
        s.setCompiled(false);
        try(ByteArrayInputStream bais = new ByteArrayInputStream(s.getCode().getBytes()))
        {
            LPLCompiler.compile(bais,script,LPLGlobals.createGlobals(env));
            s.setCompiled(true);
        }
        catch(IOException ex) {}
    }
    
    public final void compileAll(LPLGlobals globals)
    {
        decorateGlobals(globals);
        LPLClassLoader cl = new LPLClassLoader(getClass().getClassLoader());
        scripts.values().stream().filter(s -> s.isCompiled()).forEach(s -> {
            try(ByteArrayInputStream bais = new ByteArrayInputStream(s.getCode().getBytes()))
            {
                LPLGlobals subGlobals = LPLGlobals.createGlobals(globals);
                LPLFunction closure = LPLCompiler.compile(bais,s.getName(),cl,subGlobals);
                s.setClosure(closure);
            }
            catch(Throwable ex)
            {
                s.setClosure(null);
            }
        });
    }
    
    public final LPLValue executeScript(LPLObject executor, String script)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute();
        }
        return UNDEFINED;
    }
    
    final LPLValue executeScript(LPLObject executor, Script script)
    {
        currentExecutor = executor;
        return script.execute();
    }
    
    final LPLObject getCurrentExecutor() { return currentExecutor; }
    

    @Override
    public final void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeInt(scripts.size());
        for(Map.Entry<String, Script> e : scripts.entrySet())
        {
            gds.writeUTF(e.getKey());
            gds.writeUTF(e.getValue().getCode());
            gds.writeBoolean(e.getValue().isCompiled());
        }
    }

    @Override
    public final void unserialize(GameDataLoader gdl) throws IOException
    {
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
        {
            Script s = new Script(this,gdl.readUTF());
            s.setCode(gdl.readUTF());
            s.setCompiled(gdl.readBoolean());
            scripts.put(s.getName(),s);
        }
    }
    
    private void decorateGlobals(LPLGlobals globals)
    {
        globals.setGlobalValue("ExecuteScript",LPLFunction.createFunction((arg0) -> {
            Script s = getScript(arg0.toJavaString());
            if(s == null)
                throw new LPLRuntimeException("Script " + arg0 + " not found");
            return s.execute();
        }));
        
        HashMap<LPLValue, LPLValue> localData = new HashMap<>();
        
        globals.setGlobalValue("SetGlobalValue",LPLFunction.createVFunction((arg0, arg1) -> {
            if(arg0 == UNDEFINED || arg1 == UNDEFINED)
                throw new LPLRuntimeException("Invalid UNDEFINED value");
            if(arg0 == NULL)
                throw new LPLRuntimeException("Key cannot be null");
            localData.put(arg0,arg1);
        }));
        globals.setGlobalValue("GetGlobalValue",LPLFunction.createFunction((arg0, arg1) -> {
            if(arg0 == UNDEFINED || arg1 == UNDEFINED)
                throw new LPLRuntimeException("Invalid UNDEFINED value");
            LPLValue value = localData.get(arg0);
            return value == null ? arg1 : value;
        }));
        globals.setGlobalValue("HasGlobalValue",LPLFunction.createFunction((arg0) -> {
            if(arg0 == UNDEFINED)
                throw new LPLRuntimeException("Invalid UNDEFINED value");
            return localData.containsKey(arg0) ? TRUE : FALSE;
        }));
        globals.setGlobalValue("DeleteGlobalValue",LPLFunction.createVFunction((arg0) -> {
            if(arg0 == UNDEFINED)
                throw new LPLRuntimeException("Invalid UNDEFINED value");
            localData.remove(arg0);
        }));
    }
}
