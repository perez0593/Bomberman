/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.placeable;

import md.games.bomberman.creature.Creature;
import md.games.bomberman.scenario.GameObject;
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
    protected double height;
    
    @Override
    protected void innerDestroy()
    {
        if(tile != null)
            removeFromTile();
    }
    
    @Override
    public final boolean isPlaceable() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_PLACEABLE; }
    
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
        Tile aux = tile;
        tile = null;
        aux.removePlaceable();
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
    
    public final void setHeight(double height) { this.height = height < 0 ? 0 : height; }
    public final double getHeight() { return height; }
    public final void modifyHeight(double amount)
    {
        height += amount;
        if(height < 0)
            height = 0;
    }
    
    public abstract void onExplodeHit();
    public abstract void onCreatureCollide(Creature creature);
    public abstract boolean canCreatureWalk(Creature creature);
    
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
            case "setHeight": return SET_HEIGHT;
            case "getHeight": return GET_HEIGHT;
            case "modifyHeight": return MODIFY_HEIGHT;
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
    private static final LPLValue SET_HEIGHT = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<Placeable>toLPLObject().setHeight(arg1.toJavaDouble());
    });
    private static final LPLValue GET_HEIGHT = LPLFunction.createFunction((arg0) -> {
        return valueOf(arg0.<Placeable>toLPLObject().getHeight());
    });
    private static final LPLValue MODIFY_HEIGHT = LPLFunction.createVFunction((arg0, arg1) -> {
        arg0.<Placeable>toLPLObject().modifyHeight(arg1.toJavaDouble());
    });
}
