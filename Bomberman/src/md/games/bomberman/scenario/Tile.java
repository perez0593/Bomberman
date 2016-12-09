/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import md.games.bomberman.geom.Vector2;
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
    private Sprite sprite;
    
    Tile(TileManager manager, int row, int column)
    {
        this.manager = manager;
        this.row = row;
        this.column = column;
    }
    
    public final TileManager getManager() { return manager; }
    public final int getRow() { return row; }
    public final int getColumn() { return column; }
    public final Sprite getSprite() { return sprite; }
    
    public final void setSprite(Sprite sprite) { this.sprite = sprite; }
    
    public final Vector2 getPosition() { return manager.getTilePosition(row,column); }
    
    final void update(double delta)
    {
        if(sprite != null)
            sprite.update(delta);
    }
    
    final void draw(Graphics2D g, double x, double y, double w, double h)
    {
        if(sprite != null)
            sprite.draw(g,x,y,w,h);
    }
}
