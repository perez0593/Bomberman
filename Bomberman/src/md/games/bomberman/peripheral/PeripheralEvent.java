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
public class PeripheralEvent
{
    int code, port;
    char ch;
    float value;
    
    PeripheralEvent()
    {
        code = port = -1;
        ch = '\u0000';
    }
    
    final void setProperties(int code, int port, float value, char ch)
    {
        this.code = code;
        this.port = port;
        this.value = value;
        this.ch = ch;
    }
    
    public final boolean containsValidChar() { return ch != '\u0000'; }
    
    public final int getCode() { return code; }
    public final int getPort() { return port; }
    public final float getValue() { return value; }
    public final char getChar() { return ch; }
    public final PeripheralID getID()
    {
        return PeripheralID.decode(code);
    }
    public final int getIDType() { return PeripheralID.decodeType(code); }
    public final boolean isPressed() { return value != 0f; }
}
