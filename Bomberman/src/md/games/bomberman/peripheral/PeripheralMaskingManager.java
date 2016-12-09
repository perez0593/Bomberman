/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.peripheral;

import java.util.HashMap;
import java.util.Objects;
import md.games.bomberman.util.SimpleArrayList;

/**
 *
 * @author Marc
 */
public class PeripheralMaskingManager
{
    private final PeripheralController controller;
    private final HashMap<PeripheralMask,SimpleArrayList<Integer>> maskBinds;
    private final HashMap<Integer,SimpleArrayList<PeripheralMask>> idBinds;
    private final SimpleArrayList<PeripheralMaskingListener> listeners;
    
    public PeripheralMaskingManager(PeripheralController controller)
    {
        this.controller = Objects.requireNonNull(controller);
        maskBinds = new HashMap<>();
        idBinds = new HashMap<>();
        listeners = new SimpleArrayList<>(new PeripheralMaskingListener[4]);
        insertListeners();
    }
    
    public final void addListener(PeripheralMaskingListener listener)
    {
        listeners.add(listener);
    }
    
    public final void removeListener(PeripheralMaskingListener listener)
    {
        listeners.remove(listener);
    }
    
    public final void registerMask(PeripheralMask mask)
    {
        if(maskBinds.containsKey(mask))
            throw new IllegalArgumentException("This mask has already exists");
        maskBinds.put(mask,new SimpleArrayList<>(new Integer[2]));
    }
    public final void registerMasks(PeripheralMask... masks)
    {
        for(PeripheralMask mask : masks)
            registerMask(mask);
    }
    
    public final void unregisterMask(PeripheralMask mask)
    {
        SimpleArrayList<Integer> ids = maskBinds.get(mask);
        if(ids == null)
            return;
        for(Integer id : ids)
        {
            SimpleArrayList<PeripheralMask> masks = idBinds.get(id);
            if(masks == null)
                continue;
            masks.remove(mask);
        }
        ids.clear();
    }
    
    public final void assignPeripheralIdToMask(int code, PeripheralMask mask)
    {
        SimpleArrayList<Integer> ids = maskBinds.get(mask);
        if(ids == null)
            throw new IllegalArgumentException("This mask does not exists");
        SimpleArrayList<PeripheralMask> masks = idBinds.get(code);
        if(masks == null)
            idBinds.put(code,masks = new SimpleArrayList<>(new PeripheralMask[2]));
        ids.add(code);
        masks.add(mask);
    }
    
    public final void unassignPeripheralIdToMask(int code, PeripheralMask mask)
    {
        SimpleArrayList<Integer> ids = maskBinds.get(mask);
        if(ids == null)
            throw new IllegalArgumentException("This mask does not exists");
        SimpleArrayList<PeripheralMask> masks = idBinds.get(code);
        if(masks == null)
            throw new IllegalArgumentException("This code does not exists");
        ids.remove(code);
        masks.remove(mask);
        if(masks.isEmpty())
            idBinds.remove(code);
    }
    
    private void insertListeners()
    {
        controller.addListener(new PeripheralListener()
        {
            @Override
            public final void abstractInputPressed(PeripheralEvent nevent)
            {
                PeripheralMaskEvent event = (PeripheralMaskEvent) nevent;
                SimpleArrayList<PeripheralMask> masks = idBinds.get(event.code);
                if(masks == null || masks.isEmpty())
                {
                    for(PeripheralMaskingListener listener : listeners)
                        listener.dispatchMaskedEvent(event);
                    return;
                }
                for(PeripheralMask mask : masks)
                {
                    event.mask = mask;
                    for(PeripheralMaskingListener listener : listeners)
                        listener.dispatchMaskedEvent(event);
                }
            }

            @Override
            public final void abstractInputReleased(PeripheralEvent nevent)
            {
                PeripheralMaskEvent event = (PeripheralMaskEvent) nevent;
                SimpleArrayList<PeripheralMask> masks = idBinds.get(event.code);
                if(masks == null || masks.isEmpty())
                {
                    for(PeripheralMaskingListener listener : listeners)
                        listener.dispatchMaskedEvent(event);
                    return;
                }
                for(PeripheralMask mask : masks)
                {
                    event.mask = mask;
                    for(PeripheralMaskingListener listener : listeners)
                        listener.dispatchMaskedEvent(event);
                }
            }
        });
    }
    
    public final void update()
    {
        controller.update(new PeripheralMaskEvent());
    }
}
