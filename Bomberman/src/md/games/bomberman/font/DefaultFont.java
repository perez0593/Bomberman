/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.font;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import md.games.bomberman.sprites.StaticSprite;
import nt.ntjg.geom.screen.Dimension;

/**
 *
 * @author Marc
 */
public class DefaultFont implements Font
{
    private static final FontRenderContext frtDef = new FontRenderContext(new AffineTransform(),false,false);
    
    private java.awt.Font fnt;
    private Color color;
    private int size;
    
    public DefaultFont()
    {
        this(Color.BLACK,12);
    }
    public DefaultFont(Color color)
    {
        this(color,12);
    }
    public DefaultFont(int size)
    {
        this(Color.BLACK,size);
    }
    public DefaultFont(Color color, int size)
    {
        fnt = new java.awt.Font("arial",java.awt.Font.BOLD,size);
        this.size = size;
        this.color = color;
    }
    public DefaultFont(String path)
    {
        this(path,12,Color.BLACK);
    }
    public DefaultFont(String path, Color color)
    {
        this(path,12,color);
    }
    public DefaultFont(String path, int size)
    {
        this(path,size,Color.BLACK);
    }
    public DefaultFont(String path, int size, Color color)
    {
        fnt = Text.newFont(path).deriveFont((float)size);
        this.size = size;
        this.color = color;
    }
    
    public DefaultFont(java.awt.Font base, int size, Color color)
    {
        fnt = base.deriveFont((float)size);
        this.size = size;
        this.color = color;
    }
    
    @Override
    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }
    
    @Override
    public void setDimensions(int size)
    {
        this.size = size;
        fnt = fnt.deriveFont((float)size);
    }
    public int getDimensions() { return size; }
    
    public java.awt.Font basicFont() { return fnt; }
    
    @Override
    public void print(Graphics2D g, String text, int x, int y)
    {
        Text.print(g,text,fnt,x,y,color);
    }

    @Override
    public void printCentre(Graphics2D g, String text, int x, int y)
    {
        Text.printCentre(g,text,fnt,x,y,color);
    }

    @Override
    public void printFinal(Graphics2D g, String text, int x, int y)
    {
        Text.printFinal(g,text,fnt,x,y,color);
    }

    @Override
    public StaticSprite generateImage(String text)
    {
        Dimension dim = Text.stringDim(frtDef,text,fnt);
        BufferedImage bi = new BufferedImage(dim.width,dim.height,BufferedImage.TYPE_INT_ARGB);
        bi.setAccelerationPriority(1f);
        StaticSprite img = new StaticSprite(bi);
        print(bi.createGraphics(),text,0,0);
        return img;
    }
    
}
