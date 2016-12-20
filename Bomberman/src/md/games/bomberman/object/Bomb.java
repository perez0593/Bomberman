/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import md.games.bomberman.object.bomb.BombType;
import java.awt.Graphics2D;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public class Bomb extends Placeable
{
    protected final int range;
    protected final int damage;
    protected final boolean timingBomb;
    protected final boolean remoteMode;
    private double timeRemaining;
    private boolean exploited = false;
    
    public Bomb(int range, int damage, boolean remoteMode)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        this.range = range;
        this.damage = damage;
        this.timingBomb = false;
        this.remoteMode = remoteMode;
        this.timeRemaining = 1d;
    }
    
    public Bomb(int range, int damage, boolean remoteMode, double timeToExplode)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        this.range = range;
        this.damage = damage;
        this.timingBomb = true;
        this.remoteMode = remoteMode;
        this.timeRemaining = timeToExplode;
    }
    
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
        explode(getScenarioReference().getTileManager(),getTilePlaced());
        removeFromTile();
    }
    protected void explode(TileManager tiles, Tile tileOnPlaced)
    {
        tiles.createCrossExplosion(tileOnPlaced.getRow(),tileOnPlaced.getColumn(),range);
    }
    
    public final boolean hasExploited() { return exploited; }
    
    @Override
    public void update(double delta)
    {
        if(!exploited)
        {
            if(timingBomb)
            {
                if(timeRemaining <= 0)
                    explode();
                else timeRemaining -= delta;
            }
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        
    }

    @Override
    protected void innserSerialize(GameDataSaver gds)
    {
        
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl)
    {
        
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        return null;
    }
    
}
