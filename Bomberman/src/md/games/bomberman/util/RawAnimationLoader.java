/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;
import md.games.bomberman.io.Resource;
import md.games.bomberman.sprites.Animation;
import nt.dal.DAL;
import nt.dal.DALUtils;
import nt.dal.Routines;
import nt.dal.data.DALBlock;
import nt.dal.data.DALData;

/**
 *
 * @author Asus
 */
public final class RawAnimationLoader
{
    private final Routines routines;
    private HashMap<String, Animation> cache;
    private RawAnimationData dataCache;
    
    public RawAnimationLoader()
    {
        routines = new Routines();
        cache = null;
        dataCache = null;
        initRoutines();
    }
    
    private void initRoutines()
    {
        DALUtils.insertBasicFeatures(routines);
        
        routines.putRoutine("animation",(rou, env, pars) -> {
            try
            {
                if(dataCache != null || cache == null)
                    throw new IllegalStateException("Cannot create animation if another animation is creating at the same time.");
                String tag = pars.getParameter(0).toJavaString();
                BufferedImage base = Resource.SPRITES.loadRawImage(pars.getParameter(1).toJavaString());
                DALBlock data = pars.getParameter(2).asDALBlock();
                dataCache = new RawAnimationData(base);
                data.safeRun(rou);
                cache.put(tag,new Animation(dataCache));
                dataCache = null;
            }
            catch(IOException ex) { throw new RuntimeException(ex); }
        },false);
        
        routines.putRoutine("sequence",(rou, env, pars) -> {
            if(dataCache == null || cache == null)
                throw new IllegalStateException("Cannot create a sequence if not started animation creation before.");
            dataCache.addSequence(
                    pars.getParameter(0).toJavaInt(),
                    pars.getParameter(1).toJavaInt(),
                    pars.getParameter(2).toJavaInt(),
                    pars.getParameter(3).toJavaInt(),
                    pars.getParameter(4).toJavaInt());
        });
    }
    
    public final Map<String, Animation> loadFromScript(Resource resource, String fileName, String... enabledFlags) throws IOException
    {
        if(cache != null)
            throw new IllegalStateException();
        File file = resource.getFile(fileName);
        cache = new HashMap<>();
        Map<String, Animation> map = cache;
        try
        {
            if(enabledFlags.length <= 0)
                DAL.execute(file,routines);
            else
            {
                DALBlock env = DALBlock.asObjectFromMap(Arrays.stream(enabledFlags).collect(Collectors.toMap(
                        flag -> DALData.valueOf(flag),
                        flag -> DALData.TRUE
                )));
                DAL.execute(file,env,routines);
            }
        }
        finally { cache = null; }
        return map;
    }
    
    public static final class RawAnimationData implements Iterable<RawAnimationData.RawAnimationSequence>
    {
        private final BufferedImage base;
        private final LinkedList<RawAnimationSequence> parts;
        
        private RawAnimationData(BufferedImage base)
        {
            this.base = base;
            parts = new LinkedList<>();
        }
        
        public final BufferedImage getBase() { return base; }
        
        public final int getSequenceCount() { return parts.size(); }
        
        public final RawAnimationSequence getSequence(int index) { return parts.get(index); }
        
        private void addSequence(int x, int y, int width, int height, int frames)
        {
            parts.add(new RawAnimationSequence(x,y,width,height,frames));
        }

        @Override
        public final Iterator<RawAnimationSequence> iterator() { return parts.iterator(); }
        
        public final class RawAnimationSequence
        {
            private final int x, y;
            private final int width, height;
            private final int frames;
            
            private RawAnimationSequence(int x, int y, int width, int height, int frames)
            {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.frames = frames;
            }
            
            public final BufferedImage getBase() { return base; }
            
            public final int getX() { return x; }
            public final int getY() { return y; }
            public final int getWidth() { return width; }
            public final int getHeight() { return height; }
            public final int getFramesCount() { return frames; }
        }
    }
}
