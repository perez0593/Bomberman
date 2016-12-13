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
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.sprites.Sprite;

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
}
