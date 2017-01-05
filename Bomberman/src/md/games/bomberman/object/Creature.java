/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.io.IOException;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.object.creature.HitPoints;

/**
 *
 * @author Asus
 */
public abstract class Creature extends GameObject
{
    private final HitPoints hp = new HitPoints(1);
    private double speed = 1f;
    
    @Override
    public final boolean isCreature() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_CREATURE; }
    
    public abstract CreatureType getCreatureType();
    
    public final boolean isPlayer() { return getCreatureType() == CreatureType.PLAYER; }
    
    public final int getCurrentHitPoints() { return hp.getCurrentHitPoints(); }
    public final double getSpeed() { return speed; }
    
    public final boolean isAlive() { return hp.isAlive(); }
    public final HitPoints getHitPointsManager() { return hp; }
    
    public final void setSpeed(double speed) { this.speed = speed < 0 ? 0 : speed; }
    
    public final void increaseSpeed(double factor) { speed += factor < 0 ? -factor : factor; }
    public final void decreaseSpeed(double factor)
    {
        speed -= factor < 0 ? -factor : factor;
        speed = speed < 0 ? 0 : speed;
    }
    
    public abstract void die();
    
    public final void damage(int amount) { hp.damage(amount); }
    public final void heal(int amount) { hp.heal(amount); }
    public final void kill() { hp.kill(); }
    
    @Override
    protected void innerSerialize(GameDataSaver gds) throws IOException
    {
        hp.serialize(gds);
        gds.writeDouble(speed);
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        hp.unserialize(gdl);
        speed = gdl.readDouble();
    }
}
