/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import md.games.bomberman.placeable.Placeable;
import md.games.bomberman.scenario.Explosion.ExplosionId;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.Constants;

/**
 *
 * @author mpasc
 */
public final class TileManager implements SerializableObject, Iterable<Tile>
{
    private Tile[] tiles;
    private int rows;
    private int columns;
    private Vector2 position;
    private Vector2 size;
    private Vector2 tileSize;
    private Scenario scenario;
    private boolean enabledGrid;
    private final Explosion explosion = Explosion.getManager();
    
    public TileManager(int rows, int columns)
    {
        if(rows < 1 || columns < 1)
            throw new IllegalArgumentException();
        this.rows = rows;
        this.columns = columns;
        tiles = new Tile[rows * columns];
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
                tiles[r * rows + c] = new Tile(this,r,c,explosion.getReference());
        position = new Vector2();
        size = new Vector2();
        tileSize = new Vector2(Constants.TILE_WIDTH,Constants.TILE_HEIGHT);
        computeSize();
    }
    private TileManager() {}
    
    public final void setScenarioReference(Scenario scenario) { this.scenario = scenario; }
    public final Scenario getScenarioReference() { return scenario; }
    public final boolean hasScenarioReference() { return scenario != null; }
    
    public final int getRows() { return rows; }
    public final int getColumns() { return columns; }
    public final int getTileCount() { return rows * columns; }
    
    public final Tile getTile(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns)
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
        if(y - position.y < 0 || x - position.x < 0)
            return null;
        int row = (int) ((y - position.y) / tileSize.y);
        int column = (int) ((x - position.x) / tileSize.x);
        return row < 0 || row >= rows || column < 0 || column >= columns
                ? null
                : tiles[row * columns + column];
    }
    public final Tile getTileByPosition(Vector2 position) { return getTileByPosition(position.x,position.y); }
    
    public final Point getTilePositionByPosition(double x, double y)
    {
        return new Point((int) ((x - position.x) / tileSize.x), (int) ((y - position.y) / tileSize.y));
    }
    public final Point getTilePositionByPosition(Vector2 position) { return getTilePositionByPosition(position.x,position.y); }
    
    public final Vector2 getTilePosition(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns)
            throw new IllegalArgumentException();
        return new Vector2(
                position.x + tileSize.x * column,
                position.y + tileSize.y * row
        );
    }
    
    public final Tile getLookAtTile(GameObject go)
    {
        Tile current = getTileByPosition(go.getPosition());
        int degrees = Math.abs((((int) go.getDirection()) - 45) / 90) % 4;
        Tile tile;
        switch(degrees)
        {
            case 0: tile = tile(current.getRow()-1,current.getColumn()); break;
            case 1: tile = tile(current.getRow(),current.getColumn()-1); break;
            case 2: tile = tile(current.getRow()+1,current.getColumn()); break;
            case 3: tile = tile(current.getRow(),current.getColumn()+1); break;
            default: return null;
        }
        
        BoundingBox box = tile.getBoundingBox();
        if(go.hasCollision(box))
            switch(degrees)
            {
                case 0: return tile(tile.getRow()-1,tile.getColumn());
                case 1: return tile(tile.getRow(),tile.getColumn()-1);
                case 2: return tile(tile.getRow()+1,tile.getColumn());
                case 3: return tile(tile.getRow(),tile.getColumn()+1);
                default: return null;
            }
        return tile;
    }
    
    public final List<Tile> findNeighbors(GameObject go)
    {
        Point base = getTilePositionByPosition(go.getPosition());
        LinkedList<Tile> neis = new LinkedList<>();
        neighborTile(neis,go,base.y-1,base.x-1);
        neighborTile(neis,go,base.y-1,base.x);
        neighborTile(neis,go,base.y-1,base.x+1);
        neighborTile(neis,go,base.y,base.x-1);
        neighborTile(neis,go,base.y,base.x);
        neighborTile(neis,go,base.y,base.x+1);
        neighborTile(neis,go,base.y+1,base.x-1);
        neighborTile(neis,go,base.y+1,base.x);
        neighborTile(neis,go,base.y+1,base.x+1);
        return neis;
    }
    
    public final List<Tile> findCollisionTiles(Creature creature)
    {
        Tile base = getTileByPosition(creature.getPosition());
        LinkedList<Tile> cols = new LinkedList<>();
        int r = base.getRow();
        int c = base.getColumn();
        
        if(base.checkCreatureCollision(creature))
            cols.add(base);
        
        collideTile(cols,creature,r-1,c-1);
        collideTile(cols,creature,r-1,c);
        collideTile(cols,creature,r-1,c+1);
        collideTile(cols,creature,r,c-1);
        collideTile(cols,creature,r,c+1);
        collideTile(cols,creature,r+1,c-1);
        collideTile(cols,creature,r+1,c);
        collideTile(cols,creature,r+1,c+1);
        
        return cols;
    }
    
    private void neighborTile(List<Tile> neis, GameObject go, int row, int column)
    {
        Tile tile = tile(row,column);
        if(tile != null)
            neis.add(tile);
    }
    
    private void collideTile(List<Tile> cols, Creature c, int row, int column)
    {
        Tile tile = tile(row,column);
        if(tile == null)
            return;
        if(tile.checkCreatureCollision(c))
            cols.add(tile);
    }
    
    private Tile tile(int row, int column)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns)
            return null;
        return tiles[row * columns + column];
    }
    
    public final void setEnabledDrawGrid(boolean flag) { enabledGrid = flag; }
    public final boolean isEnabledDrawGrid() { return enabledGrid; }
    
    public final void setPositionX(double x) { position.x = x; }
    public final void setPositionY(double y) { position.y = y; }
    public final void setPosition(double x, double y) { position.set(x,y); }
    public final void setPosition(Vector2 position) { this.position.set(position); }
    public final Vector2 getPosition() { return position.copy(); }
    public final double getPositionX() { return position.x; }
    public final double getPositionY() { return position.y; }
    
    //public final void setWidth(double width) { size.x = width; computeTileSize(); }
    //public final void setHeight(double height) { size.y = height; computeTileSize(); }
    public final void setTileSize(double width, double height) { tileSize.set(width,height); computeSize(); }
    public final void setTileSize(Vector2 size) { tileSize.set(size); computeSize(); }
    public final Vector2 getSize() { return size.copy(); }
    public final double getWidth() { return size.x; }
    public final double getHeight() { return size.y; }
    
    private void computeSize()
    {
        size.x = tileSize.x * columns;
        size.y = tileSize.y * rows;
    }
    
    public final double getTileWidth() { return tileSize.x; }
    public final double getTileHeight() { return tileSize.y; }
    public final Vector2 getTileSize() { return tileSize.copy(); }
    
    public final void createCrossExplosion(int row, int column, int range)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns || range < 1)
            throw new IllegalArgumentException();
        Tile tile = tiles[row * columns + column];
        if(!tile.canPutPlaceable())
            return;
        tile.createExplosion(ExplosionId.CROSS);
        boolean[] block = new boolean[4];
        
        for(int i=1;i<range;i++)
        {
            if(!block[0])
            {
                if(row - i < 0)
                    block[0] = true;
                else {
                    tile = tiles[(row - i) * columns + column];
                    if(!tile.canPutPlaceable())
                    {
                        Placeable p = tile.getPlaceable();
                        if(!p.isBomb())
                            block[0] = true;
                        else tile.createExplosion(i == range-1 ? ExplosionId.END_UP : ExplosionId.VERTICAL);
                    }
                    else tile.createExplosion(i == range-1 ? ExplosionId.END_UP : ExplosionId.VERTICAL);
                }
            }
            if(!block[1])
            {
                if(row + i >= rows)
                    block[1] = true;
                else
                {
                    tile = tiles[(row + i) * columns + column];
                    if(!tile.canPutPlaceable())
                    {
                        Placeable p = tile.getPlaceable();
                        if(!p.isBomb())
                            block[1] = true;
                        else tile.createExplosion(i == range-1 ? ExplosionId.END_DOWN : ExplosionId.VERTICAL);
                    }
                    else tile.createExplosion(i == range-1 ? ExplosionId.END_DOWN : ExplosionId.VERTICAL);
                }
            }
            if(!block[2])
            {
                if(column - i < 0)
                    block[2] = true;
                else
                {
                    tile = tiles[row * columns + (column - i)];
                    if(!tile.canPutPlaceable())
                    {
                        Placeable p = tile.getPlaceable();
                        if(!p.isBomb())
                            block[2] = true;
                        else tile.createExplosion(i == range-1 ? ExplosionId.END_LEFT : ExplosionId.HORIZONTAL);
                    }
                    else tile.createExplosion(i == range-1 ? ExplosionId.END_LEFT : ExplosionId.HORIZONTAL);
                }
            }
            if(!block[3])
            {
                if(column + i >= columns)
                    block[3] = true;
                else
                {
                    tile = tiles[row * columns + (column + i)];
                    if(!tile.canPutPlaceable())
                    {
                        Placeable p = tile.getPlaceable();
                        if(!p.isBomb())
                            block[3] = true;
                        else tile.createExplosion(i == range-1 ? ExplosionId.END_RIGHT : ExplosionId.HORIZONTAL);
                    }
                    else tile.createExplosion(i == range-1 ? ExplosionId.END_RIGHT : ExplosionId.HORIZONTAL);
                }
            }
        }
        
    }
    
    public final void createSquareExplosion(int row, int column, int range)
    {
        if(row < 0 || row >= rows || column < 0 || column >= columns || range < 1)
            throw new IllegalArgumentException();
        int len = range * 2 - 1;
        int r0 = row - range - 1;
        int c0 = column - range - 1;
        
        for(int r=r0;r<len;r++)
        {
            if(r < 0 || r >= rows)
                continue;
            for(int c=c0;c<len;c++)
            {
                if(c < 0 || c >= columns)
                    continue;
                Tile tile = tiles[r * columns + c];
                if(tile.canPutPlaceable())
                    tile.createExplosion(ExplosionId.CROSS);
            }
        }
    }
    
    public final void update(double delta)
    {
        for(Tile tile : tiles)
            tile.update(delta);
    }
    
    public final void draw(Graphics2D g)
    {
        Camera cam = scenario.getCamera();
        Vector2 p0 = cam.getBounds().getPoint0();
        Vector2 p1 = cam.getBounds().getPoint1();
        
        int r = (int) (p0.y / tileSize.y);
        r = r < 0 ? 0 : r;
        int cmin = (int) (p0.x / tileSize.x);
        cmin = cmin < 0 ? 0 : cmin >= columns ? columns : cmin;
        int rmax = (int) (p1.y / tileSize.y) + 1;
        rmax = rmax < 0 ? 0 : rmax >= rows ? rows : rmax;
        int cmax = (int) (p1.x / tileSize.x) + 1;
        cmax = cmax < 0 ? 0 : cmax >= columns ? columns : cmax;
        
        for(;r<rmax;r++)
        {
            //if(r < 0) continue;
            for(int c=cmin;c<cmax;c++)
            {
                //if(c < 0) continue;
                tiles[r * columns + c]
                        .draw(g,
                                position.x + c * tileSize.x,
                                position.y + r * tileSize.y,
                                tileSize.x,
                                tileSize.y);
                if(enabledGrid)
                {
                    g.setColor(Color.red);
                    g.drawRect((int)(position.x + c * tileSize.x),
                                    (int)(position.y + r * tileSize.y),
                                    (int)tileSize.x,
                                    (int)tileSize.y);
                }
            }
        }
    }
    
    public final void reloadSprites(SpriteManager sprites)
    {
        for(Tile tile : tiles)
            tile.reloadSprites(sprites);
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
    
    @Override
    public final void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeInt(rows);
        gds.writeInt(columns);
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
                tiles[r * rows + c].serialize(gds);
        gds.writeVector2(position);
        gds.writeVector2(tileSize);
    }

    @Override
    public final void unserialize(GameDataLoader gdl) throws IOException
    {
        rows = gdl.readInt();
        columns = gdl.readInt();
        tiles = new Tile[rows * columns];
        for(int r=0;r<rows;r++)
            for(int c=0;c<columns;c++)
            {
                Tile tile;
                tiles[r * rows + c] = tile = new Tile(this,r,c,explosion.getReference());
                tile.unserialize(gdl);
            }
        position = gdl.readVector2();
        tileSize = gdl.readVector2();
        computeSize();
    }
    
    private final class TileIterator implements Iterator<Tile>
    {
        private int it = 0;

        @Override
        public final boolean hasNext() { return it < tiles.length; }

        @Override
        public final Tile next() { return tiles[it++]; }
    }
}
