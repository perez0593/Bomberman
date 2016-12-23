/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.action;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
import java.util.function.IntFunction;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.object.GameObject;
import md.games.bomberman.object.bomb.BombType;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.script.Script;

/**
 *
 * @author Asus
 */
public class Action
{
    private ActionId id;
    private final Object[] data;
    
    private Action(ActionId id, Object... data)
    {
        this.id = id;
        this.data = data;
    }
    private Action(ActionId id, int len)
    {
        this.id = id;
        this.data = new Object[len];
    }
    
    public final byte getByte(int index) { return (byte) data[index]; }
    public final short getShort(int index) { return (short) data[index]; }
    public final int getInt(int index) { return (int) data[index]; }
    public final long getLong(int index) { return (long) data[index]; }
    public final float getFloat(int index) { return (float) data[index]; }
    public final double getDouble(int index) { return (double) data[index]; }
    public final boolean getBoolean(int index) { return (boolean) data[index]; }
    public final char getChar(int index) { return (char) data[index]; }
    public final String getString(int index) { return (String) data[index]; }
    public final UUID getUUID(int index) { return (UUID) data[index]; }
    public final Vector2 getVector2(int index) { return (Vector2) data[index]; }
    
    private void writeByte(DataOutputStream dos, int index) throws IOException { dos.writeByte((byte) data[index]); }
    private void writeShort(DataOutputStream dos, int index) throws IOException { dos.writeShort((short) data[index]); }
    private void writeInt(DataOutputStream dos, int index) throws IOException { dos.writeInt((int) data[index]); }
    private void writeLong(DataOutputStream dos, int index) throws IOException { dos.writeLong((long) data[index]); }
    private void writeFloat(DataOutputStream dos, int index) throws IOException { dos.writeFloat((float) data[index]); }
    private void writeDouble(DataOutputStream dos, int index) throws IOException { dos.writeDouble((double) data[index]); }
    private void writeBoolean(DataOutputStream dos, int index) throws IOException { dos.writeBoolean((boolean) data[index]); }
    private void writeChar(DataOutputStream dos, int index) throws IOException { dos.writeChar((char) data[index]); }
    private void writeString(DataOutputStream dos, int index) throws IOException { dos.writeUTF((String) data[index]); }
    private void writeUUID(DataOutputStream dos, int index) throws IOException
    {
        UUID uuid = (UUID) data[index];
        dos.writeLong(uuid.getMostSignificantBits());
        dos.writeLong(uuid.getLeastSignificantBits());
    }
    private void writeVector2(DataOutputStream dos, int index) throws IOException
    {
        Vector2 v = (Vector2) data[index];
        dos.writeDouble(v.x);
        dos.writeDouble(v.y);
    }
    private void writeEnum(DataOutputStream dos, int index) throws IOException { dos.writeInt(((Enum<?>)data[index]).ordinal()); }
    
    private void readByte(DataInputStream dis, int index) throws IOException { data[index] = dis.readByte(); }
    private void readShort(DataInputStream dis, int index) throws IOException { data[index] = dis.readShort(); }
    private void readInt(DataInputStream dis, int index) throws IOException { data[index] = dis.readInt(); }
    private void readLong(DataInputStream dis, int index) throws IOException { data[index] = dis.readLong(); }
    private void readFloat(DataInputStream dis, int index) throws IOException { data[index] = dis.readFloat(); }
    private void readDouble(DataInputStream dis, int index) throws IOException { data[index] = dis.readDouble(); }
    private void readBoolean(DataInputStream dis, int index) throws IOException { data[index] = dis.readBoolean(); }
    private void readChar(DataInputStream dis, int index) throws IOException { data[index] = dis.readChar(); }
    private void readString(DataInputStream dis, int index) throws IOException { data[index] = dis.readUTF(); }
    private void readUUID(DataInputStream dis, int index) throws IOException
    {
        data[index] = new UUID(dis.readLong(),dis.readLong());
    }
    private void readVector2(DataInputStream dis, int index) throws IOException
    {
        data[index] = new Vector2(dis.readDouble(),dis.readDouble());
    }
    private <E extends Enum<E>> void readEnum(DataInputStream dis, int index, IntFunction<E> decoder) throws IOException
    {
        data[index] = decoder.apply(dis.readInt());
    }
    
    public final void write(DataOutputStream dos) throws IOException
    {
        dos.writeInt(id.ordinal());
        switch(id)
        {
            default: throw new IllegalStateException();
            case MOVE_OBJECT:
                writeUUID(dos,0);       //GameObject
                writeVector2(dos,1);    //(dx, dy)
                break;
            case SET_OBJECT_POSITION:
                writeUUID(dos,0);       //GameObject
                writeVector2(dos,1);    //position (x, y)
                break;
            case CREATE_BOMB:
                writeUUID(dos,0);       //Bomb Id
                writeEnum(dos,1);       //Bomb type
                writeInt(dos,2);        //range
                writeInt(dos,3);        //Tile row
                writeInt(dos,4);        //Tile column
                break;
            case EXPLODE_BOMB:
                writeUUID(dos,0);       //Bomb
                break;
            case CREATE_FIRE_EXPLOSION:
                writeBoolean(dos,0);    //Is cross
                writeInt(dos,1);        //Range
                writeInt(dos,2);        //Tile row
                writeInt(dos,3);        //Tile column
                break;
            case COLLECT_COLLECTIBLE:
                writeUUID(dos,0);       //collectible
                writeUUID(dos,1);       //creature
                break;
            case COLLECT_COLLECTIBLE_ON_TILE:
                writeInt(dos,0);        //Tile row
                writeInt(dos,1);        //Tile column
                writeUUID(dos,2);       //creature
                break;
            case COLLIDE_PLACEABLE:
                writeUUID(dos,0);       //placeable
                writeUUID(dos,1);       //creature
                break;
            case DAMAGE_CREATURE:
                writeUUID(dos,0);       //creature
                writeInt(dos,1);        //damage amount
                break;
            case EXECUTE_SCRIPT:
                writeUUID(dos,0);       //GameObject executor
                writeString(dos,1);     //Script name
                break;
        }
    }
    
    public static final Action read(DataInputStream dis) throws IOException
    {
        ActionId aid = ActionId.decode(dis.readInt());
        Action a;
        switch(aid)
        {
            default: throw new IllegalStateException();
            case MOVE_OBJECT:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readVector2(dis,1);
                break;
            case SET_OBJECT_POSITION:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readVector2(dis,1);
                break;
            case CREATE_BOMB:
                a = new Action(aid,4);
                a.readUUID(dis,0);
                a.readEnum(dis,1,BombType::decode);
                a.readInt(dis,2);
                a.readInt(dis,3);
                a.readInt(dis,4);
                break;
            case EXPLODE_BOMB:
                a = new Action(aid,1);
                a.readUUID(dis,0);
                break;
            case CREATE_FIRE_EXPLOSION:
                a = new Action(aid,3);
                a.readByte(dis,0);
                a.readInt(dis,1);
                a.readInt(dis,2);
                a.readInt(dis,3);
                break;
            case COLLECT_COLLECTIBLE:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readUUID(dis,1);
                break;
            case COLLECT_COLLECTIBLE_ON_TILE:
                a = new Action(aid,3);
                a.readInt(dis,0);
                a.readInt(dis,1);
                a.readUUID(dis,2);
                break;
            case COLLIDE_PLACEABLE:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readUUID(dis,1);
                break;
            case DAMAGE_CREATURE:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readInt(dis,1);
                break;
            case EXECUTE_SCRIPT:
                a = new Action(aid,2);
                a.readUUID(dis,0);
                a.readString(dis,1);
                break;
        }
        return a;
    }
    
    
    public static final Action moveObject(UUID id, Vector2 delta)
    {
        return new Action(ActionId.MOVE_OBJECT,id,delta.copy());
    }
    public static final Action moveObject(UUID id, double dx, double dy)
    {
        return new Action(ActionId.MOVE_OBJECT,id,new Vector2(dx,dy));
    }
    
    public static final Action setObjectPosition(UUID id, Vector2 position)
    {
        return new Action(ActionId.SET_OBJECT_POSITION,id,position.copy());
    }
    public static final Action setObjectPosition(UUID id, double x, double y)
    {
        return new Action(ActionId.SET_OBJECT_POSITION,id,new Vector2(x,y));
    }
    
    public static final Action createBomb(BombType type, int range, int row, int column)
    {
        return new Action(ActionId.CREATE_BOMB,UUID.randomUUID(),type,range,row,column);
    }
    public static final Action createBomb(BombType type, int range, Tile tile)
    {
        return new Action(ActionId.CREATE_BOMB,UUID.randomUUID(),type,range,tile.getRow(),tile.getColumn());
    }
    
    public static final Action explodeBomb(UUID bombId)
    {
        return new Action(ActionId.EXPLODE_BOMB,bombId);
    }
    
    public static final Action createFireExplosion(boolean isCross, int range, int row, int column)
    {
        return new Action(ActionId.CREATE_FIRE_EXPLOSION,isCross,range,row,column);
    }
    public static final Action createFireExplosion(boolean isCross, int range, Tile tile)
    {
        return new Action(ActionId.CREATE_FIRE_EXPLOSION,isCross,range,tile.getRow(),tile.getColumn());
    }
    
    public static final Action collectCollectible(UUID collectibleId, UUID creatureId)
    {
        return new Action(ActionId.COLLECT_COLLECTIBLE,collectibleId,creatureId);
    }
    
    public static final Action collectCollectibleOnTile(int row, int column, UUID creatureId)
    {
        return new Action(ActionId.COLLECT_COLLECTIBLE_ON_TILE,row,column,creatureId);
    }
    public static final Action collectCollectibleOnTile(Tile tile, UUID creatureId)
    {
        return new Action(ActionId.COLLECT_COLLECTIBLE_ON_TILE,tile.getRow(),tile.getColumn(),creatureId);
    }
    
    public static final Action collidePlaceable(UUID placeableId, UUID creatureId)
    {
        return new Action(ActionId.COLLIDE_PLACEABLE,placeableId,creatureId);
    }
    
    public static final Action damageCreature(UUID creatureId, int damage)
    {
        return new Action(ActionId.DAMAGE_CREATURE,creatureId,damage);
    }
    
    public static final Action executeScript(UUID executorId, String script)
    {
        return new Action(ActionId.EXECUTE_SCRIPT,executorId,script);
    }
    public static final Action executeScript(UUID executorId, Script script)
    {
        return new Action(ActionId.EXECUTE_SCRIPT,executorId,script.getName());
    }
}
