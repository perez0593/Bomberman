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
final class PeripheralEventEntry
{
    final int code;
    final float value;
    char ch;
    
    PeripheralEventEntry(int code, float value, char ch)
    {
        this.code = code;
        this.value = value;
        this.ch = ch;
    }
    PeripheralEventEntry(int code, float value)
    {
        this(code,value,'\u0000');
    }
}
