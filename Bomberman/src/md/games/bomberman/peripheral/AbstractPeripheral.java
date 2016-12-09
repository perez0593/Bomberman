/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.util.Collection;

/**
 *
 * @author Marc
 */
public abstract class AbstractPeripheral
{
    protected abstract void buildControllerEvent(PeripheralEvent event, Collection<PeripheralListener> lists);
}
