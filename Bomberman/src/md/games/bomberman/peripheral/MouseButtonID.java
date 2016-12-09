/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.peripheral;

/**
 *
 * @author Marc
 */
public class MouseButtonID extends PeripheralID
{
    private final Mouse.Button button;
    
    private MouseButtonID(Mouse.Button button)
    {
        this.button = button;
    }

    @Override
    int getCode0()
    {
        return PeripheralReference.MOUSE + (button.value << 2);
    }

    @Override
    public int getIDType()
    {
        return PeripheralReference.MOUSE;
    }

    @Override
    String toString0()
    {
        return "MOUSE " + button.name();
    }

    @Override
    public String getName()
    {
        return button.name();
    }
    
    public static final MouseButtonID
            BUTTON1 = new MouseButtonID(Mouse.Button.BUTTON1),
            BUTTON2 = new MouseButtonID(Mouse.Button.BUTTON2),
            BUTTON3 = new MouseButtonID(Mouse.Button.BUTTON3);
    
    static final MouseButtonID decode0(int code)
    {
        int id = code >>> 2;
        if(id == Mouse.Button.BUTTON1.value)
            return BUTTON1;
        if(id == Mouse.Button.BUTTON2.value)
            return BUTTON2;
        if(id == Mouse.Button.BUTTON3.value)
            return BUTTON3;
        return null;
    }
    
    public static final PeripheralID getID(int buttoncode)
    {
        if(buttoncode == Mouse.Button.BUTTON1.value)
            return BUTTON1;
        if(buttoncode == Mouse.Button.BUTTON2.value)
            return BUTTON2;
        if(buttoncode == Mouse.Button.BUTTON3.value)
            return BUTTON3;
        return PeripheralID.INVALID(-1);
    }
    
    static final int getCode0(Mouse.Button but)
    {
        return PeripheralReference.MOUSE + (but.value << 2);
    }
    
    public static final int encode(Mouse.Button but) { return getCode0(but); }
}
