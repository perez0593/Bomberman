/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.util.LinkedList;

/**
 *
 * @author Marc
 */
public class PeripheralController
{
    protected LinkedList<PeripheralListener> lists;
    protected LinkedList<AbstractPeripheral> devices;
    
    public PeripheralController()
    {
        lists = new LinkedList<>();
        devices = new LinkedList<>();
    }
    
    public void addListener(PeripheralListener listener)
    {
        lists.add(listener);
    }
    public void removeListener(int index)
    {
        lists.remove(index);
    }
    
    public void addAbstractController(AbstractPeripheral device)
    {
        devices.add(device);
    }
    public void removeAbstractController(int index)
    {
        devices.remove(index);
    }
    
    final void update(PeripheralEvent event)
    {
        for(AbstractPeripheral device : devices)
            device.buildControllerEvent(event,lists);
    }
    
    public final void update()
    {
        PeripheralEvent event = new PeripheralEvent();
        for(AbstractPeripheral device : devices)
            device.buildControllerEvent(event,lists);
    }
}
