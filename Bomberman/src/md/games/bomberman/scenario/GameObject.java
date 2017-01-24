/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.DirectionUtils;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.util.Utils;
import md.games.bomberman.util.Utils.SweptInfo;
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
    private UUID uid = null;
    private final HashMap<String,LPLValue> localData;
    private final Vector2 position;
    private final Vector2 size;
    private double direction;
    private BoundingBox boundingBox;
    private String tag;
    private Scenario scenario;
    private boolean destroid;
    
    public GameObject()
    {
        localData = new HashMap<>();
        position = new Vector2();
        size = new Vector2();
        direction = 0;
        boundingBox = null;
        tag = "";
        scenario = null;
    }
    
    /**
     * WARNING: Este metodo no debe ser usado nunca.
     * Esta pensado unicamente para la transferencia de objetos en el modo online.
     * @param id 
     */
    public final void setId(UUID id) { uid = id; }
    public final UUID getId() { return uid; }
    
    public final void setScenarioReference(Scenario scenario)
    {
        if(uid == null)
            uid = UUID.randomUUID();
        this.scenario = scenario;
    }
    public final Scenario getScenarioReference() { return scenario; }
    public final boolean hasScenarioReference() { return scenario != null; }
    
    public final void destroy()
    {
        if(!destroid)
        {
            destroid = true;
            innerDestroy();
            localData.clear();
            destroyBoundingBox();
        }
    }
    protected abstract void innerDestroy();
    public final boolean isDestroid() { return destroid; }
    
    public boolean isCreature() { return false; }
    public boolean isCollectible() { return false; }
    public boolean isPlaceable() { return false; }
    public abstract int getGameObjectType();
    
    public final void setPositionX(double x)
    {
        position.x = x;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setPositionY(double y)
    {
        position.y = y;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setPosition(double x, double y)
    {
        position.set(x,y);
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setPosition(Vector2 position)
    {
        this.position.set(position);
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final Vector2 getPosition() { return position.copy(); }
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    public final void translate(double x, double y)
    {
        position.add(x,y);
        if(boundingBox != null)
            boundingBox.translate(x,y);
    }
    public final void translate(Vector2 p)
    {
        position.add(p);
        if(boundingBox != null)
            boundingBox.translate(position);
    }
    
    public final void setSizeWidth(double width)
    {
        size.x = width < 0 ? 0 : width;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setSizeHeight(double height)
    {
        size.y = height < 0 ? 0 : height;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setSize(double width, double height)
    {
        size.x = width < 0 ? 0 : width;
        size.y = height < 0 ? 0 : height;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final void setSize(Vector2 size)
    {
        this.size.x = size.x < 0 ? 0 : size.x;
        this.size.y = size.y < 0 ? 0 : size.y;
        if(boundingBox != null)
            boundingBox.resituate(position,size);
    }
    public final Vector2 getSize() { return size.copy(); }
    public final double getSizeWidth() { return size.x; }
    public final double getSizeHeight() { return size.y; }
    
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
    
    public final void createBoundingBox() { boundingBox = BoundingBox.situate(position,size); }
    public final void destroyBoundingBox() { boundingBox = null; }
    public final boolean hasBoundingBox() { return boundingBox != null; }
    protected final BoundingBox getBoundingBox() { return boundingBox; }
    
    public final boolean hasCollision(BoundingBox box)
    {
        if(boundingBox == null)
            return false;
        return boundingBox.hasCollision(box);
    }
    
    public final boolean hasCollision(GameObject other)
    {
        return other.boundingBox != null && boundingBox != null &&
                boundingBox.hasCollision(other.boundingBox);
    }
    
    public final SweptInfo computeSwept(Vector2 selfSpeed, GameObject other)
    {
        if(other.boundingBox == null || boundingBox == null)
            return SweptInfo.EMPTY;
        return Utils.sweptBoundingBox(boundingBox,other.boundingBox,selfSpeed);
    }
    
    public boolean canSee(Camera camera)
    {
        if(boundingBox == null)
            return camera.contains(
                    position.x - size.x / 2,
                    position.y - size.y / 2,
                    size.x,size.y);
        else return camera.getBounds().hasCollision(boundingBox);
    }
    
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
        if(uid == null)
            throw new IllegalStateException();
        gds.writeUUID(uid);
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
        innerSerialize(gds);
    }
    protected abstract void innerSerialize(GameDataSaver gds) throws IOException;
    
    @Override
    public final void unserialize(GameDataLoader gdl) throws IOException
    {
        uid = gdl.readUUID();
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
    protected abstract void innerUnserialize(GameDataLoader gdl) throws IOException;
    
    
    @Override
    public final boolean equals(Object o)
    {
        return o instanceof GameObject &&
                uid.equals(((GameObject)o).uid);
    }

    @Override
    public final int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.uid);
        return hash;
    }
    
    
    /* LPL */
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        String skey = key.toJavaString();
        switch(skey)
        {
            default: {
                LPLValue value = getAttribute(skey);
                return value == null ? UNDEFINED : value;
            }
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
            case "setSizeWidth": return SET_SIZE_WIDTH;
            case "setSizeHeight": return SET_SIZE_HEIGHT;
            case "setSize": return SET_SIZE;
            case "getSize": return GET_SIZE;
            case "getSizeWidth": return GET_SIZE_WIDTH;
            case "getSizeHeight": return GET_SIZE_HEIGHT;
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
            case ARRAY:
            case OBJECT:
                break;
        }
    }
    
    
    public static final int GAME_OBJECT_TYPE_CREATURE = 0;
    public static final int GAME_OBJECT_TYPE_COLLECTIBLE = 1;
    public static final int GAME_OBJECT_TYPE_PLACEABLE = 2;
    
    
    
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
        arg0.<GameObject>toLPLObject().setPositionX(arg1.toJavaDouble());
    });
    private static final LPLValue SET_POSITION_Y = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().setPositionY(arg1.toJavaDouble());
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
    private static final LPLValue SET_SIZE_WIDTH = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().setSizeWidth(arg1.toJavaDouble());
    });
    private static final LPLValue SET_SIZE_HEIGHT = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().setSizeHeight(arg1.toJavaDouble());
    });
    private static final LPLValue SET_SIZE = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        if(arg2 == UNDEFINED)
            arg0.<GameObject>toLPLObject().setSize(arg1.toLPLObject());
        else arg0.<GameObject>toLPLObject().setSize(arg1.toJavaDouble(),arg2.toJavaDouble());
    });
    private static final LPLValue GET_SIZE = LPLFunction.createFunction((arg0) -> {
        return arg0.<GameObject>toLPLObject().size.copy();
    });
    private static final LPLValue GET_SIZE_WIDTH = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().size.x);
    });
    private static final LPLValue GET_SIZE_HEIGHT = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().size.y);
    });
    private static final LPLValue SET_DIRECTION = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<GameObject>toLPLObject().setDirection(arg1.toJavaDouble());
    });
    private static final LPLValue GET_DIRECTION = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<GameObject>toLPLObject().getDirection());
    });
}