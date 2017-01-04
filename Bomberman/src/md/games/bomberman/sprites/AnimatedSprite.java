/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;

/**
 *
 * @author Marc
 */
public class AnimatedSprite
        extends Sprite<AnimatedSprite>
        implements Iterable<BufferedImage>
{
    private final BufferedImage[] imgs;
    private float delay, maxDelay, iterator;
    private float speed;
    private boolean loop, end;
    private final int width, height;
    private String tref = null;
    
    public AnimatedSprite(BufferedImage[] bitmaps)
    {
        imgs = bitmaps;
        width = bitmaps[0].getWidth();
        height = bitmaps[0].getHeight();
        for(BufferedImage bit : bitmaps)
            if(width != bit.getWidth() || height != bit.getHeight())
                throw new IllegalArgumentException();
        delay = -1;
        maxDelay = -1;
        loop = false;
        end = true;
        speed = 1f;
        iterator = 0;
    }
    
    public AnimatedSprite(AnimatedSprite other)
    {
        imgs = other.imgs;
        width = other.width;
        height = other.height;
        delay = other.delay;
        maxDelay = other.maxDelay;
        loop = other.loop;
        end = other.end;
        speed = other.speed;
        iterator = other.iterator;
    }
    
    private AnimatedSprite(AnimatedSprite other, BufferedImage[] imgs)
    {
        this.imgs = imgs;
        width = other.width;
        height = other.height;
        delay = other.delay;
        maxDelay = other.maxDelay;
        loop = other.loop;
        end = other.end;
        speed = other.speed;
        iterator = other.iterator;
    }
    
    @Override
    public AnimatedSprite duplicate()
    {
        AnimatedSprite s = new AnimatedSprite(this);
        s.tref = tref;
        return s;
    }
    
    public boolean isEnd() { return loop ? false : end; }
    
    private boolean canContinue()
    {
        return iterator >= 0d && iterator < imgs.length;
    }
    
    public void start()
    {
        iterator = 0;
        end = false;
        delay = maxDelay;
    }
    
    public void setDelay(float seconds)
    {
        maxDelay = seconds;
    }
    public double getDelay() { return maxDelay; }
    
    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
    
    public void setLoop(boolean flag)
    {
        loop = flag;
    }
    public boolean isLoop() { return loop; }

    @Override
    public int width()
    {
        return width;
    }

    @Override
    public int height()
    {
        return height;
    }
    
    private void reallocIterator()
    {
        if(end || !canContinue())
                iterator = 0f;
    }

    @Override
    public void update(double delta)
    {
        delay -= delta;
        if(delay <= 0d)
        {
            delay = maxDelay;
            iterator += delta * speed;
        }
        if(canContinue()) return;
        if(loop)
        {
            while(!canContinue())
                iterator = (iterator < 0 ? iterator + imgs.length : iterator % ((float)imgs.length));
                //iterator = (float) (iterator < 0 ? imgs.length - (delta * -speed) : delta * speed);
            return;
        }
        end = true;
    }

    @Override
    public void draw(Graphics2D g, AffineTransform transf)
    {
        reallocIterator();
        g.drawImage(imgs[(int)iterator],transf,null);
    }

    public int size()
    {
        return imgs.length;
    }

    public BufferedImage getData(int idx)
    {
        return imgs[idx];
    }
    
    @Override
    public boolean isAnimatedSprite() { return true; }
    
    @Override
    public final boolean isMultiSprite() { return false; }
    
    @Override
    public final boolean isStaticSprite() { return false; }
    
    @Override
    public boolean isAnimation() { return false; }

    @Override
    public Iterator<BufferedImage> iterator()
    {
        return Arrays.asList(imgs).iterator();
    }
    
    @Override
    public final SpriteKind kind() { return SpriteKind.ANIMATED; }

    /*@Override
    public BufferedImage[] allData()
    {
        return Arrays.copyOf(imgs,imgs.length);
    }

    @Override
    public final void setTextureReference(String ref)
    {
        tref = ref;
    }

    @Override
    public final String getTextureReference()
    {
        return tref;
    }*/

    /*public void writeData(GameObjectOutputStream goos) throws IOException
    {
        goos.writeFloat(maxDelay);
        goos.writeFloat(speed);
        goos.writeBoolean(loop);
    }

    public void readData(GameObjectInputStream gois) throws IOException
    {
        maxDelay = gois.readFloat();
        speed = gois.readFloat();
        loop = gois.readBoolean();
    }
    
    public static void skipData(GameObjectInputStream gois) throws IOException
    {
        gois.readFloat();
        gois.readFloat();
        gois.readBoolean();
    }*/
    
}
