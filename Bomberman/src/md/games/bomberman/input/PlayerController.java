/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.input;

import md.games.bomberman.object.Player;
import md.games.bomberman.object.PlayerColor;
import md.games.bomberman.object.player.PlayerFeatures;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.action.ActionSender;

/**
 *
 * @author Asus
 */
public final class PlayerController
{
    private final Scenario scenario;
    private PlayerFeatures features;
    
    private int moveid = ID_MOVE_NONE;
    private int putbomb = ID_BOMB_NONE;
    
    public PlayerController(Scenario scenario, PlayerFeatures features)
    {
        if(scenario == null || features == null)
            throw new NullPointerException();
        this.scenario = scenario;
        this.features = features;
    }
    
    public final Player createPlayer(String name, PlayerColor color)
    {
        return features.createPlayer(name,color);
    }
    
    public final PlayerFeatures getPlayerFeatures() { return features; }
    
    public final void update(double delta)
    {
        
    }
    
    public final void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        
    }
    
    
    
    private static final int ID_MOVE_NONE = 0;
    private static final int ID_MOVE_UP = 1;
    private static final int ID_MOVE_DOWN = 2;
    private static final int ID_MOVE_LEFT = 3;
    private static final int ID_MOVE_RIGHT = 1;
    
    private static final int ID_BOMB_NONE = 0;
    private static final int ID_BOMB_PUT = 1;
    private static final int ID_BOMB_COOLDOWN = 2;
}
