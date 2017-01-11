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
 * @author Asus
 */
public class SubRegionStaticSprite extends StaticSprite
{
    private final int x0, x1, y0, y1;
    private final int width, height;
    
    public SubRegionStaticSprite(BufferedImage base, int x0, int y0, int x1, int y1)
    {
        super(base);
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.width = x1 - x0;
        this.height = y1 - y0;
    }
    
    public static final SubRegionStaticSprite fromSize(BufferedImage base, int x, int y, int width, int height)
    {
        return new SubRegionStaticSprite(base,x,y,x+width,y+height);
    }
    
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

    @Override
    public void draw(Graphics2D g, AffineTransform transf)
    {
        AffineTransform aold = g.getTransform();
        g.transform(transf);
        g.drawImage(img,0,0,width,height,x0,y0,x1,y1,null);
        g.setTransform(aold);
    }
}
