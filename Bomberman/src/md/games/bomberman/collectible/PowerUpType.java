/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.collectible;

import java.util.HashMap;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public enum PowerUpType {
    RANGEUP,
    RANGEDOWN,
    BOMBUP,
    BOMBDOWN,
    KEVLAR, // Salva 1 hit de bomba
    SPEEDUP,
    SPEEDDOWN;
    
    
    private static final PowerUpType[] VALUES = values();
    private static final HashMap<String,PowerUpType> CACHE = new HashMap<>();
    
    static {
        for(PowerUpType put : VALUES)
        {
            CACHE.put(put.name(),put);
            CACHE.put(put.name().toLowerCase(),put);
        }
    }
    
    public static final PowerUpType decode(LPLValue value)
    {
        if(value.isNumber())
        {
            int id = value.toJavaInt();
            if(id < 0 || id >= VALUES.length)
                throw new IllegalArgumentException("Invalid BombType Id " + id);
            return VALUES[id];
        }
        PowerUpType put = CACHE.get(value.toJavaString());
        if(put == null)
            throw new IllegalArgumentException("Invalid BombType name " + value);
        return put;
    }
    
    public static final PowerUpType decode(int ordinal) { return VALUES[ordinal]; }
}
