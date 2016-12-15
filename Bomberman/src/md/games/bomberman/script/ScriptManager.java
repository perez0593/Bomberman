/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import nt.lpl.LPLCompiler;
import nt.lpl.LPLEnvironment;
import nt.lpl.LPLGlobals;
import nt.lpl.types.LPLObject;

/**
 *
 * @author Asus
 */
public final class ScriptManager implements SerializableObject
{
    private final HashMap<String, Script> scripts;
    private final LPLEnvironment env;
    
    public ScriptManager()
    {
        scripts = new HashMap<>();
        env = new LPLEnvironment();
    }
    
    public final Script createScript(String name) throws IllegalArgumentException
    {
        if(scripts.containsKey(name))
            throw new IllegalArgumentException("Script " + name + " already exists");
        Script script = new Script(name);
        scripts.put(name,script);
        return script;
    }
    
    public final Script getScript(String name)
    {
        return scripts.get(name);
    }
    
    /*public final void compileScript(String script)
    {
        Script s = scripts.get(script);
        if(s == null)
            throw new IllegalArgumentException("Script " + script + " not found");
        s.setCompiled(false);
        LPLCompiler.compile(is, script,LPLGlobals.createGlobals(env))
    }*/
    

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
            Script s = new Script(gdl.readUTF());
            s.setCode(gdl.readUTF());
            s.setCompiled(gdl.readBoolean());
            scripts.put(s.getName(),s);
        }
    }
}
