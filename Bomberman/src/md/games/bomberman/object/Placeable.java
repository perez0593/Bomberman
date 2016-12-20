/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import md.games.bomberman.object.GameObject;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;

/**
 *
 * @author Asus
 */
public abstract class Placeable extends GameObject
{
    private Tile tile;
    
    public final void putInTile(int row, int column)
    {
        if(!hasScenarioReference())
            return;
        TileManager tiles = getScenarioReference().getTileManager();
        tile = tiles.getTile(row,column);
        tile.putPlaceable(this);
    }
    public final Tile getTilePlaced() { return tile; }
    public final boolean isPlacedOnTile() { return tile != null; }
    
    public final void removeFromTile()
    {
        if(tile == null || !hasScenarioReference())
            return;
        
    }
    
    public final boolean putInCurrentTile()
    {
        if(!hasScenarioReference())
            return false;
        TileManager tiles = getScenarioReference().getTileManager();
        Tile t = tiles.getTileByPosition(getPosition());
        if(t != null)
        {
            tile = t;
            tile.putPlaceable(this);
            return true;
        }
        return false;
    }
}
