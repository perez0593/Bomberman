/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.util.Iterator;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.scenario.Explosion.ExplosionId;

/**
 *
 * @author mpasc
 */
public class TileManager implements Iterable<Tile>
{
    private Tile[] tiles;
    private int rows;
    private int columns;
    private final Vector2 position;
    private final Vector2 size;
    private final Vector2 tileSize;
    private final Explosion explosion;
    
    public TileManager(int rows, int columns)
    {
        if(rows < 1 || columns < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.columns = columns;
        explosion = Explosion.getManager();
        tiles = new Tile[rows * columns];
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
                tiles[r * rows + c] = new Tile(this,r,c,explosion.getReference());
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
        return tiles[row * columns + column];
    }
    public final Tile getTile(int index)
    {
        if(index < 0 || index > rows * columns)
            throw new IllegalArgumentException();
        return tiles[index];
    }
    public final Tile getTileByPosition(double x, double y)
    {
        int row = (int) ((y - position.y) / tileSize.y);
        int column = (int) ((x - position.x) / tileSize.x);
        return row < 0 || row >= rows || column < 0 || column >= columns
                ? null
                : tiles[row * columns + column];
    }
    public final Tile getTileByPosition(Vector2 position) { return getTileByPosition(position.x,position.y); }
    
    public final Vector2 getTilePosition(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || columns >= columns)
            throw new IllegalArgumentException();
        return new Vector2(
                position.x + tileSize.x * column,
                position.y + tileSize.y * row
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
    
    public final double getTileWidth() { return tileSize.x; }
    public final double getTileHeight() { return tileSize.y; }
    public final Vector2 getTileSize() { return tileSize.copy(); }
    
    public final void createCrossExplosion(int row, int column, int range)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns || range < 1)
            throw new IllegalArgumentException();
        int len = range * 2 - 1;
        int r0 = row - (range - 1);
        int c0 = column - (range - 1);
        
        for(int r=r0;r<len&&r<rows;r++)
            tiles[r * columns + column].createExplosion(r == r0 
                    ? ExplosionId.END_UP : r == r0+len||r0==rows-1 ? ExplosionId.END_DOWN : ExplosionId.VERTICAL);
        for(int c=c0;c<len&&c<columns;c++)
            tiles[row * columns + c].createExplosion(c == c0 
                    ? ExplosionId.END_LEFT : c == c0+len||c0==columns-1 ? ExplosionId.END_RIGHT : ExplosionId.HORIZONTAL);
    }
    
    public final void createSquareExplosion(int row, int column, int range)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns || range < 1)
            throw new IllegalArgumentException();
        int r0 = row - (range - 1);
        int c0 = column - (range - 1);
        int len = range * 2 - 1;
        int rowsLen = r0 + len >= rows ? len - ((r0 + len + 1) - rows) : len;
        int columnsLen = c0 + len >= columns ? len - ((c0 + len + 1) - columns) : len;
        
        for(int r=r0;r<rowsLen;r++)
        {
            tiles[r * columns + c0].createExplosion(r == r0 
                    ? ExplosionId.END_UP : r == r0+len||r0==rows-1 ? ExplosionId.END_DOWN : ExplosionId.VERTICAL);
            tiles[r * columns + c0 + columnsLen - 1].createExplosion(r == r0 
                    ? ExplosionId.END_UP : r == r0+len||r0==rows-1 ? ExplosionId.END_DOWN : ExplosionId.VERTICAL);
        }
        for(int c=c0;c<columnsLen;c++)
        {
            tiles[r0 * columns + c].createExplosion(c == c0 
                    ? ExplosionId.END_LEFT : c == c0+len||c0==columns-1 ? ExplosionId.END_RIGHT : ExplosionId.HORIZONTAL);
            tiles[(r0 + rowsLen - 1) * columns + c].createExplosion(c == c0 
                    ? ExplosionId.END_LEFT : c == c0+len||c0==columns-1 ? ExplosionId.END_RIGHT : ExplosionId.HORIZONTAL);
        }
    }
    
    public final void update(double delta)
    {
        for(Tile tile : tiles)
            tile.update(delta);
    }
    
    public final void draw(Graphics2D g)
    {
        for(Tile tile : tiles)
        {
            tile.draw(g,
                    position.x + tileSize.x * tile.getColumn(),
                    position.y + tileSize.y * tile.getRow(),
                    tileSize.x,
                    tileSize.y
            );
        }
    }
    
    public final void resize(int rows, int columns)
    {
        if(rows < 1 || columns < 1)
            throw new IllegalArgumentException();
        int oldRows = this.rows;
        int oldColumns = this.columns;
        Tile[] aux = tiles;
        this.rows = rows;
        this.columns = columns;
        tiles = new Tile[rows * columns];
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
            {
                tiles[r * rows + c] = r < oldRows && c < oldColumns
                        ? aux[r * oldRows + c]
                        : new Tile(this,r,c,explosion.getReference());
            }
    }

    @Override
    public final Iterator<Tile> iterator() { return new TileIterator(); }
    
    private final class TileIterator implements Iterator<Tile>
    {
        private int it = 0;

        @Override
        public final boolean hasNext() { return it < tiles.length; }

        @Override
        public final Tile next() { return tiles[it++]; }
    }
}
