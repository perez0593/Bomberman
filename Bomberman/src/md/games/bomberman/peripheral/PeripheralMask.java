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
public final class PeripheralMask implements Comparable<PeripheralMask>
{
    private static int __INTERNAL_ID = 0;
    private final int __id = __INTERNAL_ID++;
    
    public static PeripheralMask generate() { return new PeripheralMask(); }

    @Override
    public final int compareTo(PeripheralMask o)
    {
        return Integer.compare(__id,__id);
    }
    
    @Override
    public final boolean equals(Object o)
    {
        return this == o;
    }

    @Override
    public final int hashCode()
    {
        int hash = 5;
        hash = 59 * hash + this.__id;
        return hash;
    }
}
