/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman;

import java.awt.Graphics2D;
import md.games.bomberman.input.InputController;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import nt.ntjg.NTJGFunctionalities;
import nt.ntjg.NTJGInputEvent;

/**
 *
 * @author Asus
 */
public final class Game
         implements NTJGFunctionalities, PeripheralMaskingListener
{
    @Override
    public void init()
    {
        InputController.initController();
        InputController.addListener(this);
    }

    @Override
    public void update(double delta)
    {
        InputController.update();
    }

    @Override
    public void draw(Graphics2D g)
    {
        
    }

    @Override
    public void dispatchEvent(NTJGInputEvent event)
    {
        
    }

    @Override
    public void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        
    }
    
}
