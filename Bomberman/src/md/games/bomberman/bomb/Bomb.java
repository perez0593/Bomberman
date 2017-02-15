/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.bomb;

import java.awt.Graphics2D;
import java.io.IOException;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.placeable.Placeable;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
import md.games.bomberman.sprites.Sprite;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.sprites.SpriteUtils;
import md.games.bomberman.util.Constants;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public class Bomb extends Placeable
{
    private Sprite sprite;
    protected int range;
    protected int damage;
    protected boolean timingBomb;
    protected boolean remoteMode;
    private double timeRemaining;
    private int fireResistence = 5;
    private boolean exploited = false;
    
    protected Bomb() {}
    
    public Bomb(int range, int damage, boolean remoteMode, Sprite sprite)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        this.range = range;
        this.damage = damage;
        this.timingBomb = false;
        this.remoteMode = remoteMode;
        this.timeRemaining = 1d;
        this.sprite = sprite;
        
        setSize(Constants.BOMB_WIDTH,Constants.BOMB_HEIGHT);
        createBoundingBox();
    }
    
    public Bomb(int range, int damage, boolean remoteMode, Sprite sprite, double timeToExplode)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        this.range = range;
        this.damage = damage;
        this.timingBomb = true;
        this.remoteMode = remoteMode;
        this.timeRemaining = timeToExplode;
        this.sprite = sprite;
        
        setSize(Constants.BOMB_WIDTH,Constants.BOMB_HEIGHT);
        createBoundingBox();
    }
    
    public final int getRange() { return range; }
    public final int getDamage() { return damage; }
    public final boolean isTimingBomb() { return timingBomb; }
    public final boolean hasRemoteMode() { return remoteMode; }
    
    @Override
    public final boolean isBomb() { return true; }
    
    public final void explodeByRemote()
    {
        if(!remoteMode)
            return;
        explode();
    }
    
    public final void explode()
    {
        if(exploited || !hasScenarioReference() || !isPlacedOnTile())
            return;
        exploited = true;
        Tile tileOnPlaced = getTilePlaced();
        TileManager tiles = getScenarioReference().getTileManager();
        destroy();
        explode(tiles,tileOnPlaced);
    }
    protected void explode(TileManager tiles, Tile tileOnPlaced)
    {
        tiles.createCrossExplosion(tileOnPlaced.getRow(),tileOnPlaced.getColumn(),range);
    }
    
    public final boolean hasExploited() { return exploited; }
    
    @Override
    public final void onExplodeHit()
    {
        if(fireResistence > 0)
            fireResistence--;
        else explode();
    }
    
    @Override
    public void update(double delta)
    {
        if(!exploited)
        {
            if(sprite != null)
                sprite.update(delta);
            if(timingBomb)
            {
                if(timeRemaining <= 0)
                    explode();
                else timeRemaining -= delta;
            }
        }
    }
    
    @Override
    public void onCreatureCollide(Creature creature) {}
    
    @Override
    public boolean canCreatureWalk(Creature creature) { return true; }

    @Override
    public void draw(Graphics2D g)
    {
        if(!exploited)
        {
            if(sprite != null)
                SpriteUtils.drawGameObjectSprite(this,g,sprite);
        }
    }
    
    @Override
    public void reloadSprites(SpriteManager sprites)
    {
        if(sprite != null)
        {
            String tag = sprite.getSpriteTag();
            sprite = sprites.getSprite(tag);
        }
    }

    @Override
    protected void innerSerialize(GameDataSaver gds) throws IOException
    {
        gds.writeInt(range);
        gds.writeInt(damage);
        gds.writeBoolean(timingBomb);
        gds.writeBoolean(remoteMode);
        gds.writeDouble(timeRemaining);
        gds.writeInt(fireResistence);
        gds.writeBoolean(exploited);
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        range = gdl.readInt();
        damage = gdl.readInt();
        timingBomb = gdl.readBoolean();
        remoteMode = gdl.readBoolean();
        timeRemaining = gdl.readDouble();
        fireResistence = gdl.readInt();
        exploited = gdl.readBoolean();
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        switch(key)
        {
            default: return super.getAttribute(key);
            case "getRange": return GET_RANGE;
            case "getDamage": return GET_DAMAGE;
            case "isTimingBomb": return IS_TIMING_BOMB;
            case "hasRemoteMode": return HAS_REMOTE_MODE;
            case "hasExploited": return HAS_EXPLOITED;
            case "explode": return EXPLODE;
        }
    }
    
    private static final LPLValue GET_RANGE = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Bomb>toLPLObject().getRange());
    });
    private static final LPLValue GET_DAMAGE = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Bomb>toLPLObject().getDamage());
    });
    private static final LPLValue IS_TIMING_BOMB = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Bomb>toLPLObject().isTimingBomb());
    });
    private static final LPLValue HAS_REMOTE_MODE = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Bomb>toLPLObject().hasRemoteMode());
    });
    private static final LPLValue HAS_EXPLOITED = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Bomb>toLPLObject().hasExploited());
    });
    private static final LPLValue EXPLODE = LPLFunction.createVFunction((arg0) -> {
        arg0.<Bomb>toLPLObject().explode();
    });
}
