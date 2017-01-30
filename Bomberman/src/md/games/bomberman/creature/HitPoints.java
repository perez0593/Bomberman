/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.creature;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public final class HitPoints extends LPLObject
{
    private int containers;
    private int redHearts;
    private int blueHearts;
    
    public HitPoints(int containers)
    {
        if(containers < 0)
            throw new IllegalArgumentException();
        this.containers = containers;
        this.redHearts = containers;
        blueHearts = 0;
    }
    
    private HitPoints(HitPoints other)
    {
        containers = other.containers;
        redHearts = other.redHearts;
        blueHearts = other.blueHearts;
    }
    
    public final HitPoints copy() { return new HitPoints(this); }
    public final void set(HitPoints other)
    {
        containers = other.containers;
        redHearts = other.redHearts;
        blueHearts = other.blueHearts;
    }
    
    public final void setContainers(int containers)
    {
        if(containers < 0)
            throw new IllegalArgumentException();
        this.containers = containers;
    }
    
    public final void setRedHearts(int hearts)
    {
        redHearts = hearts < 0 ? 0 : hearts > containers ? containers : hearts;
    }
    
    public final void setBlueHearts(int hearts)
    {
        blueHearts = hearts < 0 ? 0 : hearts;
    }
    
    public final int getContainers() { return containers; }
    public final int getRedHearts() { return redHearts; }
    public final int getBlueHearts() { return blueHearts; }
    public final int getCurrentHitPoints() { return redHearts + blueHearts; }
    public final boolean isAlive() { return getCurrentHitPoints() > 0; }
    
    
    private int blueDamage(int amount)
    {
        if(amount > blueHearts)
        {
            amount -= blueHearts;
            blueHearts = 0;
            return amount;
        }
        blueHearts -= amount;
        return 0;
    }
    
    public final void damageOnlyRed(int amount)
    {
        if(amount > redHearts)
        {
            amount -= redHearts;
            redHearts = 0;
            if(isAlive())
                damage(amount);
        }
        redHearts -= amount;
    }
    
    public final void damage(int amount)
    {
        amount = blueDamage(amount);
        if(amount <= 0 || !isAlive())
            return;
        redHearts -= amount;
        if(redHearts < 0)
            redHearts = 0;
    }
    
    public final void kill()
    {
        blueHearts = redHearts = 0;
    }
    
    public final void heal(int amount)
    {
        setRedHearts(redHearts + amount);
    }
    
    public final void giveBlueHeart()
    {
        blueHearts++;
    }

    public final void serialize(DataOutputStream dos) throws IOException
    {
        dos.writeInt(containers);
        dos.writeInt(redHearts);
        dos.writeInt(blueHearts);
    }

    public final void unserialize(DataInputStream dis) throws IOException
    {
        containers = dis.readInt();
        redHearts = dis.readInt();
        blueHearts = dis.readInt();
    }
    
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "setContainers": return SET_CONTAINERS;
            case "setRedHearts": return SET_RED_HEARTS;
            case "setBlueHearts": return SET_BLUE_HEARTS;
            case "getContainers": return GET_CONTAINERS;
            case "getRedHearts": return GET_RED_HEARTS;
            case "getBlueHearts": return GET_BLUE_HEARTS;
            case "getCurrentHitPoints": return GET_CURRENT_HIT_POINTS;
            case "isAlive": return IS_ALIVE;
        }
    }
    
    private static final LPLValue SET_CONTAINERS = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<HitPoints>toLPLObject().setContainers(arg1.toJavaInt());
    });
    private static final LPLValue SET_RED_HEARTS = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<HitPoints>toLPLObject().setRedHearts(arg1.toJavaInt());
    });
    private static final LPLValue SET_BLUE_HEARTS = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<HitPoints>toLPLObject().setBlueHearts(arg1.toJavaInt());
    });
    private static final LPLValue GET_CONTAINERS = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<HitPoints>toLPLObject().getContainers());
    });
    private static final LPLValue GET_RED_HEARTS = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<HitPoints>toLPLObject().getRedHearts());
    });
    private static final LPLValue GET_BLUE_HEARTS = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<HitPoints>toLPLObject().getBlueHearts());
    });
    private static final LPLValue GET_CURRENT_HIT_POINTS = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<HitPoints>toLPLObject().getCurrentHitPoints());
    });
    private static final LPLValue IS_ALIVE = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<HitPoints>toLPLObject().isAlive());
    });
}
