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
public final class PeripheralMaskEvent extends PeripheralEvent
{
    PeripheralMask mask;
    
    PeripheralMaskEvent()
    {
        super();
        mask = null;
    }
    
    final void setProperties(int code, int port, float value, char ch, PeripheralMask mask)
    {
        this.code = code;
        this.port = port;
        this.value = value;
        this.ch = ch;
        this.mask = mask;
    }
    
    public final PeripheralMask getMask() { return mask; }
    
    public final boolean isEqualsMask(PeripheralMask mask) { return this.mask == mask; }
    
    public final boolean isAnyEqualsMasks(PeripheralMask... masks)
    {
        for(PeripheralMask mask : masks)
            if(this.mask == mask)
                return true;
        return false;
    }
    
    public final PeripheralMask getMaskMatches(PeripheralMask... masks)
    {
        for(PeripheralMask mask : masks)
            if(this.mask == mask)
                return mask;
        return null;
    }
}
