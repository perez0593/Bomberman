/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.io.IOException;
import java.util.List;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.object.creature.HitPoints;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.util.Direction;
import md.games.bomberman.util.Utils.SweptInfo;

/**
 *
 * @author Asus
 */
public abstract class Creature extends GameObject
{
    private final HitPoints hp = new HitPoints(1);
    private final Vector2 speed = new Vector2();
    private double speedRatio = 1f;
    
    @Override
    public final boolean isCreature() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_CREATURE; }
    
    public abstract CreatureType getCreatureType();
    
    public final boolean isPlayer() { return getCreatureType() == CreatureType.PLAYER; }
    
    public final int getCurrentHitPoints() { return hp.getCurrentHitPoints(); }
    public final double getSpeedRatio() { return speedRatio; }
    
    public final boolean isAlive() { return hp.isAlive(); }
    public final HitPoints getHitPointsManager() { return hp; }
    
    public final void setSpeedRatio(double speed) { this.speedRatio = speed < 0 ? 0 : speed; }
    
    public final void increaseSpeedRatio(double factor) { speedRatio += factor < 0 ? -factor : factor; }
    public final void decreaseSpeedRatio(double factor)
    {
        speedRatio -= factor < 0 ? -factor : factor;
        speedRatio = speedRatio < 0 ? 0 : speedRatio;
    }
    
    public final Vector2 getSpeed() { return speed.copy(); }
    
    public final void walk(Direction direction, double speedBase)
    {
        switch(direction)
        {
            default: throw new IllegalStateException();
            case UP: speed.y = -(speedBase * speedRatio); break;
            case DOWN: speed.y = speedBase * speedRatio; break;
            case LEFT: speed.x = -(speedBase * speedRatio); break;
            case RIGHT: speed.x = speedBase * speedRatio; break;
        }
    }
    
    public final void stopWalk(boolean inXAxis)
    {
        if(inXAxis)
            speed.x = 0;
        speed.y = 0;
    }
    public final void stopWalk() { speed.zero(); }
    
    public abstract void die();
    
    public final void damage(int amount) { hp.damage(amount); }
    public final void heal(int amount) { hp.heal(amount); }
    public final void kill() { hp.kill(); }
    
    @Override
    public void update(double delta)
    {
        List<Tile> cols = getScenarioReference().getTileManager().findCollisionTiles(this);
        if(cols.isEmpty())
            translate(speed.x * delta, speed.y * delta);
        else
        {
            Vector2 dspeed = speed.product(delta);
            double elapsedTime = 1d;
            for(Tile tile : cols)
            {
                tile.onCreatureCollide(this);
                if(tile.canPutPlaceable())
                    continue;
                SweptInfo si = computeSwept(dspeed,tile.getPlaceable());
                if(elapsedTime > si.time)
                    elapsedTime = si.time;
            }
            translate(dspeed.x * elapsedTime, dspeed.y * elapsedTime);
        }
    }
    
    @Override
    protected void innerSerialize(GameDataSaver gds) throws IOException
    {
        hp.serialize(gds);
        gds.writeDouble(speedRatio);
        gds.writeVector2(speed);
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        hp.unserialize(gdl);
        speedRatio = gdl.readDouble();
        speed.set(gdl.readVector2());
    }
}
