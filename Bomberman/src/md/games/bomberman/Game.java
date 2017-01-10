/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman;

import java.awt.Graphics2D;
import md.games.bomberman.input.InputController;
import md.games.bomberman.peripheral.KeyID;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.ScenarioManager;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
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
        scenarioManager.update(delta);
    }

    @Override
    public void draw(Graphics2D g)
    {
        scenarioManager.draw(g);
    }

    @Override
    public void dispatchEvent(NTJGInputEvent event)
    {
        
    }

    @Override
    public void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        System.out.println(event.getID());
        
        int code = event.getCode();
        if(code == KeyID.encode(KeyID.VK_RIGHT))
            scenarioManager.getScenario().getCamera().traslate(10,0);
        else if(code == KeyID.encode(KeyID.VK_LEFT))
            scenarioManager.getScenario().getCamera().traslate(-10,0);
        else if(code == KeyID.encode(KeyID.VK_DOWN))
            scenarioManager.getScenario().getCamera().traslate(0,10);
        else if(code == KeyID.encode(KeyID.VK_UP))
            scenarioManager.getScenario().getCamera().traslate(0,-10);
    }
    
    
    final void startDebugScenario()
    {
        try
        {
            scenarioManager = new ScenarioManager();
            Scenario scenario = scenarioManager.createDebugScenario(30,30);
            scenarioManager.loadTheme("basic_theme");
            
            TileManager tiles = scenario.getTileManager();
            tiles.setSize(300,300);
            for(int r=0;r<tiles.getRows();r++)
                for(int c=0;c<tiles.getColumns();c++)
                {
                    Tile tile = tiles.getTile(r,c);
                    if(r == c)
                        tile.setSprite(scenario.getSpriteManager().getSprite("block1"));
                    else tile.setSprite(scenario.getSpriteManager().getSprite("tile1"));
                }
            
            scenario.getCamera().setPosition(0,0);
        }
        catch(Throwable th) { th.printStackTrace(System.err); }
    }
}
