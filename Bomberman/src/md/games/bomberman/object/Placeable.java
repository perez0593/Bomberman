/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public abstract class Placeable extends GameObject
{
    private Tile tile;
    
    public final boolean putOnTile(int row, int column)
    {
        if(!hasScenarioReference())
            return false;
        TileManager tiles = getScenarioReference().getTileManager();
        tile = tiles.getTile(row,column);
        tile.putPlaceable(this);
        return true;
    }
    public final Tile getTilePlaced() { return tile; }
    public final boolean isPlacedOnTile() { return tile != null; }
    
    public final void removeFromTile()
    {
        if(tile == null || !hasScenarioReference())
            return;
        
    }
    
    public final boolean putOnCurrentTile()
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
    
    public abstract void onExplodeHit();
    public abstract void onPlayerCollide(Player player);
    
    @Override
    protected LPLValue getAttribute(String key)
    {
        switch(key)
        {
            default: return null;
            case "putOnTile": return PUT_ON_TILE;
            case "putOnCurrentTile": return PUT_ON_CURRENT_TILE;
            case "getTilePlaced": return GET_TILE_PLACED;
            case "isPlacedOnTile": return IS_PLACED_ON_TILE;
        }
    }
    
    private static final LPLValue PUT_ON_TILE = LPLFunction.createFunction((arg0, arg1, arg2) -> {
        return arg0.<Placeable>toLPLObject().putOnTile(arg1.toJavaInt(),arg2.toJavaInt()) ? TRUE : FALSE;
    });
    private static final LPLValue PUT_ON_CURRENT_TILE = LPLFunction.createFunction((arg0) -> {
        return arg0.<Placeable>toLPLObject().putOnCurrentTile() ? TRUE : FALSE;
    });
    private static final LPLValue GET_TILE_PLACED = LPLFunction.createFunction((arg0) -> {
        Tile tile = arg0.<Placeable>toLPLObject().getTilePlaced();
        return LPLValue.varargsOf(valueOf(tile.getRow()),valueOf(tile.getColumn()));
    });
    private static final LPLValue IS_PLACED_ON_TILE = LPLFunction.createFunction((arg0) -> {
        return arg0.<Placeable>toLPLObject().isPlacedOnTile() ? TRUE : FALSE;
    });
}
