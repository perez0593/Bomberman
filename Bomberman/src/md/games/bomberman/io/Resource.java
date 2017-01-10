/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import md.games.bomberman.sprites.Animation;
import nt.adm.AnimationData;

/**
 *
 * @author mpasc
 */
public final class Resource
{
    private static final String ROOT_PATH = System.getProperty("user.dir") + "/bin";
    
    
    private final File base;
    private final String pathBase;
    
    private Resource(String path)
    {
        base = new File(ROOT_PATH + "/" + path);
        pathBase = base.getAbsolutePath();
        base.mkdirs();
    }
    
    public final String getAbsolutePath() { return pathBase; }
    
    private File getFile(String path) throws FileNotFoundException
    {
        File file = new File(pathBase + "/" + path);
        if(!file.exists() || !file.isFile())
            throw new FileNotFoundException();
        return file;
    }
    
    public final BufferedImage loadRawImage(String path) throws FileNotFoundException, IOException
    {
        return ImageIO.read(getFile(path));
    }
    
    public final Animation loadAnimation(String path, int width, int height) throws FileNotFoundException, IOException
    {
        AnimationData ad = AnimationData.load(getFile(path));
        return new Animation(ad,width,height);
    }
    
    /* FILES */
    
    public static final Resource SPRITES = new Resource("sprites");
    public static final Resource THEMES = new Resource("sprites/themes");
}
