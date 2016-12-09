/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import net.java.games.input.Component;

/**
 *
 * @author Marc
 */
public enum PovDirection
{
    NONE(Component.POV.OFF,-1),
    UP(Component.POV.UP,2),
    DOWN(Component.POV.DOWN,6),
    LEFT(Component.POV.LEFT,4),
    RIGHT(Component.POV.RIGHT,0),
    UP_LEFT(Component.POV.UP_LEFT,3),
    UP_RIGHT(Component.POV.UP_RIGHT,1),
    DOWN_LEFT(Component.POV.DOWN_LEFT,5),
    DOWN_RIGHT(Component.POV.DOWN_RIGHT,7);
    
    private final float dir;
    private final int dircode;
    
    private PovDirection(final float direction, final int code)
    {
        dir = direction;
        dircode = code;
    }
    
    public final int dirCode() { return dircode; }
    
    public float floatValue() { return dir; }
    
    private static final PovDirection[] valids;
    static
    {
        valids = new PovDirection[values().length-1];
        System.arraycopy(values(),1,valids,0,valids.length);
    }
    
    public static final PovDirection[] validDirs()
    {
        return valids;
    }
    
    public static int cast(PovDirection dir)
    {
        switch(dir)
        {
            default: return 4;
            case UP_LEFT: return 0;
            case UP: return 1;
            case UP_RIGHT: return 2;
            case LEFT: return 3;
            case RIGHT: return 5;
            case DOWN_LEFT: return 6;
            case DOWN: return 7;
            case DOWN_RIGHT: return 8;
        }
    }
    
    public static PovDirection cast(int intValue)
    {
        switch(intValue)
        {
            default: return NONE;
            case 0: return UP_LEFT;
            case 1: return UP;
            case 2: return UP_RIGHT;
            case 3: return LEFT;
            case 5: return RIGHT;
            case 6: return DOWN_LEFT;
            case 7: return DOWN;
            case 8: return DOWN_RIGHT;
        }
    }
    
    public static PovDirection cast(float floatValue)
    {
        if(floatValue == Component.POV.OFF) return NONE;
        if(floatValue == Component.POV.UP) return UP;
        if(floatValue == Component.POV.DOWN) return DOWN;
        if(floatValue == Component.POV.LEFT) return LEFT;
        if(floatValue == Component.POV.RIGHT) return RIGHT;
        if(floatValue == Component.POV.UP_LEFT) return UP_LEFT;
        if(floatValue == Component.POV.UP_RIGHT) return UP_RIGHT;
        if(floatValue == Component.POV.DOWN_LEFT) return DOWN_LEFT;
        if(floatValue == Component.POV.DOWN_RIGHT) return DOWN_RIGHT;
        return NONE;
    }
}
