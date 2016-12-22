/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.bomb;

import java.util.HashMap;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public enum BombType {
    NORMAL, // Bomba normal
    SPIKES, // Larga distancia
    THROWABLE, // Bomba lanzable
    FASTEXPOLDE, // Bomba de poco tiempo
    C4, // Bomba con control remoto
    HIGHRANGE, // Bomba con rango cuadrado, no cruz
    NINJA, // Bomba invisible Normal
    MINE, // Bomba invisible que solo explota cuando la pisas
    HEAL, // Bomba que te da una vida si te toca
    ICEBOMB, // Bomba que te paraliza si te toca
    TELEPORT; // Bomba que se teletransporta a otro punto del mapa aleatorio y explota
    
    
    private static final BombType[] VALUES = values();
    private static final HashMap<String,BombType> CACHE = new HashMap<>();
    
    static {
        for(BombType bt : VALUES)
        {
            CACHE.put(bt.name(),bt);
            CACHE.put(bt.name().toLowerCase(),bt);
        }
    }
    
    public static final BombType decode(LPLValue value)
    {
        if(value.isNumber())
        {
            int id = value.toJavaInt();
            if(id < 0 || id >= VALUES.length)
                throw new IllegalArgumentException("Invalid BombType Id " + id);
            return VALUES[id];
        }
        BombType bt = CACHE.get(value.toJavaString());
        if(bt == null)
            throw new IllegalArgumentException("Invalid BombType name " + value);
        return bt;
    }
    
    public static final BombType decode(int ordinal) { return VALUES[ordinal]; }
}
