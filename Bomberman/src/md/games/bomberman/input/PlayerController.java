/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.input;

import md.games.bomberman.input.InputMasks.Bind;
import md.games.bomberman.object.Player;
import md.games.bomberman.object.PlayerColor;
import md.games.bomberman.object.player.PlayerFeatures;
import md.games.bomberman.object.player.PlayerId;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.util.Direction;

/**
 *
 * @author Asus
 */
public final class PlayerController
{
    private final Scenario scenario;
    private final PlayerId playerId;
    private final String name;
    private final PlayerColor color;
    private PlayerFeatures features;
    private Player player;
    private boolean inDeathBorder;
    
    private int moveid = ID_MOVE_NONE;
    private int putbomb = ID_BOMB_NONE;
    
    public PlayerController(Scenario scenario, PlayerFeatures features, PlayerId playerId, String name, PlayerColor color)
    {
        if(scenario == null || features == null || playerId == null || name == null || color == null)
            throw new NullPointerException();
        this.scenario = scenario;
        this.playerId = playerId;
        this.name = name;
        this.color = color;
        this.features = features;
        this.inDeathBorder = false;
    }
    
    public final Player createPlayer()
    {
        if(player != null)
            throw new IllegalStateException();
        return player = features.createPlayer(name,color);
    }
    
    public final PlayerFeatures getPlayerFeatures() { return features; }
    
    public final void update(double delta)
    {
        updateMove(delta);
        updatePutBomb();
    }
    
    private void updateMove(double delta)
    {
        if(moveid == ID_MOVE_NONE)
            return;
        if(inDeathBorder)
        {
            scenario.getDeathBorder().movePlayer(player,SPEED_BASE * delta);
        }
        else
        {
            double moveAmount = SPEED_BASE;
            switch(moveid)
            {
                case ID_MOVE_NONE: player.stopWalk();
                case ID_MOVE_UP: player.stopWalk(true); player.walk(Direction.UP,moveAmount); break;
                case ID_MOVE_DOWN: player.stopWalk(true); player.walk(Direction.DOWN,moveAmount); break;
                case ID_MOVE_LEFT: player.stopWalk(false); player.walk(Direction.LEFT,moveAmount); break;
                case ID_MOVE_RIGHT: player.stopWalk(false); player.walk(Direction.RIGHT,moveAmount); break;
                case ID_MOVE_UP | ID_MOVE_LEFT: player.walk(Direction.UP,moveAmount); player.walk(Direction.LEFT,moveAmount); break;
                case ID_MOVE_UP | ID_MOVE_RIGHT: player.walk(Direction.UP,moveAmount); player.walk(Direction.RIGHT,moveAmount); break;
                case ID_MOVE_DOWN | ID_MOVE_LEFT: player.walk(Direction.DOWN,moveAmount); player.walk(Direction.LEFT,moveAmount); break;
                case ID_MOVE_DOWN | ID_MOVE_RIGHT: player.walk(Direction.DOWN,moveAmount); player.walk(Direction.RIGHT,moveAmount); break;
            }
        }
    }
    
    private void updatePutBomb()
    {
        if(putbomb == ID_BOMB_NONE || putbomb == ID_BOMB_COOLDOWN || player == null)
            return;
        player.putBomb(putbomb == ID_BOMB_PUT_PRIMARY);
        putbomb = ID_BOMB_COOLDOWN;
    }
    
    public final void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        if(event.isPressed())
            pressed(event);
        else released(event);
    }
    
    private void pressed(PeripheralMaskEvent event)
    {
        Bind binded = InputMasks.matchAny(playerId,event.getMask(),Bind.MOVE_UP,Bind.MOVE_DOWN,Bind.MOVE_LEFT,Bind.MOVE_RIGHT);
        if(binded != null) switch(binded)
        {
            case MOVE_UP:
                moveid |= ID_MOVE_UP;
                moveid &= ~ID_MOVE_DOWN;
                break;
            case MOVE_DOWN:
                moveid |= ID_MOVE_DOWN;
                moveid &= ~ID_MOVE_UP;
                break;
            case MOVE_LEFT:
                moveid |= ID_MOVE_LEFT;
                moveid &= ~ID_MOVE_RIGHT;
                break;
            case MOVE_RIGHT:
                moveid |= ID_MOVE_RIGHT;
                moveid &= ~ID_MOVE_LEFT;
                break;
        }
        else if(InputMasks.match(playerId,Bind.PUT_BOMB,event.getMask()))
        {
            if(putbomb == ID_BOMB_NONE)
                putbomb = ID_BOMB_PUT_PRIMARY;
        }
    }
    
    private void released(PeripheralMaskEvent event)
    {
        Bind binded = InputMasks.matchAny(playerId,event.getMask(),Bind.MOVE_UP,Bind.MOVE_DOWN,Bind.MOVE_LEFT,Bind.MOVE_RIGHT);
        if(binded != null) switch(binded)
        {
            case MOVE_UP: moveid &= ~ID_MOVE_UP; break;
            case MOVE_DOWN: moveid &= ~ID_MOVE_DOWN; break;
            case MOVE_LEFT: moveid &= ~ID_MOVE_LEFT; break;
            case MOVE_RIGHT: moveid &= ~ID_MOVE_RIGHT; break;
        }
        if(InputMasks.match(playerId,Bind.PUT_BOMB,event.getMask()) && putbomb == ID_BOMB_COOLDOWN)
            putbomb = ID_BOMB_NONE;
    }
    
    
    
    private static final int ID_MOVE_NONE = 0;
    private static final int ID_MOVE_UP = 0x1;
    private static final int ID_MOVE_DOWN = 0x2;
    private static final int ID_MOVE_LEFT = 0x4;
    private static final int ID_MOVE_RIGHT = 0x8;
    
    private static final int ID_BOMB_NONE = 0;
    private static final int ID_BOMB_PUT_PRIMARY = 1;
    private static final int ID_BOMB_PUT_SECONDARY = 2;
    private static final int ID_BOMB_COOLDOWN = 3;
    
    private static final double SPEED_BASE = 100d;
}
