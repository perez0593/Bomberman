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
 * @author Asus
 */
public enum CollectibleId
{
    ;
    
    private final String lowerName = name().toLowerCase();
    private static final HashMap<String, CollectibleId> HASH_ID = new HashMap<>();
    private static final CollectibleId[] VALUES = values();
    
    static {
        for(CollectibleId cid : VALUES)
        {
            HASH_ID.put(cid.name(),cid);
            HASH_ID.put(cid.lowerName,cid);
        }
    }
    
    public static final CollectibleId decode(int id)
    {
        return id < 0 || id > VALUES.length ? null : VALUES[id];
    }
    public static final CollectibleId decode(String name) { return HASH_ID.get(name); }
    public static final CollectibleId decode(LPLValue value)
    {
        if(value.isNumber())
            return decode(value.toJavaInt());
        return decode(value.toJavaString());
    }
    
    public final int asInt() { return ordinal(); }
    public final String asString() { return lowerName; }
    public final LPLValue asLPLNumber() { return LPLValue.valueOf(asInt()); }
    public final LPLValue asLPLString() { return LPLValue.valueOf(asString()); }
}
