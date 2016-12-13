/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import md.games.bomberman.io.Resource;
import md.games.bomberman.sprites.AnimatedSprite;
import md.games.bomberman.sprites.Sprite;
import md.games.bomberman.sprites.SpriteUtils;
import md.games.bomberman.sprites.StaticSprite;
import nt.dal.DAL;
import nt.dal.DALParseException;
import nt.dal.DALUtils;
import nt.dal.Routines;
import nt.dal.data.DALBlock;
import nt.dal.data.DALData;
import nt.dal.data.DALUserdata;

/**
 *
 * @author Asus
 */
public final class ScenarioTheme
{
    private static final Routines ROUTINES = initRoutines();
    
    private final String name;
    private final HashMap<String,BufferedImage> rawCache;
    private final HashMap<String,Sprite> spriteCache;
    
    private ScenarioTheme(String name)
    {
        if(name == null)
            throw new NullPointerException();
        this.name = name;
        rawCache = new HashMap<>();
        spriteCache = new HashMap<>();
    }
    
    public static final ScenarioTheme loadTheme(String name) throws FileNotFoundException, IOException
    {
        ScenarioTheme theme = new ScenarioTheme(name);
        DALBlock env = DALBlock.asObject();
        env.setAttribute(STR_SCENARIO,new DALUserdata(theme));
        File initFile = new File(Resource.THEMES.getAbsolutePath() + "/" + name + "/.theme");
        if(!initFile.exists() || !initFile.isDirectory())
            throw new FileNotFoundException(".theme file in " + name + " theme not found");
        try { DAL.execute(initFile,env,ROUTINES); }
        catch(DALParseException ex) { throw new IOException(ex); }
        return theme;
    }
    
    public final <S extends Sprite> S getSprite(String tag)
    {
        return (S) spriteCache.get(tag);
    }
    
    private static final DALData STR_SCENARIO = DALData.valueOf("__scenario__");
    private static Routines initRoutines()
    {
        Routines routines = new Routines();
        DALUtils.insertBasicFeatures(routines);
        
        routines.putRoutine("sprite.static",(rou,env,pars) -> {
            ScenarioTheme theme = env.getAttribute(STR_SCENARIO).<ScenarioTheme>asDALUserdata().get();
            String path = pars.getParameter(0).toJavaString();
            String tag = pars.getParameter(1).toJavaString();
            BufferedImage raw = theme.loadRaw(path);
            StaticSprite ss = new StaticSprite(raw);
            ss.setSpriteTag(tag);
            theme.spriteCache.put(tag,ss);
        });
        
        routines.putRoutine("sprite.animated",(rou,env,pars) -> {
            ScenarioTheme theme = env.getAttribute(STR_SCENARIO).<ScenarioTheme>asDALUserdata().get();
            String path = pars.getParameter(0).toJavaString();
            String tag = pars.getParameter(1).toJavaString();
            int x, y, width, height, count;
            if(pars.getParameterCount() > 5)
            {
                x = pars.getParameter(2).toJavaInt();
                y = pars.getParameter(3).toJavaInt();
                width = pars.getParameter(4).toJavaInt();
                height = pars.getParameter(5).toJavaInt();
                count = pars.getParameter(6).toJavaInt();
            }
            else
            {
                x = y = 0;
                width = pars.getParameter(2).toJavaInt();
                height = pars.getParameter(3).toJavaInt();
                count = pars.getParameter(4).toJavaInt();
            }
            BufferedImage raw = theme.loadRaw(path);
            BufferedImage[] raws = SpriteUtils.arrayImages(x,y,width,height,count,raw);
            AnimatedSprite as = new AnimatedSprite(raws);
            as.setSpriteTag(tag);
            theme.spriteCache.put(tag,as);
        });
        
        return routines;
    }
    private BufferedImage loadRaw(String path)
    {
        try
        {
            BufferedImage raw = rawCache.get(path);
            if(raw == null)
            {
                try { raw = Resource.THEMES.loadRawImage(name + "/" + path); }
                catch(IOException ex)
                {
                    raw = Resource.THEMES.loadRawImage(path);
                }
                rawCache.put(path,raw);
            }
            return raw;
        }
        catch(IOException ex)
        {
            throw new RuntimeException(ex);
        }
    }
}
