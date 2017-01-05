/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.input;

import md.games.bomberman.peripheral.GamePadHandler;
import md.games.bomberman.peripheral.Keyboard;
import md.games.bomberman.peripheral.Mouse;
import md.games.bomberman.peripheral.PeripheralController;
import md.games.bomberman.peripheral.PeripheralID;
import md.games.bomberman.peripheral.PeripheralMask;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import md.games.bomberman.peripheral.PeripheralMaskingManager;
import nt.ntjg.NTJG;

/**
 *
 * @author Asus
 */
public final class InputController
{
    private static final PeripheralController CONTROLLER = new PeripheralController();
    private static final PeripheralMaskingManager MANAGER = new PeripheralMaskingManager(CONTROLLER);
    
    private static final GamePadHandler PADS = new GamePadHandler();
    private static final Keyboard KEYBOARD = new Keyboard();
    private static final Mouse MOUSE = new Mouse();
    
    private static boolean INITIATED = false;
    
    public static final void initController()
    {
        if(INITIATED)
            return;
        INITIATED = true;
        
        CONTROLLER.addAbstractController(PADS);
        CONTROLLER.addAbstractController(KEYBOARD);
        CONTROLLER.addAbstractController(MOUSE);
        
        NTJG.ntjgAddKeyboardListener(KEYBOARD);
        NTJG.ntjgAddMouseListener(MOUSE);
        
        InputMasks.setInManager(MANAGER);
    }
    
    public static final void update()
    {
        MANAGER.update();
    }
    
    public static final void findGamePads()
    {
        PADS.findGamePads();
    }
    
    public static final int getGamePadCount() { return PADS.size(); }
    
    public static final void registerMask(PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.registerMask(mask);
    }
    
    public static final void unregisterMask(PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.unregisterMask(mask);
        MANAGER.assignPeripheralIdToMask(0, mask);
    }
    
    public static final void assignPeripheralIdToMask(int code, PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.assignPeripheralIdToMask(code,mask);
    }
    public static final void assignPeripheralIdToMask(PeripheralID id, PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.assignPeripheralIdToMask(id.getCode(),mask);
    }
    
    public static final void unassignPeripheralIdToMask(int code, PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.unassignPeripheralIdToMask(code,mask);
    }
    public static final void unassignPeripheralIdToMask(PeripheralID id, PeripheralMask mask)
    {
        if(mask == null)
            throw new NullPointerException();
        MANAGER.unassignPeripheralIdToMask(id.getCode(),mask);
    }
    
    public static final void addListener(PeripheralMaskingListener listener)
    {
        if(listener == null)
            throw new NullPointerException();
        MANAGER.addListener(listener);
    }
    
    public static final void removeListener(PeripheralMaskingListener listener)
    {
        if(listener == null)
            throw new NullPointerException();
        MANAGER.removeListener(listener);
    }
}
