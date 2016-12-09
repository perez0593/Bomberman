/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.font;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import md.games.bomberman.sprites.Sprite;
import md.games.bomberman.sprites.StaticSprite;

/**
 *
 * @author Marc
 */
public class SpriteFont implements Font
{
    private final HashMap<Character,Sprite> map;
    private int size;
    private int ispace;
    
    public SpriteFont()
    {
        size = 16;
        ispace = 0;
        map = new HashMap<>();
    }
    
    public void setInterletterSpace(int value)
    {
        ispace = value;
    }
    
    public int getTextSize(String text)
    {
        int dim = -ispace;
        char c;
        for(int i=0;i<text.length();i++)
        {
            c = text.charAt(i);
            dim += ispace;
            if(c == ' ')
            {
                dim += size;
                continue;
            }
            dim += map.get(c).width();
        }
        return dim;
    }
    
    public int getDimensions() { return size; }
    
    public void addCharacter(char character, Sprite bmp)
    {
        if(bmp.height() > size)
            size = bmp.height();
        map.put(character,bmp);
    }
    
    public void decodeDirectory(File dir) throws IOException
    {
        if(!dir.exists() || !dir.isDirectory()) return;
        map.clear();
        for(File file : dir.listFiles())
            addCharacter(file.getName().charAt(0),new StaticSprite(file));
    }

    @Override
    public void print(Graphics2D g, String text, int x, int y)
    {
        Sprite bmp;
        char c;
        for(int i=0;i<text.length();i++)
        {
            c = text.charAt(i);
            if(c == ' ')
            {
                x += size+ispace;
                continue;
            }
            bmp = map.get(c);
            bmp.draw(g,x,y,bmp.width(),size);
            x += bmp.width() + ispace;
        }
    }
    
    public void print(Graphics2D g, String text, int x, int y, int width, int height)
    {
        Sprite bmp;
        char c;
        int msize = (int) (height / (float) size);
        for(int i=0;i<text.length();i++)
        {
            c = text.charAt(i);
            if(c == ' ')
            {
                x += msize+ispace;
                continue;
            }
            bmp = map.get(c);
            bmp.draw(g,x,y,width / (float) bmp.width(),msize);
            x += width / (float) bmp.width() + ispace;
        }
    }
    
    @Override
    public StaticSprite generateImage(String text)
    {
        int dim = getTextSize(text);
        BufferedImage bi = new BufferedImage(dim,size,BufferedImage.TYPE_INT_ARGB);
        bi.setAccelerationPriority(1f);
        StaticSprite img = new StaticSprite(bi);
        print(bi.createGraphics(),text,0,0);
        return img;
    }
    
    /*@Override
    public Texture generateImage(String text)
    {
        return generateImage(text);
    }*/

    @Override
    public void printCentre(Graphics2D g, String text, int x, int y)
    {
        print(g,text,x-(getTextSize(text)/2),y);
    }
    
    public void printCentre(Graphics2D g, String text, int x, int y, int width, int height)
    {
        print(g,text,x-(getTextSize(text)/2),y,width,height);
    }

    @Override
    public void printFinal(Graphics2D g, String text, int x, int y)
    {
        print(g,text,x-getTextSize(text),y);
    }
    
    public void printFinal(Graphics2D g, String text, int x, int y, int width, int height)
    {
        print(g,text,x-getTextSize(text),y,width,height);
    }

    @Override
    public void setColor(Color color) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDimensions(int size) throws UnsupportedOperationException
    {
        throw new UnsupportedOperationException();
    }
}
