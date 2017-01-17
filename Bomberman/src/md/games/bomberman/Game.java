/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman;

import java.awt.Color;
import java.awt.Graphics2D;
import md.games.bomberman.font.DefaultFont;
import md.games.bomberman.input.InputController;
import md.games.bomberman.peripheral.KeyID;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.ScenarioManager;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
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
        camc.dispatchMaskedEvent(event);
        
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
            scenarioManager = new ScenarioManager();
            Scenario scenario = scenarioManager.createDebugScenario(30,30);
            scenarioManager.loadTheme("basic_theme");
            
            TileManager tiles = scenario.getTileManager();
            tiles.setSize(800,800);
            for(int r=0;r<tiles.getRows();r++)
                for(int c=0;c<tiles.getColumns();c++)
                {
                    Tile tile = tiles.getTile(r,c);
                    if(r == c)
                        tile.setSprite(scenario.getSpriteManager().getSprite("block1"));
                    else tile.setSprite(scenario.getSpriteManager().getSprite("tile1"));
                }
            
            scenario.getCamera().setPosition(150,150);
            camc = new CameraController(scenario.getCamera());
            
            camc.setUpCode(KeyID.encode(KeyID.VK_UP));
            camc.setDownCode(KeyID.encode(KeyID.VK_DOWN));
            camc.setLeftCode(KeyID.encode(KeyID.VK_LEFT));
            camc.setRightCode(KeyID.encode(KeyID.VK_RIGHT));
            camc.setZoomInCode(KeyID.encode(KeyID.VK_I));
            camc.setZoomOutCode(KeyID.encode(KeyID.VK_O));
        }
        catch(Throwable th) { th.printStackTrace(System.err); }
    }
}