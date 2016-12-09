/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author Marc
 */
public final class Mouse extends AbstractPeripheral implements MouseListener
{
    public final Point pos;
    public boolean but1, but2, but3;
    private final LinkedList<PeripheralEventEntry> entries;
    
    public enum Button
    {
        BUTTON1(MouseEvent.BUTTON1),
        BUTTON2(MouseEvent.BUTTON2),
        BUTTON3(MouseEvent.BUTTON3);
        
        public final int value;
        
        private Button(final int value)
        {
            this.value = value;
        }
    }
    
    public Mouse()
    {
        entries = new LinkedList<>();
        pos = new Point();
        but1 = false;
        but2 = false;
        but3 = false;
    }
    
    public boolean isPressedBut1() { return but1; }
    public boolean isPressedBut2() { return but2; }
    public boolean isPressedBut3() { return but3; }
    

    @Override
    public void mouseClicked(MouseEvent e)
    {
        
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                if(but1) break;
                but1 = true;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON1),1f));
                break;
            case MouseEvent.BUTTON2:
                if(but2) break;
                but2 = true;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON2),1f));
                break;
            case MouseEvent.BUTTON3:
                if(but3) break;
                but3 = true;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON3),1f));
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        switch(e.getButton())
        {
            case MouseEvent.BUTTON1:
                if(!but1) break;
                but1 = false;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON1),0f));
                break;
            case MouseEvent.BUTTON2:
                if(!but2) break;
                but2 = false;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON2),0f));
                break;
            case MouseEvent.BUTTON3:
                if(!but3) break;
                but3 = false;
                entries.add(new PeripheralEventEntry(MouseButtonID.getCode0(Button.BUTTON3),0f));
                break;
        }
    }
    
    public boolean isInto(Rectangle bounds)
    {
        return bounds.contains(pos);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        
    }
    
    @Override
    protected void buildControllerEvent(PeripheralEvent event, Collection<PeripheralListener> lists)
    {
        PeripheralEventEntry e;
        while(!entries.isEmpty())
        {
            e = entries.removeFirst();
            event.setProperties(e.code,-1,e.value,'\u0000');
            if(e.value != 0f) for(PeripheralListener list : lists)
                list.abstractInputPressed(event);
            else for(PeripheralListener list : lists)
                list.abstractInputReleased(event);
        }
    }
}
