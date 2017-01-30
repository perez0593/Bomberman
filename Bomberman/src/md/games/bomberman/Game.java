/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;
import md.games.bomberman.creature.player.Player;
import md.games.bomberman.creature.player.PlayerColor;
import md.games.bomberman.creature.player.PlayerId;
import md.games.bomberman.font.DefaultFont;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.input.InputController;
import md.games.bomberman.input.InputMasks;
import md.games.bomberman.input.PlayerController;
import md.games.bomberman.io.Resource;
import md.games.bomberman.peripheral.KeyID;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import md.games.bomberman.placeable.Rock;
import md.games.bomberman.placeable.RockFactory;
import md.games.bomberman.placeable.RockFactory.RockId;
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.ScenarioManager;
import md.games.bomberman.scenario.TileManager;
import md.games.bomberman.scenario.build.ScenarioLoader;
import md.games.bomberman.util.CameraController;
import nt.ntjg.NTJG;
import nt.ntjg.NTJGFunctionalities;
import nt.ntjg.NTJGInputEvent;

/**
 *
 * @author Asus
 */
public final class Game
         implements NTJGFunctionalities, PeripheralMaskingListener
{
    private ScenarioManager scenarioManager;
    private CameraController camc;
    private PlayerController pc;
    
    @Override
    public void init()
    {
        InputController.initController();
        InputController.addListener(this);
        startDebugScenario();
    }

    @Override
    public void update(double delta)
    {
        InputController.update();
        camc.update(delta);
        scenarioManager.getScenario().getCamera().lookAt(pc.getPlayer());
        pc.update(delta);
        scenarioManager.update(delta);
    }

    @Override
    public void draw(Graphics2D g)
    {
        scenarioManager.draw(g);
        drawCamera(g);
    }

    @Override
    public void dispatchEvent(NTJGInputEvent event)
    {
        
    }

    @Override
    public void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        System.out.println(event.getID());
        if(event.getCode() == KeyID.encode(KeyID.VK_ESCAPE))
        {
            NTJG.ntjgAbort(0);
            return;
        }
        camc.dispatchMaskedEvent(event);
        pc.dispatchMaskedEvent(event);
        
        /*int code = event.getCode();
        if(code == KeyID.encode(KeyID.VK_RIGHT))
            scenarioManager.getScenario().getCamera().traslate(10,0);
        else if(code == KeyID.encode(KeyID.VK_LEFT))
            scenarioManager.getScenario().getCamera().traslate(-10,0);
        else if(code == KeyID.encode(KeyID.VK_DOWN))
            scenarioManager.getScenario().getCamera().traslate(0,10);
        else if(code == KeyID.encode(KeyID.VK_UP))
            scenarioManager.getScenario().getCamera().traslate(0,-10);*/
    }
    
    private static final DefaultFont FONT = new DefaultFont(Color.WHITE);
    private void drawCamera(Graphics2D g)
    {
        Camera cam = scenarioManager.getScenario().getCamera();
        String text = "[" + cam.getX() + ", " + cam.getY() + "] " + (1d / cam.getZoom() * 100) + "%";
        int maxx = NTJG.ntjgGetWindowWidth();
        int maxy = NTJG.ntjgGetWindowHeight();
        FONT.printFinal(g, text, maxx - 20, maxy - 40);
    }
    
    
    final void startDebugScenario()
    {
        try
        {
            ScenarioLoader sloader = ScenarioLoader.loadScenario(new File("testmap.lpl"));
            scenarioManager = sloader.getScenarioManager();
            Scenario scenario = scenarioManager.getScenario();
            
            scenario.getCamera().setCustomViewport(sloader.getViewport().x,sloader.getViewport().y);
            scenario.setCameraLimits();

            camc = new CameraController(scenario.getCamera());
            
            camc.setUpCode(KeyID.encode(KeyID.VK_UP));
            camc.setDownCode(KeyID.encode(KeyID.VK_DOWN));
            camc.setLeftCode(KeyID.encode(KeyID.VK_LEFT));
            camc.setRightCode(KeyID.encode(KeyID.VK_RIGHT));
            camc.setZoomInCode(KeyID.encode(KeyID.VK_I));
            camc.setZoomOutCode(KeyID.encode(KeyID.VK_O));
            
            InputController.assignPeripheralIdToMask(KeyID.encode(KeyID.VK_W),InputMasks.P1_MOVE_UP);
            InputController.assignPeripheralIdToMask(KeyID.encode(KeyID.VK_S),InputMasks.P1_MOVE_DOWN);
            InputController.assignPeripheralIdToMask(KeyID.encode(KeyID.VK_A),InputMasks.P1_MOVE_LEFT);
            InputController.assignPeripheralIdToMask(KeyID.encode(KeyID.VK_D),InputMasks.P1_MOVE_RIGHT);
            
            scenario.getSpriteManager().loadAnimations(Resource.ANIMATIONS,"bomberman.ad");
            
            pc = new PlayerController(scenario,sloader.getPlayerFeatures(),PlayerId.ONE,"NT",PlayerColor.WHITE);
            Player p = pc.createPlayer();
            scenario.addCreature(p);
            scenario.putCreatureInTile(sloader.getPlayerStartTile().x,sloader.getPlayerStartTile().y,p);
        }
        catch(Throwable th)
        {
            th.printStackTrace(System.err);
            NTJG.ntjgAbort(1);
        }
    }
    
    private static void putRock(Scenario scenario, int row, int column, double widthFactor, double heightFactor)
    {
        TileManager tiles = scenario.getTileManager();
        Vector2 size = tiles.getTileSize();
        size.x *= widthFactor;
        size.y *= heightFactor;
        Rock rock = RockFactory.create(scenario.getSpriteManager(),RockId.UNBREAKABLE,size.x,size.y);
        scenario.registerGameObject(rock);
        tiles.getTile(row,column).putPlaceable(rock);
    }
}
