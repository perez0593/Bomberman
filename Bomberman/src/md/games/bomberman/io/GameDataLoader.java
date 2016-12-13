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
import md.games.bomberman.sprites.Sprite;

/**
 *
 * @author mpasc
 */
public final class GameDataLoader extends DataInputStream
{
    private final HashMap<String,Constructor<? extends SerializableObject>> objCache;
    private final ScenarioTheme theme;
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
}
