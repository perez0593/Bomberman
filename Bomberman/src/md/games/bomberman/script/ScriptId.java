/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.script;

import java.util.HashMap;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public enum ScriptId
{
    ON_CREATE,
    ON_DESTROY,
    ON_DIE,
    ON_DAMAGE,
    ON_HEAL,
    ON_COLLIDE,
    ON_COLLECT,
    ON_EXPLODE;
    
    private static final ScriptId[] VALUES = values();
    
    private static final HashMap<String, ScriptId> CACHE = new HashMap<>();
    
    static {
        for(ScriptId sid : VALUES)
            CACHE.put(sid.name(),sid);
    }
    
    public static final ScriptId decode(LPLValue value)
    {
        return CACHE.get(value.toJavaString());
    }
    
    public static final ScriptId decode(String name)
    {
        return CACHE.get(name);
    }
    
    public final LPLValue asLPLValue() { return LPLValue.valueOf(name()); }
}
