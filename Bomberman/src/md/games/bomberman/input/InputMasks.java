/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.input;

import java.lang.reflect.Field;
import md.games.bomberman.creature.player.PlayerId;
import md.games.bomberman.peripheral.PeripheralMask;
import md.games.bomberman.peripheral.PeripheralMaskingManager;

/**
 *
 * @author Asus
 */
public final class InputMasks
{
    private InputMasks() {}
    
    public static final PeripheralMask P1_MOVE_UP = PeripheralMask.generate();
    public static final PeripheralMask P1_MOVE_DOWN = PeripheralMask.generate();
    public static final PeripheralMask P1_MOVE_LEFT = PeripheralMask.generate();
    public static final PeripheralMask P1_MOVE_RIGHT = PeripheralMask.generate();
    public static final PeripheralMask P1_PUT_BOMB = PeripheralMask.generate();
    
    public static final PeripheralMask P2_MOVE_UP = PeripheralMask.generate();
    public static final PeripheralMask P2_MOVE_DOWN = PeripheralMask.generate();
    public static final PeripheralMask P2_MOVE_LEFT = PeripheralMask.generate();
    public static final PeripheralMask P2_MOVE_RIGHT = PeripheralMask.generate();
    public static final PeripheralMask P2_PUT_BOMB = PeripheralMask.generate();
    
    public static final PeripheralMask P3_MOVE_UP = PeripheralMask.generate();
    public static final PeripheralMask P3_MOVE_DOWN = PeripheralMask.generate();
    public static final PeripheralMask P3_MOVE_LEFT = PeripheralMask.generate();
    public static final PeripheralMask P3_MOVE_RIGHT = PeripheralMask.generate();
    public static final PeripheralMask P3_PUT_BOMB = PeripheralMask.generate();
    
    public static final PeripheralMask P4_MOVE_UP = PeripheralMask.generate();
    public static final PeripheralMask P4_MOVE_DOWN = PeripheralMask.generate();
    public static final PeripheralMask P4_MOVE_LEFT = PeripheralMask.generate();
    public static final PeripheralMask P4_MOVE_RIGHT = PeripheralMask.generate();
    public static final PeripheralMask P4_PUT_BOMB = PeripheralMask.generate();
    
    
    public static final void setInManager(PeripheralMaskingManager manager)
    {
        for(Field field : InputMasks.class.getDeclaredFields())
        {
            try
            {
                PeripheralMask mask = (PeripheralMask) field.get(null);
                manager.registerMask(mask);
            }
            catch(IllegalArgumentException | IllegalAccessException ex)
            {
                throw new IllegalStateException(ex);
            }
        }
    }
    
    
    public static final PeripheralMask bind(PlayerId playerId, Bind bindedMask)
    {
        switch(bindedMask)
        {
            case MOVE_UP: switch(playerId) {
                case ONE: return P1_MOVE_UP;
                case TWO: return P2_MOVE_UP;
                case THREE: return P3_MOVE_UP;
                case FOUR: return P4_MOVE_UP;
            } break;
            case MOVE_DOWN: switch(playerId) {
                case ONE: return P1_MOVE_DOWN;
                case TWO: return P2_MOVE_DOWN;
                case THREE: return P3_MOVE_DOWN;
                case FOUR: return P4_MOVE_DOWN;
            } break;
            case MOVE_LEFT: switch(playerId) {
                case ONE: return P1_MOVE_LEFT;
                case TWO: return P2_MOVE_LEFT;
                case THREE: return P3_MOVE_LEFT;
                case FOUR: return P4_MOVE_LEFT;
            } break;
            case MOVE_RIGHT: switch(playerId) {
                case ONE: return P1_MOVE_RIGHT;
                case TWO: return P2_MOVE_RIGHT;
                case THREE: return P3_MOVE_RIGHT;
                case FOUR: return P4_MOVE_RIGHT;
            } break;
            case PUT_BOMB: switch(playerId) {
                case ONE: return P1_PUT_BOMB;
                case TWO: return P2_PUT_BOMB;
                case THREE: return P3_PUT_BOMB;
                case FOUR: return P4_PUT_BOMB;
            } break;
        }
        throw new IllegalStateException();
    }
    
    public static final boolean match(PlayerId playerId, Bind bindedMask, PeripheralMask mask)
    {
        return bind(playerId,bindedMask) == mask;
    }
    
    public static final Bind matchAny(PlayerId playerId, PeripheralMask mask, Bind... bindedMasks)
    {
        for(Bind bindedMask : bindedMasks)
            if(bind(playerId,bindedMask) == mask)
                return bindedMask;
        return null;
    }
    
    
    public enum Bind
    {
        MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, PUT_BOMB;
    }
}
