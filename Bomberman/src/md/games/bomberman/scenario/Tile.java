/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.io.IOException;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.collectible.Collectible;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.placeable.Placeable;
import md.games.bomberman.scenario.Explosion.ExplosionId;
import md.games.bomberman.scenario.Explosion.ExplosionReference;
import md.games.bomberman.sprites.Sprite;

/**
 *
 * @author mpasc
 */
public final class Tile
{
    private final TileManager manager;
    private final int row;
    private final int column;
    private final ExplosionReference explosion;
    private Placeable placeable;
    private Collectible collectible;
    private Sprite sprite;
    
    
    Tile(TileManager manager, int row, int column, ExplosionReference explosion)
    {
        this.manager = manager;
        this.row = row;
        this.column = column;
        this.explosion = explosion;
    }
    
    public final TileManager getManager() { return manager; }
    public final int getRow() { return row; }
    public final int getColumn() { return column; }
    public final Sprite getSprite() { return sprite; }
    
    public final void setSprite(Sprite sprite) { this.sprite = sprite; }
    
    public final Vector2 getPosition() { return manager.getTilePosition(row,column); }
    
    public final void createExplosion(ExplosionId explosionId) { explosion.explode(explosionId); }
    
    /*public final void onCreatureCollide(Creature creature)
    {
        if(placeable != null)
            placeable.onCreatureCollide(creature);
        else if(collectible != null)
        {
            collectible.onCollect(creature);
            //manager.getScenarioReference().sendAction(Action.collectCollectible(collectible.getId(),creature.getId()));
            collectible = null;
        }
    }*/
    
    public final void collectCollectible(Creature c)
    {
        if(collectible == null)
            return;
        collectible.onCollect(c);
        collectible = null;
    }
    
    public final boolean checkCreatureCollision(Creature c)
    {
        return (placeable != null && c.hasCollision(placeable)) ||
                (collectible != null && c.hasCollision(collectible));
    }
    
    final void situateBoundingBox(BoundingBox box)
    {
        box.translate(getPosition());
    }
    
    final BoundingBox getBoundingBox()
    {
        Vector2 p = getPosition();
        return new BoundingBox(p.x,p.y,p.x+manager.getWidth(),p.y+manager.getHeight());
    }
    
    public final void putPlaceable(Placeable placeable)
    {
        if(placeable == null)
            throw new NullPointerException();
        if(this.placeable != null)
            throw new IllegalStateException();
        if(!manager.hasScenarioReference())
            throw new IllegalStateException();
        if(placeable.isPlacedOnTile())
        {
            this.placeable = placeable;
            placeable.setScenarioReference(manager.getScenarioReference());
            Vector2 position = getPosition();
            position.add(manager.getTileWidth()/2,manager.getTileHeight()/2);
            placeable.setPosition(position);
        }
        else placeable.putOnTile(row,column);
    }
    public final Placeable getPlaceable() { return placeable; }
    public final boolean canPutPlaceable() { return placeable == null; }
    public final void removePlaceable()
    {
        if(placeable == null)
            return;
        if(placeable.isPlacedOnTile())
            placeable = null;
        else placeable.removeFromTile();
    }
    
    public final void putCollectible(Collectible collectible)
    {
        if(collectible == null)
            throw new NullPointerException();
        if(!manager.hasScenarioReference())
            throw new IllegalStateException();
        this.collectible = this.collectible == null
                ? Collectible.join(this.collectible,collectible)
                : collectible;
    }
    public final Collectible getCollectible() { return collectible; }
    public final boolean canPutCollectible() { return collectible == null; }
    public final void removeCollectible()
    {
        if(collectible != null)
            collectible = null;
    }
    
    final void update(double delta)
    {
        if(sprite != null)
            sprite.update(delta);
        if(placeable != null)
            placeable.update(delta);
        else if(collectible != null)
            collectible.update(delta);
        if(!explosion.isEnd())
        {
            explosion.update(delta);
            if(placeable != null)
                placeable.onExplodeHit();
        }
    }
    
    public final boolean hasPlaceable() { return placeable != null; }
    public final boolean hasCollectible() { return collectible != null; }
    
    final void draw(Graphics2D g, double x, double y, double w, double h)
    {
        if(sprite != null)
            sprite.draw(g,x,y,w,h);
        if(placeable != null)
            placeable.draw(g);
        else if(collectible != null)
            collectible.draw(g);
        if(!explosion.isEnd())
            explosion.draw(g,x,y,w,h);
    }
    
    final void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeIfNonNull(placeable,() -> gds.writeUUID(placeable.getId()));
        gds.writeIfNonNull(collectible,() -> gds.writeUUID(collectible.getId()));
        gds.writeIfNonNull(sprite,() -> gds.writeSprite(sprite));
    }
    
    final void unserialize(GameDataLoader gdl) throws IOException
    {
        gdl.readIfNonNull(() -> placeable = gdl.readGameObjectById());
        gdl.readIfNonNull(() -> collectible = gdl.readGameObjectById());
        gdl.readIfNonNull(() -> sprite = gdl.readSprite());
    }
}
