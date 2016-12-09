/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import nt.ntjg.geom.screen.Dimension;

/**
 *
 * @author Marc
 */
public class Text
{
    static Dimension stringDim(Graphics2D g2, String text, Font font)
    {
        Rectangle2D s = font.getStringBounds(text, g2.getFontRenderContext());
        Dimension d = new Dimension();
        d.width = (int) s.getWidth();
        d.height = (int) s.getHeight();
        return d;
    }
    
    static Dimension stringDim(FontRenderContext fcontext, String text, Font font)
    {
        Rectangle2D s = font.getStringBounds(text,fcontext);
        Dimension d = new Dimension();
        d.width = (int) s.getWidth();
        d.height = (int) s.getHeight();
        return d;
    }
    
    static Dimension stringDim(String text, Font font)
    {
        Rectangle2D s = font.getStringBounds(text,new FontRenderContext(new AffineTransform(),false,false));
        Dimension d = new Dimension();
        d.width = (int) s.getWidth();
        d.height = (int) s.getHeight();
        return d;
    }
    
    public static void print(Graphics g, String text, Font font, int x, int y, Color color)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setColor(color);
        g2.drawString(text,x,y);
        //g2.drawString(text,x,y);
    }
    
    public static void printCentre(Graphics g, String text, Font font, int x, int y, Color color)
    {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = stringDim(g2,text,font);
        g2.setFont(font);
        int asc = g2.getFontMetrics().getAscent();
        g2.setColor(color);
        g2.drawString(text,x-(d.width/2),y-(d.height/2)+asc);
    }
    
    public static void printFinal(Graphics g, String text, Font font, int x, int y, Color color)
    {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = stringDim(g2,text,font);
        g2.setFont(font);
        int asc = g2.getFontMetrics().getAscent();
        g2.setColor(color);
        g2.drawString(text,x-(d.width),y-(d.height)+asc);
    }
    
    public static Font newFont(String path)
    {
        try
        {
            return Font.createFont(Font.TRUETYPE_FONT,new File(path));
        }
        catch(FontFormatException | IOException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
}
