/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.creature;

import java.io.IOException;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;

/**
 *
 * @author Asus
 */
public final class HitPoints implements SerializableObject
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

    @Override
    public final void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeInt(containers);
        gds.writeInt(redHearts);
        gds.writeInt(blueHearts);
    }

    @Override
    public final void unserialize(GameDataLoader gdl) throws IOException
    {
        containers = gdl.readInt();
        redHearts = gdl.readInt();
        blueHearts = gdl.readInt();
    }
}
