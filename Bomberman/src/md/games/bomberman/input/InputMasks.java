/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.input;

import java.lang.reflect.Field;
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
}
