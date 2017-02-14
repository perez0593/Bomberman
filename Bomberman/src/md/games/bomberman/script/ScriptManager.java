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
import md.games.bomberman.scenario.GameObject;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.event.LPLScenarioEventBuilder;
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
import nt.lpl.types.LPLVarargs;
import nt.lpl.types.objects.LPLObjectInstance;

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
    
    public final void compileAll(Scenario scenario, LPLGlobals globals)
    {
        ScriptsObject compiledScripts = new ScriptsObject();
        decorateGlobals(globals,scenario,compiledScripts);
        LPLClassLoader cl = new LPLClassLoader(getClass().getClassLoader());
        scripts.values().stream().filter(s -> !s.isCompiled()).forEach(s -> {
            StringBuilder sb = new StringBuilder();
            
            sb.append("Scripts[\"").append(s.getName()).append("\"] = function()\n\n");
            sb.append(s.getCode().replace("\n","\n\t")).append("\nend\n");
            try(ByteArrayInputStream bais = new ByteArrayInputStream(sb.toString().getBytes()))
            {
                LPLValue hashGlobals = new LPLObjectInstance();
                LPLGlobals subGlobals = LPLGlobals.wrap(globals,hashGlobals);
                LPLFunction closure = LPLCompiler.compile(bais,s.getName(),cl,subGlobals);
                compiledScripts.lastGlobals = hashGlobals;
                closure.call();
                s.setCompiledCode(compiledScripts.scripts.get(s.getName()).closure,subGlobals);
                s.setCompiled(true);
            }
            catch(Throwable ex)
            {
                System.err.println("Script error: " + s.getName());
                ex.printStackTrace(System.err);
                s.setCompiledCode(null,null);
            }
        });
        scripts.values().stream().filter(s -> s.isCompiled()).forEach(s -> {
            s.initiateCompiledCode();
        });
        compiledScripts.canput = false;
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
    public final LPLValue executeScript(LPLObject executor, String script, LPLValue arg0)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute(arg0);
        }
        return UNDEFINED;
    }
    public final LPLValue executeScript(LPLObject executor, String script, LPLValue arg0, LPLValue arg1)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute(arg0,arg1);
        }
        return UNDEFINED;
    }
    public final LPLValue executeScript(LPLObject executor, String script, LPLValue arg0, LPLValue arg1, LPLValue arg2)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute(arg0,arg1,arg2);
        }
        return UNDEFINED;
    }
    public final LPLValue executeScript(LPLObject executor, String script, LPLValue arg0, LPLValue arg1, LPLValue arg2, LPLValue arg3)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute(arg0,arg1,arg2,arg3);
        }
        return UNDEFINED;
    }
    public final LPLValue executeScript(LPLObject executor, String script, LPLValue... args)
    {
        Script s = scripts.get(script);
        if(s != null)
        {
            currentExecutor = executor;
            return s.execute(args);
        }
        return UNDEFINED;
    }
    
    final LPLValue executeScript(LPLObject executor, Script script)
    {
        currentExecutor = executor;
        return script.execute();
    }
    final LPLValue executeScript(LPLObject executor, Script script, LPLValue arg0)
    {
        currentExecutor = executor;
        return script.execute(arg0);
    }
    final LPLValue executeScript(LPLObject executor, Script script, LPLValue arg0, LPLValue arg1)
    {
        currentExecutor = executor;
        return script.execute(arg0,arg1);
    }
    final LPLValue executeScript(LPLObject executor, Script script, LPLValue arg0, LPLValue arg1, LPLValue arg2)
    {
        currentExecutor = executor;
        return script.execute(arg0,arg1,arg2);
    }
    final LPLValue executeScript(LPLObject executor, Script script, LPLValue arg0, LPLValue arg1, LPLValue arg2, LPLValue arg3)
    {
        currentExecutor = executor;
        return script.execute(arg0,arg1,arg2,arg3);
    }
    final LPLValue executeScript(LPLObject executor, Script script, LPLValue[] args)
    {
        currentExecutor = executor;
        return script.execute(args);
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
    
    private void decorateGlobals(LPLGlobals globals, Scenario scenario, ScriptsObject scripts)
    {
        /*globals.setGlobalValue("ExecuteScript",LPLFunction.createFunction((arg0) -> {
            Script s = getScript(arg0.toJavaString());
            if(s == null)
                throw new LPLRuntimeException("Script " + arg0 + " not found");
            return s.execute();
        }));*/
        globals.setGlobalValue("Scripts",scripts);
        globals.setGlobalValue("Scenario",scenario);
        globals.setGlobalValue("Event",new LPLScenarioEventBuilder(scenario));
        
        globals.setGlobalValue("GetObjectSelf",LPLFunction.createFunction(() -> currentExecutor));
        
        globals.setGlobalValue("ImportScript",LPLFunction.createFunction((arg0) -> {
            CompiledScript cs = scripts.scripts.get(arg0.toJavaString());
            Script s = ScriptManager.this.scripts.get(arg0.toJavaString());
            if(cs == null || s == null)
                return UNDEFINED;
            s.initiateCompiledCode();
            return cs.data;
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
        
        
        globals.setGlobalValue("AssignScriptToObject",LPLFunction.createVFunction((arg0, arg1, arg2) -> {
            GameObject go = arg0.toLPLObject();
            ScriptId id = ScriptId.decode(arg1);
            if(id == null)
                throw new LPLRuntimeException("Invalid ScriptId: " + arg1);
            Script script = getScript(arg2.toJavaString());
            if(script == null)
                throw new LPLRuntimeException("Script \"" + arg2 + "\" not found");
            go.setScript(id,script);
        }));
        globals.setGlobalValue("GetScriptFromObject",LPLFunction.createFunction((arg0, arg1) -> {
            GameObject go = arg0.toLPLObject();
            ScriptId id = ScriptId.decode(arg1);
            if(id == null)
                throw new LPLRuntimeException("Invalid ScriptId: " + arg1);
            Script script = go.getScript(id);
            return script == null ? NULL : LPLValue.valueOf(script.getName());
        }));
        globals.setGlobalValue("UnassignScriptFromObject",LPLFunction.createVFunction((arg0, arg1) -> {
            GameObject go = arg0.toLPLObject();
            ScriptId id = ScriptId.decode(arg1);
            if(id == null)
                throw new LPLRuntimeException("Invalid ScriptId: " + arg1);
            go.removeScript(id);
        }));
    }
    
    
    private static final class ScriptsObject extends LPLObject
    {
        private final HashMap<String, CompiledScript> scripts = new HashMap<>();
        private LPLValue lastGlobals = null;
        private boolean canput = true;
        
        @Override
        public final LPLValue getAttribute(LPLValue key)
        {
            CompiledScript cs = scripts.get(key.toJavaString());
            return cs == null ? LPLValue.UNDEFINED : cs;
        }
        
        @Override
        public final LPLValue setAttribute(LPLValue key, LPLValue value)
        {
            if(!canput)
                throw new LPLRuntimeException("Cannot set any script in runtime");
            String name = key.toJavaString();
            CompiledScript cs;
            scripts.put(key.toJavaString(),cs = new CompiledScript(name,(LPLFunction)value,lastGlobals));
            lastGlobals = null;
            return cs;
        }
        
        @Override
        public final LPLValue length() { return valueOf(scripts.size()); }
        
        @Override
        public final LPLValue in(LPLValue key) { return valueOf(scripts.containsKey(key.toJavaString())); }
    }
    
    
    private static final class CompiledScript extends LPLObject
    {
        private final LPLFunction closure;
        private final String name;
        private final LPLValue data;
        
        private CompiledScript(String name, LPLFunction closure, LPLValue data)
        {
            this.closure = closure;
            this.name = name;
            this.data = data;
        }
        
        @Override
        public final LPLVarargs call() {
            return closure.call(); }
        
        @Override
        public final LPLVarargs call(LPLValue arg0) { return closure.call(arg0); }
        
        @Override
        public final LPLVarargs call(LPLValue arg0, LPLValue arg1) { return closure.call(arg0,arg1); }
        
        @Override
        public final LPLVarargs call(LPLValue arg0, LPLValue arg1, LPLValue arg2) { return closure.call(arg0,arg1,arg2); }
        
        @Override
        public final LPLVarargs call(LPLVarargs args) { return closure.call(args); }
        
        @Override
        public final LPLValue getAttribute(LPLValue key) { return data.getAttribute(key); }
    }
}
