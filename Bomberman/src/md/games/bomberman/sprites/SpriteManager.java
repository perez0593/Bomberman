/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import md.games.bomberman.io.Resource;
import md.games.bomberman.scenario.ScenarioTheme;

/**
 *
 * @author Asus
 */
public final class SpriteManager
{
    private final HashMap<String, BufferedImage> rawCache;
    private final HashMap<String, Sprite> spriteCache;
    private ScenarioTheme theme;
    
    public SpriteManager()
    {
        rawCache = new HashMap<>();
        spriteCache = new HashMap<>();
    }
    
    public final StaticSprite loadStaticSprite(Resource resource, String path, String tag) throws IOException
    {
        if(spriteCache.containsKey(tag))
            throw new IllegalArgumentException("Sprite with tag \"" + tag + "\" already exists");
        BufferedImage base = loadRaw(resource,path);
        StaticSprite ss = new StaticSprite(base);
        ss.setSpriteTag(tag);
        spriteCache.put(tag,ss);
        return ss;
    }
    public final StaticSprite loadStaticSprite(String path, String tag) throws IOException
    {
        return loadStaticSprite(Resource.SPRITES,path,tag);
    }
    
    public final StaticSprite loadSubRegionStaticSprite(Resource resource, String path, String tag, int x, int y, int width, int height) throws IOException
    {
        if(spriteCache.containsKey(tag))
            throw new IllegalArgumentException("Sprite with tag \"" + tag + "\" already exists");
        BufferedImage base = loadRaw(resource,path);
        SubRegionStaticSprite ss = SubRegionStaticSprite.fromSize(base,x,y,width,height);
        ss.setSpriteTag(tag);
        spriteCache.put(tag,ss);
        return ss;
    }
    
    public final AnimatedSprite loadAnimatedSprite(Resource resource, String path, String tag, int x, int y, int width, int height, int frames) throws IOException
    {
        if(spriteCache.containsKey(tag))
            throw new IllegalArgumentException("Sprite with tag \"" + tag + "\" already exists");
        BufferedImage base = loadRaw(resource,path);
        //BufferedImage[] raws = SpriteUtils.arrayImages(x,y,width,height,frames,base);
        AnimatedSprite as = new AnimatedSprite(base,x,y,width,height,frames);
        as.setSpriteTag(tag);
        spriteCache.put(tag,as);
        return as;
    }
    public final AnimatedSprite loadAnimatedSprite(Resource resource, String path, String tag, int width, int height, int frames) throws IOException
    {
        return loadAnimatedSprite(resource,path,tag,0,0,width,height,frames);
    }
    public final AnimatedSprite loadAnimatedSprite(String path, String tag, int x, int y, int width, int height, int frames) throws IOException
    {
        return loadAnimatedSprite(Resource.SPRITES,path,tag,x,y,width,height,frames);
    }
    public final AnimatedSprite loadAnimatedSprite(String path, String tag, int width, int height, int frames) throws IOException
    {
        return loadAnimatedSprite(Resource.SPRITES,path,tag,0,0,width,height,frames);
    }
    
    public final void loadAnimations(Resource resource, String path, String... enabledFlags) throws IOException
    {
        Map<String, Animation> map = resource.loadAnimations(path,enabledFlags);
        for(Map.Entry<String, Animation> e : map.entrySet())
        {
            if(spriteCache.containsKey(e.getKey()))
                throw new IllegalArgumentException("Animation or sprite with tag \"" + e.getKey() + "\" already exists");
            spriteCache.put(e.getKey(),e.getValue());
        }
    }
    public final void loadAnimations(String path, String... enabledFlags) throws IOException
    {
        loadAnimations(Resource.SPRITES,path,enabledFlags);
    }
    
    /*public final void loadScenarioTheme(String name) throws IOException
    {
        if(theme != null)
            throw new IllegalStateException("Another scenario theme is loaded");
        theme = ScenarioTheme.loadTheme(this,name);
    }*/
    
    private BufferedImage loadRaw(Resource resource, String path) throws IOException
    {
        BufferedImage raw = rawCache.get(path);
        if(raw == null)
        {
            raw = resource.loadRawImage(path);
            rawCache.put(path,raw);
        }
        return raw;
    }
    
    public final void clearRawCache()
    {
        rawCache.clear();
    }
    
    public final void clearSpriteCache()
    {
        spriteCache.clear();
    }
    
    public final void unloadSprite(String tag)
    {
        spriteCache.remove(tag);
    }
    
    /*public final void unloadScenarioTheme()
    {
        if(theme == null)
            throw new IllegalStateException("There is not any theme loaded");
        for(String tag : theme.getIterableTags())
            spriteCache.remove(tag);
        theme = null;
    }*/
    
    public final <S extends Sprite<S>> S getSprite(String tag)
    {
        Sprite s = spriteCache.get(tag);
        return s == null ? null : (S) s.duplicate();
    }
    
    //public final boolean hasThemeLoaded() { return theme != null; }
}
