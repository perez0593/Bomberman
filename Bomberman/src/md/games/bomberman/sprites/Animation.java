/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import md.games.bomberman.util.RawAnimationLoader.RawAnimationData;
import md.games.bomberman.util.RawAnimationLoader.RawAnimationData.RawAnimationSequence;
import nt.adm.AnimationData.AnimationSequence;

/**
 *
 * @author mpasc
 */
public final class Animation
        extends Sprite<Animation>
{
    private final AnimationPart[] parts;
    private final BufferedImage base;
    private float speed;
    private int animationId;
    
    /*public Animation(AnimationData ad) throws IOException
    {
        parts = new AnimationPart[ad.getSequenceCount()];
        for(int i=0;i<parts.length;i++)
            parts[i] = new AnimationPart(ad.getSequence(i));
        base = ad.get
        speed = 1f;
        animationId = 0;
    }*/
    
    public Animation(RawAnimationData rad) throws IOException
    {
        parts = new AnimationPart[rad.getSequenceCount()];
        int count = 0;
        for(RawAnimationSequence ras : rad)
            parts[count++] = new AnimationPart(ras);
        base = rad.getBase();
        speed = 1f;
        animationId = 0;
    }
    
    private Animation(Animation other)
    {
        parts = new AnimationPart[other.parts.length];
        for(int i=0;i<parts.length;i++)
            parts[i] = new AnimationPart(other.parts[i]);
        base = other.base;
        speed = other.speed;
        animationId = 0;
    }
    
    public final int getAnimationSequenceCount() { return parts.length; }
    
    public final void setAnimationSequence(int id)
    {
        if(id == animationId)
            return;
        parts[animationId].reset();
        animationId = id;
    }
    public final int getCurrentAnimationSequenceId() { return animationId; }
    
    public final boolean isCurrentAnimationSequenceEnds() { return !parts[animationId].canContinue(); }
    
    public final void setSpeed(float speed) { this.speed = speed; }
    public final float getSpeed() { return speed; }
    
    @Override
    public final int width() { return parts[animationId].width; }

    @Override
    public final int height() { return parts[animationId].height; }

    @Override
    public void update(double delta)
    {
        parts[animationId].update(delta);
    }

    @Override
    public void draw(Graphics2D g, AffineTransform transf)
    {
        parts[animationId].draw(g,transf);
    }

    @Override
    public boolean isAnimatedSprite() { return false; }

    @Override
    public boolean isMultiSprite() { return false; }

    @Override
    public boolean isStaticSprite() { return false; }
    
    @Override
    public boolean isAnimation() { return true; }

    @Override
    public Sprite duplicate() { return new Animation(this); }

    @Override
    public final SpriteKind kind() { return SpriteKind.ANIMATION; }
    
    private final class AnimationPart
    {
        private final int x, y;
        private final int width, height, frames;
        private float iterator;
        
        private AnimationPart(AnimationSequence as) throws IOException
        {
            x = as.getX();
            y = as.getY();
            width = as.getWidth();
            height = as.getHeight();
            frames = as.getFramesCount();
        }
        
        private AnimationPart(RawAnimationSequence ras) throws IOException
        {
            x = ras.getX();
            y = ras.getY();
            width = ras.getWidth();
            height = ras.getHeight();
            frames = ras.getFramesCount();
        }
        
        private AnimationPart(AnimationPart other)
        {
            x = other.x;
            y = other.y;
            width = other.width;
            height = other.height;
            frames = other.frames;
            iterator = 0;
        }
        
        private boolean canContinue()
        {
            return iterator >= 0d && iterator < frames;
        }

        private void reset()
        {
            iterator = 0;
        }

        private void reallocIterator()
        {
            if(!canContinue())
                iterator = 0f;
        }

        private void update(double delta)
        {
            iterator += delta * speed;
            if(canContinue()) return;
            while(!canContinue())
                iterator = (iterator < 0 ? iterator + frames : iterator % ((float)frames));
        }

        private void draw(Graphics2D g, AffineTransform transf)
        {
            reallocIterator();
            AffineTransform aold = g.getTransform();
            g.transform(transf);
            int dx = x + ((int)iterator * width);
            g.drawImage(base,0,0,width,height,dx,y,dx + width,y + height,null);
            g.setTransform(aold);
        }
    }
    
}
