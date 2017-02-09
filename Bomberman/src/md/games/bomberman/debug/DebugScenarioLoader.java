/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.debug;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import md.games.bomberman.creature.player.PlayerFeatures;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.input.InputController;
import md.games.bomberman.input.InputMasks;
import md.games.bomberman.peripheral.KeyID;
import md.games.bomberman.peripheral.PeripheralMask;
import md.games.bomberman.placeable.Rock;
import md.games.bomberman.placeable.RockFactory;
import md.games.bomberman.placeable.RockType;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.ScenarioManager;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.script.Script;
import md.games.bomberman.script.ScriptManager;
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
public final class DebugScenarioLoader extends LPLObject
{
    private final ScenarioManager smanager;
    private final LPLEnvironment env;
    private Scenario scenario;
    private final PlayerFeatures pfeat;
    private final Point playerInitTile;
    private final Point viewport;
    private boolean fullscreen;
    
    private DebugScenarioLoader()
    {
        smanager = new ScenarioManager();
        pfeat = new PlayerFeatures();
        env = new LPLEnvironment();
        playerInitTile = new Point();
        viewport = new Point(640,480);
    }
    
    public static final DebugScenarioLoader loadScenario(File file) throws LPLCompilerException
    {
        DebugScenarioLoader loader = new DebugScenarioLoader();
        LPLGlobals globals = LPLGlobals.createGlobals(loader.env);
        globals.setGlobalValue("CreateScenario",loader.SCENARIO_INIT);
        LPLFunction func = loader.env.compile(file,"ScenarioLoaderScript",globals);
        func.call();
        LPLGlobals subGlobals = LPLGlobals.createGlobals(globals);
        loader.scenario.getScriptManager().compileAll(loader.scenario,subGlobals);
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
            case "putRock": return PUT_ROCK;
            case "getPlayerFeatures": return GET_PLAYER_FEATURES;
            case "setPlayerStartTile": return SET_PLAYER_START_TILE;
            case "loadTheme": return LOAD_THEME;
            case "initTiles": return INIT_TILES;
            case "setWindowConfig": return SET_WINDOW_CONFIG;
            case "bindKey": return BIND_KEY;
            case "addScript": return ADD_SCRIPT;
            case "setOnInitScript": return SET_ON_INIT_SCRIPT;
        }
    }
    
    
    private static final LPLValue PUT_ROCK = LPLFunction.createVVarFunction((args) -> {
        DebugScenarioLoader loader = args.arg0().toLPLObject();
        RockType type = RockType.decode(args.arg(1));
        int row = args.arg(2).toJavaInt();
        int column = args.arg(3).toJavaInt();
        float wFactor = args.arg(4).toJavaFloat();
        float hFactor = args.arg(5).toJavaFloat();
        wFactor = wFactor < 0 ? 0 : wFactor > 1 ? 1 : wFactor;
        hFactor = hFactor < 0 ? 0 : hFactor > 1 ? 1 : hFactor;
        
        Vector2 size = loader.scenario.getTileManager().getTileSize();
        size.x *= wFactor;
        size.y *= hFactor;
        Rock rock = RockFactory.create(loader.scenario.getSpriteManager(),type,size.x,size.y);
        loader.scenario.registerGameObject(rock);
        loader.scenario.getTileManager().getTile(row,column).putPlaceable(rock);
    });
    private static final LPLValue GET_PLAYER_FEATURES = LPLFunction.createFunction((arg0) -> {
        return arg0.<DebugScenarioLoader>toLPLObject().pfeat;
    });
    private static final LPLValue SET_PLAYER_START_TILE = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        arg0.<DebugScenarioLoader>toLPLObject().playerInitTile.setLocation(arg2.toJavaInt(),arg1.toJavaInt());
    });
    private static final LPLValue LOAD_THEME = LPLFunction.createVFunction((arg0, arg1) -> {
        try { arg0.<DebugScenarioLoader>toLPLObject().smanager.loadTheme(arg1.toJavaString()); }
        catch(IOException ex) { throw new LPLRuntimeException(ex); }
    });
    private static final LPLValue INIT_TILES = LPLFunction.createVFunction((arg0, arg1) -> {
        DebugScenarioLoader loader = arg0.toLPLObject();
        for(Tile tile : loader.scenario.getTileManager())
            arg1.call(tile);
    });
    private static final LPLValue SET_WINDOW_CONFIG = LPLFunction.createVVarFunction((args) -> {
        DebugScenarioLoader loader = args.arg0().toLPLObject();
        loader.viewport.setLocation(args.arg(1).toJavaInt(),args.arg(2).toJavaInt());
        loader.fullscreen = args.arg(3).toJavaBoolean();
    });
    private static final LPLValue BIND_KEY = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        int code = findKeyId(arg1.toJavaString());
        PeripheralMask mask = findMask(arg2.toJavaString());
        if(code < 0 || mask == null)
            return;
        InputController.assignPeripheralIdToMask(code,mask);
    });
    private static final LPLValue ADD_SCRIPT = LPLFunction.createVFunction((arg0, arg1, arg2) -> {
        DebugScenarioLoader loader = arg0.toLPLObject();
        File file = new File(arg2.toJavaString());
        if(!file.exists() || !file.isFile())
            return;
        ScriptManager scripts = loader.scenario.getScriptManager();
        Script s = scripts.createScript(arg1.toJavaString());
        String code = extractStringFromFile(file);
        if(code == null)
            return;
        s.setCode(code);
    });
    private static final LPLValue SET_ON_INIT_SCRIPT = LPLFunction.createVFunction((arg0, arg1) -> {
        DebugScenarioLoader loader = arg0.toLPLObject();
        loader.scenario.setOnInitScript(arg1.toJavaString());
    });
    
    
    
    
    private final LPLValue SCENARIO_INIT = LPLFunction.createFunction((arg0, arg1) -> {
        int rows = arg0.toJavaInt();
        int columns = arg1.toJavaInt();
        DebugScenarioLoader.this.scenario = DebugScenarioLoader.this.smanager.createDebugScenario(rows,columns);
        return DebugScenarioLoader.this;
    });
    
    
    private static int findKeyId(String name)
    {
        try
        {
            Field field = KeyID.class.getField("VK_" + name.toUpperCase());
            if(field == null)
                return -1;
            int value = field.getInt(null);
            return KeyID.encode(value);
        }
        catch(NoSuchFieldException | IllegalAccessException ex)
        {
            return -1;
        }
    }
    
    private static String extractStringFromFile(File file)
    {
        StringBuilder sb = new StringBuilder();
        sb.setLength((int) (file.length() / 2));
        char[] buffer = new char[8192];
        int len;
        try(FileReader fr = new FileReader(file))
        {
            while((len = fr.read(buffer)) > 0)
                sb.append(buffer,0,len);
            return sb.toString();
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return null;
        }
    }
    
    private static PeripheralMask findMask(String name)
    {
        try
        {
            Field field = InputMasks.class.getField(name.toUpperCase());
            if(field == null)
                return null;
            return (PeripheralMask) field.get(null);
        }
        catch(NoSuchFieldException | IllegalAccessException | ClassCastException ex)
        {
            return null;
        }
    }
}
