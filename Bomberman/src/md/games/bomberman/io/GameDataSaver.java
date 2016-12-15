/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.sprites.Sprite;
import nt.lpl.types.LPLNumber;
import nt.lpl.types.LPLType;
import nt.lpl.types.LPLValue;
import nt.lpl.types.objects.LPLMap;
import nt.lpl.types.objects.LPLObjectInstance;

/**
 *
 * @author mpasc
 */
public final class GameDataSaver extends DataOutputStream
{
    public GameDataSaver(OutputStream out, int bufferLength)
    {
        super(new BufferedOutputStream(out,bufferLength));
    }
    public GameDataSaver(BufferedOutputStream out)
    {
        super(out);
    }
    public GameDataSaver(OutputStream out)
    {
        super(new BufferedOutputStream(out));
    }
    
    public final void writeIfNonNull(Object ref, Action a) throws IOException
    {
        writeBoolean(ref != null);
        if(ref != null)
            a.write();
    }
    
    public final void writeSerializableObject(SerializableObject so) throws IOException
    {
        String clazz = so.getClass().getName();
        writeUTF(clazz);
        so.serialize(this);
    }
    
    public final void writeVector2(Vector2 v) throws IOException
    {
        writeDouble(v.x);
        writeDouble(v.y);
    }
    
    public final void writeSprite(Sprite s) throws IOException
    {
        writeUTF(s.getSpriteTag());
    }
    
    public final void writeLPL(LPLValue value) throws IOException
    {
        LPLType type = value.getType();
        writeInt(type.ordinal());
        switch(value.getType())
        {
            default: throw new IllegalArgumentException(type + " lpl type cannot write");
            case UNDEFINED: case NULL: break;
            case BOOLEAN:
                writeBoolean(value.toJavaBoolean());
                break;
            case NUMBER: {
                LPLNumber n = (LPLNumber) value;
                if(n.isInteger())
                {
                    writeByte(0);
                    writeInt(n.toJavaInt());
                }
                else if(n.isLong())
                {
                    writeByte(1);
                    writeLong(n.toJavaLong());
                }
                else if(n.isFloat())
                {
                    writeByte(2);
                    writeFloat(n.toJavaFloat());
                }
                else
                {
                    writeByte(3);
                    writeDouble(n.toJavaDouble());
                }
            } break;
            case STRING:
                writeUTF(value.toJavaString());
                break;
            case ARRAY: {
                List<LPLValue> array = value.toJavaList();
                writeInt(array.size());
                for(LPLValue avalue : array)
                    writeLPL(avalue);
            }
            case OBJECT: {
                if(!(value instanceof LPLObjectInstance))
                {
                    if(!(value instanceof LPLMap))
                        throw new IllegalArgumentException("Only \"literal Object\" or \"Map\" instance can write");
                    Map<LPLValue, LPLValue> map = value.toJavaMap();
                    writeByte(1);
                    writeInt(map.size());
                    for(Map.Entry<LPLValue, LPLValue> e : map.entrySet())
                    {
                        writeLPL(e.getKey());
                        writeLPL(e.getValue());
                    }
                    break;
                }
                Set<Map.Entry<LPLValue, LPLValue>> attributes = ((LPLObjectInstance)value).getAttributesEntrySet();
                writeByte(0);
                writeInt(attributes.size());
                for(Map.Entry<LPLValue, LPLValue> e : attributes)
                {
                    writeLPL(e.getKey());
                    writeLPL(e.getValue());
                }
            }
        }
    }
    
    
    @FunctionalInterface
    public interface Action { void write() throws IOException; }
}
