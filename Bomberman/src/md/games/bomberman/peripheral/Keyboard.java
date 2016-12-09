/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author Marc
 */
public class Keyboard extends AbstractPeripheral implements KeyListener
{
    private final HashMap<Integer,PeripheralEventEntry> pressed;
    private final LinkedList<PeripheralEventEntry> entries;
    
    public Keyboard()
    {
        pressed = new HashMap<>();
        entries = new LinkedList<>();
    }
    
    private void pushKey(int key)
    {
        PeripheralEventEntry e = new PeripheralEventEntry(KeyID.getCode0(key),1f);
        if(pressed.put(key,e) == null)
            entries.add(e);
    }
    private void pushKey(int key, char c)
    {
        PeripheralEventEntry e = new PeripheralEventEntry(KeyID.getCode0(key),1f,c);
        PeripheralEventEntry old;
        if((old = pressed.put(key,e)) == null)
            entries.add(e);
        else if(old.ch == '\u0000' && old.code == e.code)
            old.ch = e.ch;
    }
    private void releaseKey(int key)
    {
        entries.add(new PeripheralEventEntry(KeyID.getCode0(key),0f));
        pressed.remove(key);
    }
    
    public boolean isKeyPressed(int code)
    {
        return pressed.containsKey(code);
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch(code)
        {
            case KeyEvent.VK_SHIFT:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    pushKey(KeyID.VK_SHIFT_LEFT,e.getKeyChar());
                else pushKey(KeyID.VK_SHIFT_RIGHT,e.getKeyChar());
            break;
            case KeyEvent.VK_CONTROL:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    pushKey(KeyID.VK_CONTROL_LEFT,e.getKeyChar());
                else pushKey(KeyID.VK_CONTROL_RIGHT,e.getKeyChar());
            break;
            default: pushKey(Integer.valueOf(code),e.getKeyChar());
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch(code)
        {
            case KeyEvent.VK_SHIFT:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    pushKey(KeyID.VK_SHIFT_LEFT);
                else pushKey(KeyID.VK_SHIFT_RIGHT);
            break;
            case KeyEvent.VK_CONTROL:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    pushKey(KeyID.VK_CONTROL_LEFT);
                else pushKey(KeyID.VK_CONTROL_RIGHT);
            break;
            default: pushKey(Integer.valueOf(code));
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int code = e.getKeyCode();
        switch(code)
        {
            case KeyEvent.VK_SHIFT:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    releaseKey(KeyID.VK_SHIFT_LEFT);
                else releaseKey(KeyID.VK_SHIFT_RIGHT);
            break;
            case KeyEvent.VK_CONTROL:
                if(e.getKeyLocation() == KeyID.KEY_LOCATION_LEFT)
                    releaseKey(KeyID.VK_CONTROL_LEFT);
                else releaseKey(KeyID.VK_CONTROL_RIGHT);
            break;
            default: releaseKey(Integer.valueOf(code));
        }
    }

    @Override
    protected final void buildControllerEvent(PeripheralEvent event, Collection<PeripheralListener> lists)
    {
        PeripheralEventEntry e;
        while(!entries.isEmpty())
        {
            e = entries.removeFirst();
            event.setProperties(e.code,-1,e.value,e.ch);
            if(e.value != 0f) for(PeripheralListener list : lists)
                list.abstractInputPressed(event);
            else for(PeripheralListener list : lists)
                list.abstractInputReleased(event);
        }
    }
    
}
