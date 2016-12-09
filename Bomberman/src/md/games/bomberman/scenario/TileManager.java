/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import md.games.bomberman.geom.Vector2;

/**
 *
 * @author mpasc
 */
public class TileManager
{
    private final Tile[] tiles;
    private final int rows;
    private final int columns;
    private final Vector2 position;
    private final Vector2 size;
    private final Vector2 tileSize;
    
    public TileManager(int rows, int columns)
    {
        if(rows < 1 || columns < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.columns = columns;
        tiles = new Tile[rows * columns];
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
                tiles[r * rows + c] = new Tile(this,r,c);
        position = new Vector2();
        size = new Vector2(1,1);
        tileSize = new Vector2();
        computeTileSize();
    }
    
    public final int getRows() { return rows; }
    public final int getColumns() { return columns; }
    public final int getTileCount() { return rows * columns; }
    
    public final Tile getTile(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || columns >= columns)
            throw new IllegalArgumentException();
        return tiles[row * rows + column];
    }
    public final Tile getTile(int index)
    {
        if(index < 0 || index > rows * columns)
            throw new IllegalArgumentException();
        return tiles[index];
    }
    
    public final Vector2 getTilePosition(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || columns >= columns)
            throw new IllegalArgumentException();
        return new Vector2(
                position.x + tileSize.x * column,
                position.y = tileSize.y * row
        );
    }
    
    public final void setPositionX(double x) { position.x = x; }
    public final void setPositionY(double y) { position.y = y; }
    public final void setPosition(double x, double y) { position.set(x,y); }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    public final Vector2 getPosition() { return position.copy(); }
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    
    public final void setWidth(double width) { size.x = width; computeTileSize(); }
    public final void setHeight(double height) { size.y = height; computeTileSize(); }
    public final void setSize(double width, double height) { size.set(width,height); computeTileSize(); }
    public final void setSize(Vector2 size) { this.size.set(size); computeTileSize(); }
    public final Vector2 getSize() { return size.copy(); }
    public final double getWidth() { return size.x; }
    public final double getHeight() { return size.y; }
    
    private void computeTileSize()
    {
        tileSize.x = size.x / columns;
        tileSize.y = size.y / rows;
    }
}
