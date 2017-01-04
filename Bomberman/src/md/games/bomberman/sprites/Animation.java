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
import java.io.InputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import nt.adm.AnimationData;
import nt.adm.AnimationData.AnimationSequence;

/**
 *
 * @author mpasc
 */
public final class Animation
        extends Sprite<Animation>
{
    private final AnimationPart[] parts;
    private final int width;
    private final int height;
    private float speed;
    private int animationId;
    
    public Animation(AnimationData ad, int width, int height) throws IOException
    {
        parts = new AnimationPart[ad.getSequenceCount()];
        for(int i=0;i<parts.length;i++)
            parts[i] = new AnimationPart(ad.getSequence(i));
        this.width = width;
        this.height = height;
        speed = 1f;
        animationId = 0;
    }
    
    private Animation(Animation other)
    {
        parts = new AnimationPart[other.parts.length];
        for(int i=0;i<parts.length;i++)
            parts[i] = new AnimationPart(other.parts[i]);
        width = other.width;
        height = other.height;
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
    public final int width() { return width; }

    @Override
    public final int height() { return height; }

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
        private final BufferedImage[] imgs;
        private final int width, height, frames;
        private float iterator;
        
        private AnimationPart(AnimationSequence as) throws IOException
        {
            width = as.getWidth();
            height = as.getHeight();
            frames = as.getFramesCount();
            BufferedImage base;
            try (InputStream baseInput = as.getInputStream()) {
                base = ImageIO.read(baseInput);
            }
            imgs = SpriteUtils.arrayImages(as.getX(),as.getY(),width,height,frames,base);
        }
        
        private AnimationPart(AnimationPart other)
        {
            imgs = Arrays.copyOf(other.imgs,other.imgs.length);
            width = other.width;
            height = other.height;
            frames = other.frames;
            iterator = 0;
        }
        
        private boolean canContinue()
        {
            return iterator >= 0d && iterator < imgs.length;
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
                iterator = (iterator < 0 ? iterator + imgs.length : iterator % ((float)imgs.length));
        }

        private void draw(Graphics2D g, AffineTransform transf)
        {
            reallocIterator();
            g.drawImage(imgs[(int)iterator],transf,null);
        }
    }
    
}
