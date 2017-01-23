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
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.sprites.Animation;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.Constants;
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
    private final Vector2 animationSize = new Vector2();
    protected Animation animation;
    private double speedRatio = 1f;
    private Direction walkDirection;
    
    @Override
    public final boolean isCreature() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_CREATURE; }
    
    public abstract CreatureType getCreatureType();
    
    protected final void loadAnimation(SpriteManager sprites, String animationTag)
    {
        animation = sprites.getSprite(animationTag);
    }
    
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
    
    public final void setAnimationSize(Vector2 size) { animationSize.set(size); }
    public final void setAnimationSize(double width, double height) { animationSize.set(width,height); }
    public final void setAnimationSizeWidth(double width) { animationSize.x = width; }
    public final void setAnimationSizeHeight(double height) { animationSize.y = height; }
    
    public final Vector2 getAnimationSize() { return animationSize.copy(); }
    public final double getAnimationSizeWidth() { return animationSize.x; }
    public final double getAnimationSizeHeight() { return animationSize.y; }
    
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
        walkDirection = computeWalkingDirection();
    }
    
    public final void stopWalk(boolean inXAxis)
    {
        if(inXAxis)
            speed.x = 0;
        speed.y = 0;
        walkDirection = speed.isZero() ? null : computeWalkingDirection();
    }
    public final void stopWalk()
    {
        speed.zero();
        walkDirection = null;
    }
    
    protected Direction getWalkingDirection() { return walkDirection; }
    
    public abstract void die();
    
    public final void damage(int amount) { hp.damage(amount); }
    public final void heal(int amount) { hp.heal(amount); }
    public final void kill() { hp.kill(); }
    
    private Direction computeWalkingDirection()
    {
        int dir = ((int)((new Vector2(speed.x,-speed.y).getDirection()) / Math.PI * 180d)) % 360;
        if(dir < 0)
            dir += 360;
        if(dir > 315 || dir < 45)
            return Direction.RIGHT;
        if(dir >= 45 && dir <= 135)
            return Direction.UP;
        if(dir > 135 && dir < 225)
            return Direction.LEFT;
        return Direction.DOWN;
    }
    
    protected void updateAnimation(double delta)
    {
        animation.update(delta);
        if(walkDirection == null)
            animation.setAnimationSequence(Constants.CREATURE_ANIMATION_STOPPED);
        else switch(walkDirection)
        {
            default: animation.setAnimationSequence(Constants.CREATURE_ANIMATION_STOPPED); break;
            case UP: animation.setAnimationSequence(Constants.CREATURE_ANIMATION_WALK_UP); break;
            case DOWN: animation.setAnimationSequence(Constants.CREATURE_ANIMATION_WALK_DOWN); break;
            case LEFT: animation.setAnimationSequence(Constants.CREATURE_ANIMATION_WALK_LEFT); break;
            case RIGHT: animation.setAnimationSequence(Constants.CREATURE_ANIMATION_WALK_RIGHT); break;
        }
    }
    
    @Override
    public boolean canSee(Camera camera)
    {
        return camera.contains(
                getPositionX() - animationSize.x / 2,
                getPositionY() - animationSize.y / 2,
                animationSize.x,animationSize.y);
    }
    
    @Override
    public void update(double delta)
    {
        updateAnimation(delta);
        if(hasScenarioReference())
        {
            Scenario scenario = getScenarioReference();
            List<Tile> neis = scenario.getTileManager().findNeighbors(this);
            Vector2 dspeed = speed.product(delta);
            double elapsedTime = 1d;
            if(!neis.isEmpty())
            {
                for(Tile tile : neis)
                {
                    if(tile.hasPlaceable())
                    {
                        SweptInfo si = computeSwept(dspeed,tile.getPlaceable());
                        if(si.time < 1)
                        {
                            tile.getPlaceable().onCreatureCollide(this);
                            if(elapsedTime > si.time)
                                elapsedTime = si.time;
                        }
                    }
                }
            }
            translate(dspeed.x * elapsedTime, dspeed.y * elapsedTime);

            Tile currentTile = scenario.getTileManager().getTileByPosition(getPosition());
            if(currentTile != null)
            {
                if(currentTile.hasCollectible() && hasCollision(currentTile.getCollectible()))
                    currentTile.collectCollectible(this);
            }
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
