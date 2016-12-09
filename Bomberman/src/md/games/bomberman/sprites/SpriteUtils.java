/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Marc
 */
public final class SpriteUtils
{
    private SpriteUtils() {}
    
    public static BufferedImage[] arrayImages(int x, int y, int w, int h, int length, BufferedImage base)
    {
        BufferedImage[] res = new BufferedImage[length];
        int size = w * length + x;
        for(int idx=0,i=x;i<size;i+=w,idx++)
            res[idx] = base.getSubimage(i,y,w,h);
        return res;
    }
    public static BufferedImage[] arrayImages(int x, int y, int w, int h, int length, File file)
    {
        try
        {
            return arrayImages(x,y,w,h,length,ImageIO.read(file));
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    public static BufferedImage[] arrayImages(int x, int y, int w, int h, int length, String path)
    {
        return arrayImages(x,y,w,h,length,new File(path));
    }
    public static BufferedImage[] arrayImages(int w, int h, int length, BufferedImage base)
    {
        return arrayImages(0,0,w,h,length,base);
    }
    public static BufferedImage[] arrayImages(int w, int h, int length, File file)
    {
        return arrayImages(0,0,w,h,length,file);
    }
    public static BufferedImage[] arrayImages(int w, int h, int length, String path)
    {
        return arrayImages(0,0,w,h,length,new File(path));
    }
    
    public static BufferedImage[][] matrixImages(int rows, int columns, int w, int h, BufferedImage base)
    {
        BufferedImage[][] res = new BufferedImage[rows][];
        int size = h * rows;
        for(int idx=0,i=0;i<size;i+=h,idx++)
            res[idx] = arrayImages(0,i,w,h,columns,base);
        return res;
    }
    public static BufferedImage[][] matrixImages(int rows, int columns, int w, int h, File file)
    {
        try
        {
            return matrixImages(rows,columns,w,h,ImageIO.read(file));
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    public static BufferedImage[][] matrixImages(int rows, int columns, int w, int h, String path)
    {
        return matrixImages(rows,columns,w,h,new File(path));
    }
    
    public static final BufferedImage duplicateBufferedImage(BufferedImage bi)
    {
        BufferedImage nbi = new BufferedImage(bi.getWidth(),bi.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = nbi.createGraphics();
        g.drawImage(bi,0,0,null);
        g.dispose();
        return nbi;
    }
}
