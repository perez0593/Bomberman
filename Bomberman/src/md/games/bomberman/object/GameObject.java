/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.awt.Graphics2D;
import java.util.HashMap;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.util.Conversor;
import nt.dal.build.DALDataBlockBuilder;
import nt.dal.data.DALBlock;
import nt.dal.data.DALData;
import nt.lpl.LPLRuntimeException;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public abstract class GameObject extends LPLObject
{
    private final HashMap<String,LPLValue> localData;
    private final Vector2 position;
    private double theta;
    private BoundingBox boundingBox;
    private String tag;
    
    public GameObject()
    {
        localData = new HashMap<>();
        position = new Vector2();
        theta = 0;
        boundingBox = null;
        tag = "";
    }
    
    public final void setPositionX(double x) { position.x = x; }
    public final void setPositionY(double y) { position.y = y; }
    public final void setPosition(double x, double y) { position.set(x,y); }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    public final Vector2 getPosition() { return position.copy(); }
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    public final void translate(double x, double y) { position.add(x,y); }
    public final void translate(Vector2 p) { position.add(p); }
    
    public final void 
    
    public final void setTag(String tag)
    {
        if(tag == null)
            throw new NullPointerException();
        this.tag = tag;
    }
    public final String getTag() { return tag; }
    
    public final void setLocalValue(String key, LPLValue value)
    {
        if(key == null || value == null)
            throw new NullPointerException();
        checkLPLType(value);
        localData.put(key,value);
    }
    public final void setLocalNull(String key) { setLocalValue(key,NULL); }
    public final void setLocalInt(String key, int value) { setLocalValue(key,valueOf(value)); }
    public final void setLocalLong(String key, long value) { setLocalValue(key,valueOf(value)); }
    public final void setLocalFloat(String key, float value) { setLocalValue(key,valueOf(value)); }
    public final void setLocalDouble(String key, double value) { setLocalValue(key,valueOf(value)); }
    public final void setLocalBoolean(String key, boolean value) { setLocalValue(key,valueOf(value)); }
    public final void setLocalString(String key, String value) { setLocalValue(key,valueOf(value)); }
    
    public final LPLValue getLocalValue(String key)
    {
        if(key == null)
            throw new NullPointerException();
        return localData.get(key);
    }
    public final int getLocalInt(String key) { return getLocalValue(key).toJavaInt(); }
    public final long getLocalLong(String key) { return getLocalValue(key).toJavaLong(); }
    public final float getLocalFloat(String key) { return getLocalValue(key).toJavaFloat(); }
    public final double getLocalDouble(String key) { return getLocalValue(key).toJavaDouble(); }
    public final boolean getLocalBoolean(String key) { return getLocalValue(key).toJavaBoolean(); }
    public final String getLocalString(String key) { return getLocalValue(key).toJavaString(); }
    
    public final boolean isLocalValueNull(String key) { return getLocalValue(key) == NULL; }
    
    public final boolean hasLocalValue(String key)
    {
        if(key == null)
            throw new NullPointerException();
        return localData.containsKey(key);
    }
    
    public final void deleteLocalValue(String key)
    {
        if(key == null)
            throw new NullPointerException();
        localData.remove(key);
    }
    
    public abstract void update(double delta);
    public abstract void draw(Graphics2D g);
    
    /* Input/Output */
    /*public final DALDataBlockBuilder serialize()
    {
        DALDataBlockBuilder base = new DALDataBlockBuilder();
        innserSerialize(base);
        base.assign("object.__class",getGameObjectClass().name());
        base.assign("object.tag",tag);
        base.assign("object.localData",localData);
        return base;
    }
    protected abstract void innserSerialize(DALDataBlockBuilder base);
    
    public final void unserialize(DALBlock base)
    {
        tag = base.getAttribute(DALData.valueOf("object.tag")).toJavaString();
        base.getAttribute(DALData.valueOf("object.localData"))
                .toJavaMap().entrySet().stream().forEach(e -> {
                    localData.put(e.getKey().toJavaString(),Conversor.valueOf(e.getKey()));
        });
        innerUnserialize(base);
    }
    protected abstract void innerUnserialize(DALBlock base);*/
    
    
    /* LPL */
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        String skey = key.toJavaString();
        switch(skey)
        {
            default: return getAttribute(skey);
            case "getLocalValue": return GET_LOCAL_VALUE;
            case "setLocalValue": return SET_LOCAL_VALUE;
            case "hasLocalValue": return HAS_LOCAL_VALUE;
            case "deleteLocalValue": return DELETE_LOCAL_VALUE;
            case "getTag": return GET_TAG;
            case "setTag": return SET_TAG;
        }
    }
    protected abstract LPLValue getAttribute(String key);
    
    private static void checkLPLType(LPLValue value)
    {
        switch(value.getType())
        {
            default: throw new LPLRuntimeException("Expected only number, boolean, string or null");
            case NULL:
            case NUMBER:
            case BOOLEAN:
            case STRING:
                break;
        }
    }
    
    
    
    /* LPL Functions */
    private static final LPLValue GET_LOCAL_VALUE = LPLFunction.createFunction((arg0, arg1, arg2) -> {
        LPLValue value = arg0.<GameObject>toLPLObject().localData.get(arg1.toJavaString());
        return value == null ? arg2 : value;
    });
    private static final LPLValue SET_LOCAL_VALUE = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        checkLPLType(arg2);
        arg0.<GameObject>toLPLObject().localData.put(arg1.toJavaString(),arg2);
    });
    private static final LPLValue HAS_LOCAL_VALUE = LPLFunction.createFunction((arg0, arg1) -> {
        return valueOf(arg0.<GameObject>toLPLObject().localData.containsKey(arg1.toJavaString()));
    });
    private static final LPLValue DELETE_LOCAL_VALUE = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().localData.remove(arg1.toJavaString());
    });
    private static final LPLValue GET_TAG = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().tag);
    });
    private static final LPLValue SET_TAG = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().tag = arg1.toJavaString();
    });
}
