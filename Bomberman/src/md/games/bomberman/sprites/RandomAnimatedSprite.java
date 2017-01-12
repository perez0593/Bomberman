/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 *
 * @author Asus
 */
public class RandomAnimatedSprite extends AnimatedSprite
{
    private double min, max, current;
    private static final Random RAND = new Random();
    
    public RandomAnimatedSprite(BufferedImage base, int x, int y, int width, int height, int frames)
    {
        super(base,x,y,width,height,frames);
        super.setLoop(false);
        min = 0;
        max = 10;
        current = 0;
    }
    
    public RandomAnimatedSprite(AnimatedSprite other)
    {
        super(other);
        min = 0;
        max = 10;
        current = 0;
    }
    
    public RandomAnimatedSprite(RandomAnimatedSprite other)
    {
        super(other);
        super.setLoop(false);
        min = other.min;
        max = other.max;
        current = other.current;
    }
    
    public final void setRestart(double min, double max)
    {
        this.min = min;
        this.max = max;
    }
    
    @Deprecated
    @Override
    public void setLoop(boolean flag) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    /**
     * Use isSleeping() in this subclass.
     * @return
     * @throws UnsupportedOperationException
     * @deprecated
     */
    @Deprecated
    @Override
    public boolean isEnd() throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException("Not supported yet");
    }
    public final boolean isSleeping()
    {
        return current > 0;
    }
    
    @Override
    public final void update(double delta)
    {
        if(isSleeping())
        {
            current -= delta;
            return;
        }
        super.update(delta);
        if(super.isEnd())
        {
            super.start();
            generateCurrent();
        }
    }
    
    private void generateCurrent()
    {
        current = RAND.nextDouble();
        current = (max - min) * current + min;
    }
}
