/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;
import net.java.games.input.Component.Identifier;

/**
 *
 * @author Marc
 */
final class GamePadInputID extends PeripheralID
{
    static final int BUTTON = 0, AXIS = 1;
    
    final Identifier componentID;
    final String name;
    final int port;
    final int type;
    final int flag;
    
    private GamePadInputID(int type, int port, int flag, Identifier id)
    {
        super();
        this.type = type;
        this.port = port;
        this.flag = flag;
        this.componentID = id;
        name = "GAME_" + port + "_" + flag + "_" + componentID.getName();
    }

    @Override
    final int getCode0()
    {
        return (PeripheralReference.GAMEPAD + ((port & 0x1F) << 2) + ((type & 0x1) << 7) +
                 ((flag & 0x7) << 8)) + ((dic.get(componentID) & 0xFFFF) << 11);
    }
    
    @Override
    final String toString0()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(componentID.getName()).append(": ").append(getCode());
        return sb.toString();
    }
    
    @Override
    public final int getIDType()
    {
        return PeripheralReference.GAMEPAD;
    }
    
    @Override
    public final String getName()
    {
        return name;
    }
    
    static final GamePadInputID decode0(int code)
    {
        return new GamePadInputID(((code >>> 7) & 0x1),
            ((code >>> 2) & 0x1F),((code >>> 8) & 0x7),
            dic_inv.get(((code >>> 11) & 0xFFFF)));
    }
    
    static final GamePadInputID createAxisID(int port, Identifier id, boolean positive)
    {
        return new GamePadInputID(AXIS,port,positive?0:1,id);
    }
    
    static final GamePadInputID createPovID(int port, PovDirection dir)
    {
        if(dir == PovDirection.NONE)
            throw new IllegalArgumentException("Expected valid pov direction");
        return new GamePadInputID(AXIS,port,dir.dirCode(),Identifier.Axis.POV);
    }
    
    static final GamePadInputID createButtonID(int port, Identifier id)
    {
        return new GamePadInputID(BUTTON,port,0,id);
    }
    
    
    
    
    
    
    private static final HashMap<Identifier,Integer> dic = new HashMap<>();
    private static final HashMap<Integer,Identifier> dic_inv = new HashMap<>();
    
    static final void checkAllPorts()
    {
        TreeSet<Integer> tree = new TreeSet<>();
        GamePadInputID id;
        for(int port=0;port<32;port++)
        {
            for(Identifier iden : dic.keySet())
            {
                if(iden instanceof Identifier.Axis)
                {
                    if(iden.equals(Identifier.Axis.POV))
                    {
                        for(PovDirection dir : PovDirection.validDirs())
                        {
                            id = createPovID(port,dir);
                            if(tree.contains(id.getCode()))
                                throw new IllegalStateException("Repeat code: " + id.getCode());
                            tree.add(id.getCode());
                        }
                        continue;
                    }
                    id = createAxisID(port,iden,true);
                    if(tree.contains(id.getCode()))
                            throw new IllegalStateException("Repeat code: " + id.getCode());
                        tree.add(id.getCode());
                    id = createAxisID(port,iden,false);
                    if(tree.contains(id.getCode()))
                            throw new IllegalStateException("Repeat code: " + id.getCode());
                        tree.add(id.getCode());
                    continue;
                }
                id = createButtonID(port,iden);
                if(tree.contains(id.getCode()))
                            throw new IllegalStateException("Repeat code: " + id.getCode());
                        tree.add(id.getCode());
            }
        }
        System.out.println("All possible codes:");
        for(Integer i : tree)
            System.out.println(i);
    }
    
    static final String allCodes(int port)
    {
        StringBuilder sb = new StringBuilder();
        TreeSet<Integer> tree = new TreeSet<>();
        GamePadInputID id;
        for(Identifier iden : dic.keySet())
        {
            if(iden instanceof Identifier.Axis)
            {
                if(iden.equals(Identifier.Axis.POV))
                {
                    for(PovDirection dir : PovDirection.validDirs())
                    {
                        id = createPovID(port,dir);
                        sb.append(id).append("\n");
                        if(tree.contains(id.getCode()))
                            throw new IllegalStateException("Repeat code: " + id.getCode());
                        tree.add(id.getCode());
                    }
                    continue;
                }
                id = createAxisID(port,iden,true);
                sb.append(id).append("\n");
                if(tree.contains(id.getCode()))
                        throw new IllegalStateException("Repeat code: " + id.getCode());
                    tree.add(id.getCode());
                id = createAxisID(port,iden,false);
                sb.append(id).append("\n");
                if(tree.contains(id.getCode()))
                        throw new IllegalStateException("Repeat code: " + id.getCode());
                    tree.add(id.getCode());
                continue;
            }
            id = createButtonID(port,iden);
            sb.append(id).append("\n");
            if(tree.contains(id.getCode()))
                        throw new IllegalStateException("Repeat code: " + id.getCode());
                    tree.add(id.getCode());
        }
        System.out.println(tree);
        return sb.substring(0,sb.length()-1);
    }
    
    static
    {
        TreeMap<String,Identifier> tree = new TreeMap<>();
        Identifier id;
        Object field;
        Class cl;
        try
        {
            cl = Identifier.Axis.class;
            for(Field f : cl.getDeclaredFields())
            {
                if((field = f.get(null)) instanceof Identifier.Axis)
                {
                    id = (Identifier.Axis) field;
                    if(id.equals(Identifier.Axis.POV))
                        tree.put("POV " + id.getName(),id);
                    else tree.put("AXIS " + id.getName(),id);
                }
            }
            cl = Identifier.Button.class;
            for(Field f : cl.getDeclaredFields())
            {
                if((field = f.get(null)) instanceof Identifier.Button)
                {
                    id = (Identifier.Button) field;
                    tree.put("BUTTON " + id.getName(),id);
                }
            }
            
            int count = 0;
            for(Identifier idef : tree.values())
            {
                //System.out.println(idef.getName());
                dic_inv.put(count,idef);
                dic.put(idef,count++);
            }
        }
        catch(IllegalAccessException | IllegalArgumentException | SecurityException ex)
        {
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
