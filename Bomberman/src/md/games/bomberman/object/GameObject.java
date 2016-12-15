/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.DirectionUtils;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import nt.lpl.LPLRuntimeException;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public abstract class GameObject
        extends LPLObject
        implements SerializableObject
{
    private final HashMap<String,LPLValue> localData;
    private final Vector2 position;
    private final Vector2 size;
    private double direction;
    private BoundingBox boundingBox;
    private String tag;
    
    public GameObject()
    {
        localData = new HashMap<>();
        position = new Vector2();
        size = new Vector2();
        direction = 0;
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
    
    public final void setWidth(double width) { size.x = width < 0 ? 0 : width; }
    public final void setHeight(double height) { size.y = height < 0 ? 0 : height; }
    public final void setSize(double width, double height)
    {
        size.x = width < 0 ? 0 : width;
        size.y = height < 0 ? 0 : height;
    }
    public final void setSize(Vector2 size)
    {
        this.size.x = size.x < 0 ? 0 : size.x;
        this.size.y = size.y < 0 ? 0 : size.y;
    }
    public final Vector2 getSize() { return size.copy(); }
    public final double getWidth() { return size.x; }
    public final double getHeight() { return size.y; }
    
    public final double getDirection() { return direction / StrictMath.PI * 180d; }
    public final double getDirectionInRadians() { return direction; }
    public final void setDirection(double degrees) { direction = DirectionUtils.degToRad(degrees); }
    public final void setDirection(int degrees) { direction = DirectionUtils.degToRad(degrees); }
    
    @SuppressWarnings("empty-statement")
    public final void setDirectionInRadians(double radians)
    {
        if(radians < 0)
            while((radians += DirectionUtils.PI2) < 0);
        else if(radians > 0)
            while((radians -= DirectionUtils.PI2) >= DirectionUtils.PI2);
        direction = radians;
    }
    
    public final void rotate(double degrees) { setDirection(getDirection() + degrees); }
    public final void rotateInRadians(double radians) { setDirectionInRadians(direction + radians); }
    
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
    @Override
    public final void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeUTF(tag);
        gds.writeVector2(position);
        gds.writeVector2(size);
        gds.writeDouble(direction);
        gds.writeIfNonNull(boundingBox,() -> gds.writeSerializableObject(boundingBox));
        gds.writeInt(localData.size());
        for(Map.Entry<String, LPLValue> e : localData.entrySet())
        {
            gds.writeUTF(e.getKey());
            gds.writeLPL(e.getValue());
        }
        innserSerialize(gds);
    }
    protected abstract void innserSerialize(GameDataSaver gds);
    
    @Override
    public final void unserialize(GameDataLoader gdl) throws IOException
    {
        tag = gdl.readUTF();
        position.set(gdl.readVector2());
        size.set(gdl.readVector2());
        direction = gdl.readDouble();
        gdl.readIfNonNull(() -> boundingBox = gdl.readSerializableObject());
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
            localData.put(gdl.readUTF(),gdl.readLPL());
        innerUnserialize(gdl);
    }
    protected abstract void innerUnserialize(GameDataLoader gdl);
    
    
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
            case "setPositionX": return SET_POSITION_X;
            case "setPositionY": return SET_POSITION_Y;
            case "setPosition": return SET_POSITION;
            case "getPosition": return GET_POSITION;
            case "getPositionX": return GET_POSITION_X;
            case "getPositionY": return GET_POSITION_Y;
            case "setWidth": return SET_WIDTH;
            case "setHeight": return SET_HEIGHT;
            case "setSize": return SET_SIZE;
            case "getSize": return GET_SIZE;
            case "getWidth": return GET_WIDTH;
            case "getHeight": return GET_HEIGHT;
            case "setDirection": return SET_DIRECTION;
            case "getDirection": return GET_DIRECTION;
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
    private static final LPLValue SET_POSITION_X = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().position.x = arg1.toJavaDouble();
    });
    private static final LPLValue SET_POSITION_Y = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().position.y = arg1.toJavaDouble();
    });
    private static final LPLValue SET_POSITION = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        if(arg2 == UNDEFINED)
            arg0.<GameObject>toLPLObject().setPosition(arg1.toLPLObject());
        else arg0.<GameObject>toLPLObject().setPosition(arg1.toJavaDouble(),arg2.toJavaDouble());
    });
    private static final LPLValue GET_POSITION = LPLFunction.createFunction((arg0) -> {
        return arg0.<GameObject>toLPLObject().position.copy();
    });
    private static final LPLValue GET_POSITION_X = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().position.x);
    });
    private static final LPLValue GET_POSITION_Y = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().position.y);
    });
    private static final LPLValue SET_WIDTH = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().size.x = arg1.toJavaDouble();
    });
    private static final LPLValue SET_HEIGHT = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().size.y = arg1.toJavaDouble();
    });
    private static final LPLValue SET_SIZE = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        if(arg2 == UNDEFINED)
            arg0.<GameObject>toLPLObject().setSize(arg1.toLPLObject());
        else arg0.<GameObject>toLPLObject().setSize(arg1.toJavaDouble(),arg2.toJavaDouble());
    });
    private static final LPLValue GET_SIZE = LPLFunction.createFunction((arg0) -> {
        return arg0.<GameObject>toLPLObject().size.copy();
    });
    private static final LPLValue GET_WIDTH = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().size.x);
    });
    private static final LPLValue GET_HEIGHT = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().size.y);
    });
    private static final LPLValue SET_DIRECTION = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().setDirection(arg1.toJavaDouble());
    });
    private static final LPLValue GET_DIRECTION = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().getDirection());
    });
}
