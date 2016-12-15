/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.scenario.ScenarioTheme;
import md.games.bomberman.script.Script;
import md.games.bomberman.script.ScriptManager;
import md.games.bomberman.sprites.Sprite;
import nt.lpl.types.LPLType;
import nt.lpl.types.LPLValue;
import nt.lpl.types.objects.LPLMap;
import nt.lpl.types.objects.LPLObjectInstance;

/**
 *
 * @author mpasc
 */
public final class GameDataLoader extends DataInputStream
{
    private static final LPLType[] LPL_TYPES = LPLType.values();
    private final HashMap<String,Constructor<? extends SerializableObject>> objCache;
    private final ScenarioTheme theme;
    private ScriptManager scripts;
    private final ClassLoader classLoader;
    
    public GameDataLoader(BufferedInputStream in, ScenarioTheme theme)
    {
        super(in);
        if(theme == null)
            throw new NullPointerException();
        objCache = new HashMap<>();
        this.theme = theme;
        classLoader = GameDataLoader.class.getClassLoader();
    }
    public GameDataLoader(InputStream in, int bufferLength, ScenarioTheme theme)
    {
        this(new BufferedInputStream(in,bufferLength),theme);
    }
    public GameDataLoader(InputStream in, ScenarioTheme theme)
    {
        this(new BufferedInputStream(in),theme);
    }
    
    public final void readIfNonNull(Action a) throws IOException
    {
        if(readBoolean())
            a.read();
    }
    
    public final <SO extends SerializableObject> SO readSerializableObject() throws IOException
    {
        String clazz = readUTF();
        Constructor<? extends SerializableObject> cns = objCache.get(clazz);
        if(cns == null)
        {
            try
            {
                Class<? extends SerializableObject> cl =
                        (Class<? extends SerializableObject>) classLoader.loadClass(clazz);
                cns = cl.getConstructor();
                cns.setAccessible(true);
                objCache.put(clazz,cns);
            }
            catch(ClassNotFoundException | NoSuchMethodException | SecurityException ex)
            {
                throw new IOException(ex);
            }
        }
        try
        {
            SerializableObject so = cns.newInstance();
            so.unserialize(this);
            return (SO) so;
        }
        catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
        {
            throw new IOException(ex);
        }
    }
    
    public final Vector2 readVector2() throws IOException
    {
        return new Vector2(readDouble(),readDouble());
    }
    
    public final Sprite readSprite() throws IOException
    {
        String tag = readUTF();
        return theme.getSprite(tag);
    }
    
    public final LPLValue readLPL() throws IOException
    {
        LPLType type = LPL_TYPES[readInt()];
        switch(type)
        {
            case UNDEFINED: return LPLValue.UNDEFINED;
            case NULL: return LPLValue.NULL;
            case BOOLEAN: return LPLValue.valueOf(readBoolean());
            case NUMBER: {
                switch(readInt())
                {
                    case 0: return LPLValue.valueOf(readInt());
                    case 1: return LPLValue.valueOf(readLong());
                    case 2: return LPLValue.valueOf(readFloat());
                    default: return LPLValue.valueOf(readDouble());
                }
            }
            case STRING: return LPLValue.valueOf(readUTF());
            case ARRAY: {
                LPLValue[] array = new LPLValue[readInt()];
                for(int i=0;i<array.length;i++)
                    array[i] = readLPL();
                return LPLValue.valueOf(array);
            }
            case OBJECT: {
                byte otype = readByte();
                int len = readInt();
                HashMap<LPLValue, LPLValue> map = new HashMap<>();
                for(int i=0;i<len;i++)
                    map.put(readLPL(),readLPL());
                return otype != 0 ? new LPLMap(map) : new LPLObjectInstance(map);
            }
            default: throw new IOException("Invalid LPL type");
        }
    }
    
    public final Script readScript() throws IOException
    {
        if(scripts == null)
            throw new IllegalStateException("Scripts manager not found");
        return scripts.getScript(readUTF());
    }
    
    
    @FunctionalInterface
    public interface Action { void read() throws IOException; }
}
