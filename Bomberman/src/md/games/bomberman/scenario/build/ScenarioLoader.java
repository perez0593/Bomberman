/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.build;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import md.games.bomberman.creature.player.PlayerFeatures;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.placeable.Rock;
import md.games.bomberman.placeable.RockFactory;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.ScenarioManager;
import md.games.bomberman.scenario.Tile;
import nt.lpl.LPLEnvironment;
import nt.lpl.LPLGlobals;
import nt.lpl.LPLRuntimeException;
import nt.lpl.compiler.LPLCompilerException;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public final class ScenarioLoader extends LPLObject
{
    private final ScenarioManager smanager;
    private final LPLEnvironment env;
    private Scenario scenario;
    private final PlayerFeatures pfeat;
    private final Point playerInitTile;
    private final Point viewport;
    private boolean fullscreen;
    
    private ScenarioLoader()
    {
        smanager = new ScenarioManager();
        pfeat = new PlayerFeatures();
        env = new LPLEnvironment();
        playerInitTile = new Point();
        viewport = new Point(640,480);
    }
    
    public static final ScenarioLoader loadScenario(File file) throws LPLCompilerException
    {
        ScenarioLoader loader = new ScenarioLoader();
        LPLGlobals globals = LPLGlobals.createGlobals(loader.env);
        globals.setGlobalValue("CreateScenario",loader.SCENARIO_INIT);
        LPLFunction func = loader.env.compile(file,"ScenarioLoaderScript",globals);
        func.call();
        return loader;
    }
    
    public final ScenarioManager getScenarioManager() { return smanager; }
    public final PlayerFeatures getPlayerFeatures() { return pfeat; }
    public final Point getPlayerStartTile() { return playerInitTile; }
    public final Point getViewport() { return viewport; }
    public final boolean getIsFullscreen() { return fullscreen; }
    
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "putWall": return PUT_WALL;
            case "getPlayerFeatures": return GET_PLAYER_FEATURES;
            case "setPlayerStartTile": return SET_PLAYER_START_TILE;
            case "loadTheme": return LOAD_THEME;
            case "initTiles": return INIT_TILES;
            case "setWindowConfig": return SET_WINDOW_CONFIG;
        }
    }
    
    
    private static final LPLValue PUT_WALL = LPLFunction.createVVarFunction((args) -> {
        ScenarioLoader loader = args.arg0().toLPLObject();
        int row = args.arg(1).toJavaInt();
        int column = args.arg(2).toJavaInt();
        float wFactor = args.arg(3).toJavaFloat();
        float hFactor = args.arg(4).toJavaFloat();
        wFactor = wFactor < 0 ? 0 : wFactor > 1 ? 1 : wFactor;
        hFactor = hFactor < 0 ? 0 : hFactor > 1 ? 1 : hFactor;
        
        Vector2 size = loader.scenario.getTileManager().getTileSize();
        size.x *= wFactor;
        size.y *= hFactor;
        Rock rock = RockFactory.create(loader.scenario.getSpriteManager(),RockFactory.RockId.UNBREAKABLE,size.x,size.y);
        loader.scenario.registerGameObject(rock);
        loader.scenario.getTileManager().getTile(row,column).putPlaceable(rock);
    });
    private static final LPLValue GET_PLAYER_FEATURES = LPLFunction.createFunction((arg0) -> {
        return arg0.<ScenarioLoader>toLPLObject().pfeat;
    });
    private static final LPLValue SET_PLAYER_START_TILE = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        arg0.<ScenarioLoader>toLPLObject().playerInitTile.setLocation(arg2.toJavaInt(),arg1.toJavaInt());
    });
    private static final LPLValue LOAD_THEME = LPLFunction.createVFunction((arg0, arg1) -> {
        try { arg0.<ScenarioLoader>toLPLObject().smanager.loadTheme(arg1.toJavaString()); }
        catch(IOException ex) { throw new LPLRuntimeException(ex); }
    });
    private static final LPLValue INIT_TILES = LPLFunction.createVFunction((arg0, arg1) -> {
        ScenarioLoader loader = arg0.toLPLObject();
        for(Tile tile : loader.scenario.getTileManager())
            arg1.call(tile);
    });
    private static final LPLValue SET_WINDOW_CONFIG = LPLFunction.createVVarFunction((args) -> {
        ScenarioLoader loader = args.arg0().toLPLObject();
        loader.viewport.setLocation(args.arg(1).toJavaInt(),args.arg(2).toJavaInt());
        loader.fullscreen = args.arg(3).toJavaBoolean();
    });
    
    
    
    
    private final LPLValue SCENARIO_INIT = LPLFunction.createFunction((arg0, arg1) -> {
        int rows = arg0.toJavaInt();
        int columns = arg1.toJavaInt();
        ScenarioLoader.this.scenario = ScenarioLoader.this.smanager.createDebugScenario(rows,columns);
        return ScenarioLoader.this;
    });
}
