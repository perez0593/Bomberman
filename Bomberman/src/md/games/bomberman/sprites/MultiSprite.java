/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author u75213
 */
public final class MultiSprite extends Sprite<MultiSprite>
{
    private final BufferedImage[] imgs;
    private final int width, height;
    private int iterator;
    
    public MultiSprite(BufferedImage[] bitmaps)
    {
        imgs = bitmaps;
        width = bitmaps[0].getWidth();
        height = bitmaps[0].getHeight();
        for(BufferedImage bit : bitmaps)
            if(width != bit.getWidth() || height != bit.getHeight())
                throw new IllegalArgumentException();
        iterator = 0;
    }
    public MultiSprite(MultiSprite other)
    {
        imgs = other.imgs;
        width = other.width;
        height = other.height;
        iterator = 0;
    }

    @Override
    public final int width() { return width; }

    @Override
    public final int height() { return height; }

    @Override
    public final void update(double delta) {}

    @Override
    public final void draw(Graphics2D g, AffineTransform transf)
    {
        g.drawImage(imgs[iterator],transf,null);
    }

    @Override
    public final boolean isAnimatedSprite() { return false; }
    
    @Override
    public final boolean isMultiSprite() { return true; }
    
    @Override
    public final boolean isStaticSprite() { return false; }

    @Override
    public final Sprite duplicate()
    {
        return new MultiSprite(this);
    }
    
    @Override
    public final SpriteKind kind() { return SpriteKind.MULTI; }
}
