/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import net.java.games.input.Controller;
import net.java.games.input.DirectInputEnvironmentPlugin;

/**
 *
 * @author Marc
 */
public final class GamePadHandler extends AbstractPeripheral
{
    private final HashMap<Integer,GamePad> pads;
    
    static
    {
        System.setProperty("net.java.games.input.librarypath",new File("natives").getAbsolutePath());
    }
    
    public GamePadHandler()
    {
        pads = new HashMap<>();
        findGamePads();
    }
    
    public final void findGamePads()
    {
        if(!pads.isEmpty())
        {
            LinkedList<Integer> bads = new LinkedList<>();
            for(Entry<Integer,GamePad> pad : pads.entrySet())
            {
                if(!pad.getValue().poll())
                    bads.add(pad.getKey());
            }
            for(Integer pad : bads)
                pads.remove(pad);
        }
        DirectInputEnvironmentPlugin environment = new DirectInputEnvironmentPlugin();
        int port = -1;
        GamePad pad;
        for(Controller c : environment.getControllers())
        {
            port++;
            if(port > 31) break;
            if(!GamePad.isGamePad(c)) continue;
            if((pad = pads.get(port)) != null && pad.poll()) continue;
            pad = new GamePad(c,port);
            pads.put(port,pad);
        }
    }
    
    public final int size() { return pads.size(); }
    
    public final GamePad getGamePad(int index) { return pads.get(index); }

    @Override
    protected void buildControllerEvent(PeripheralEvent event, Collection<PeripheralListener> lists)
    {
        for(GamePad pad : pads.values())
            pad.buildControllerEvent(event,lists);
    }
    
    
    private final class Semaphore
    {
        private volatile boolean flag;
        
        private Semaphore()
        {
            flag = false;
        }
        
        public synchronized final void pass()
        {
            
        }
    }
}
