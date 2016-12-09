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
public abstract class PeripheralID
{
    private int code;
    private final boolean invalid;
    
    PeripheralID()
    {
        code = -1;
        invalid = false;
    }
    private PeripheralID(int code)
    {
        invalid = true;
        this.code = code;
    }
    
    public final boolean isValid() { return invalid; }
    
    public final int getCode()
    {
        return code >= 0 ? code : (code = getCode0());
    }
    abstract int getCode0();
    public abstract int getIDType();
    abstract String toString0();
    public abstract String getName();
    
    @Override
    public final boolean equals(Object o)
    {
        return o != null && o instanceof PeripheralID &&
                getCode() == ((PeripheralID)o).getCode();
    }

    @Override
    public final int hashCode()
    {
        int hash = 5;
        hash = 83 * hash + getCode();
        return hash;
    }
    
    @Override
    public final String toString()
    {
        return "[code = " + getCode() + "] " + toString0();
    }
    
    static PeripheralID INVALID(int code)
    {
        final int negCode = code > 0 ? -code : code == 0 ? -1 : code;
        return new PeripheralID(negCode)
        {
            @Override
            int getCode0()
            {
                return negCode;
            }

            @Override
            public int getIDType()
            {
                return -1;
            }

            @Override
            String toString0()
            {
                return "INVALID";
            }

            @Override
            public String getName()
            {
                return "INVALID";
            }
        };
    }
    
    public static final int decodeType(int code)
    {
        return code & 0x3;
    }
    
    public static final PeripheralID decode(int code)
    {
        if(code < 0) return INVALID(code);
        switch(code & 0x3)
        {
            case PeripheralReference.GAMEPAD:
                break; //TODO GamePadInput
                //return GamePadInputID.decode0(code);
            case PeripheralReference.MOUSE:
                return MouseButtonID.decode0(code);
            case PeripheralReference.KEYBOARD:
                return KeyID.decode0(code);
        }
        return INVALID(-1);
    }
}
