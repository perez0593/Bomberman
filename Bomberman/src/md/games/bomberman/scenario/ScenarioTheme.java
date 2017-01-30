/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import md.games.bomberman.io.Resource;
import md.games.bomberman.sprites.SpriteManager;
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
    private final SpriteManager smanager;
    private final LinkedList<String> cache;
    
    private ScenarioTheme(SpriteManager smanager, String name)
    {
        if(name == null)
            throw new NullPointerException();
        if(smanager == null)
            throw new NullPointerException();
        this.name = name;
        this.smanager = smanager;
        cache = new LinkedList<>();
    }
    
    public final Iterable<String> getIterableTags() { return cache; }
    
    public static final ScenarioTheme loadTheme(SpriteManager smanager, String name) throws FileNotFoundException, IOException
    {
        ScenarioTheme theme = new ScenarioTheme(smanager,name);
        DALBlock env = DALBlock.asObject();
        env.setAttribute(STR_SCENARIO,new DALUserdata(theme));
        File initFile = new File(Resource.THEMES.getAbsolutePath() + "/" + name + "/theme");
        if(!initFile.exists() || !initFile.isFile())
            throw new FileNotFoundException("theme file in " + name + " theme not found");
        try { DAL.execute(initFile,env,ROUTINES); }
        catch(DALParseException ex) { throw new IOException(ex); }
        return theme;
    }
    
    private static final DALData STR_SCENARIO = DALData.valueOf("__scenario__");
    private static Routines initRoutines()
    {
        Routines routines = new Routines();
        DALUtils.insertBasicFeatures(routines);
        
        routines.putRoutine("sprite.static",(rou,env,pars) -> {
            ScenarioTheme theme = env.getDeepAttribute(STR_SCENARIO).<ScenarioTheme>asDALUserdata().get();
            String path = theme.name + "/" + pars.getParameter(0).toJavaString();
            String tag = "theme." + pars.getParameter(1).toJavaString();
            try { theme.smanager.loadStaticSprite(Resource.THEMES,path,tag); }
            catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }
            theme.cache.add(tag);
        });
        
        routines.putRoutine("sprite.region",(rou,env,pars) -> {
            ScenarioTheme theme = env.getDeepAttribute(STR_SCENARIO).<ScenarioTheme>asDALUserdata().get();
            String path = theme.name + "/" + pars.getParameter(0).toJavaString();
            String tag = "theme." + pars.getParameter(1).toJavaString();
            int x = pars.getParameter(2).toJavaInt();
            int y = pars.getParameter(3).toJavaInt();
            int w = pars.getParameter(4).toJavaInt();
            int h = pars.getParameter(5).toJavaInt();
            try { theme.smanager.loadSubRegionStaticSprite(Resource.THEMES,path,tag,x,y,w,h); }
            catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }
            theme.cache.add(tag);
        });
        
        routines.putRoutine("sprite.animated",(rou,env,pars) -> {
            ScenarioTheme theme = env.getDeepAttribute(STR_SCENARIO).<ScenarioTheme>asDALUserdata().get();
            String path = theme.name + "/" + pars.getParameter(0).toJavaString();
            String tag = "theme." + pars.getParameter(1).toJavaString();
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
            try { theme.smanager.loadAnimatedSprite(Resource.THEMES,path,tag,x,y,width,height,count); }
            catch(IOException ex)
            {
                throw new RuntimeException(ex);
            }
            theme.cache.add(tag);
        });
        
        return routines;
    }
}
