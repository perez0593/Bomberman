/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import md.games.bomberman.object.GameObject;
import md.games.bomberman.sprites.StaticSprite;

/**
 *
 * @author mpasc
 */
public final class GameDataLoader extends DataInputStream
{
    private final HashMap<String,Constructor<? extends GameObject>> objCache;
    private final HashMap<String,BufferedImage> imgCache;
    private final HashMap<String,StaticSprite> staticSpriteCache;
    
    public GameDataLoader(InputStream in)
    {
        super(in);
        objCache = new HashMap<>();
        imgCache = new HashMap<>();
        staticSpriteCache = new HashMap<>();
    }
    
    public final <GO extends GameObject> GO readGameObject() throws IOException
    {
        String clazz = readUTF();
        Constructor<? extends GameObject> cns = objCache.get(clazz);
        if(cns == null)
        {
            try
            {
                
            }
            catch(Exception ex)
            {
                throw new IOException(ex);
            }
        }
    }
}
