/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.placeable;

import java.util.HashMap;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public enum RockType
{
    UNBREAKABLE,
    BREAKABLE_WEAK,
    BREAKABLE_HARD;
    
    private static final RockType[] VALUES = values();
    private static final HashMap<String,RockType> CACHE = new HashMap<>();
    
    static {
        for(RockType bt : VALUES)
        {
            CACHE.put(bt.name(),bt);
            CACHE.put(bt.name().toLowerCase(),bt);
        }
    }
    
    public static final RockType decode(LPLValue value)
    {
        if(value.isNumber())
        {
            int id = value.toJavaInt();
            if(id < 0 || id >= VALUES.length)
                throw new IllegalArgumentException("Invalid BombType Id " + id);
            return VALUES[id];
        }
        RockType rt = CACHE.get(value.toJavaString());
        if(rt == null)
            throw new IllegalArgumentException("Invalid BombType name " + value);
        return rt;
    }
    
    public static final RockType decode(int ordinal) { return VALUES[ordinal]; }
}
