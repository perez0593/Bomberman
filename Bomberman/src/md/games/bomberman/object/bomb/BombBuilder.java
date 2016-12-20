/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.bomb;

import java.util.Random;
import md.games.bomberman.object.Bomb;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;

/**
 *
 * @author Asus
 */
public final class BombBuilder
{
    private BombBuilder() {}
    
    public static final int BOMB_RANGE = 3;
    public static final int BOMB_DAMAGE = 1;
    public static final double BOMB_TIME_TO_EXPLODE = 4f; //seconds
    
    private static final Random RAND = new Random();
    
    public static final Bomb createBomb(BombType type)
    {
        switch(type)
        {
            default: throw new IllegalStateException();
            case NORMAL: return new Bomb(BOMB_RANGE, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
            case SPIKES: return new Bomb(BOMB_RANGE * 2, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case THROWABLE: return null;
            case FASTEXPOLDE: return new Bomb(BOMB_RANGE, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE / 2);
            case C4: return new Bomb(BOMB_RANGE, BOMB_DAMAGE, true);
            case HIGHRANGE: return new SquareBomb(BOMB_RANGE, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case NINJA: return null;
            /*TODO*/case MINE: return null;
            case HEAL: return new Bomb(BOMB_RANGE, -1, false, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case ICEBOMB: return null;
            case TELEPORT: return new TeleportBomb(BOMB_RANGE, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
        }
    }
    
    private static final class SquareBomb extends Bomb
    {
        public SquareBomb(int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode);
        }

        public SquareBomb(int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, timeToExplode);
        }
        
        @Override
        protected final void explode(TileManager tiles, Tile tileOnPlaced)
        {
            tiles.createSquareExplosion(tileOnPlaced.getRow(),tileOnPlaced.getColumn(),range);
        }
    }
    
    private static final class TeleportBomb extends Bomb
    {
        public TeleportBomb(int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode);
        }

        public TeleportBomb(int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, timeToExplode);
        }
        
        @Override
        protected final void explode(TileManager tiles, Tile tileOnPlaced)
        {
            int row = RAND.nextInt(tiles.getRows());
            int column = RAND.nextInt(tiles.getColumns());
            tiles.createCrossExplosion(row,column,range);
        }
    }
}
