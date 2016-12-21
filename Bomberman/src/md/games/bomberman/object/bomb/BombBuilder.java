/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.bomb;

import java.util.Random;
import md.games.bomberman.object.Bomb;
import md.games.bomberman.object.Player;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;

/**
 *
 * @author Asus
 */
public final class BombBuilder
{
    private BombBuilder() {}
    
    public static final int BOMB_DAMAGE = 1;
    public static final double BOMB_TIME_TO_EXPLODE = 5f; //seconds
    
    private static final Random RAND = new Random();
    
    public static final Bomb createBomb(BombType type, int range)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        switch(type)
        {
            default: throw new IllegalStateException();
            case NORMAL: return new Bomb(range, BOMB_DAMAGE, false, true, BOMB_TIME_TO_EXPLODE);
            case SPIKES: return new Bomb(range * 2, BOMB_DAMAGE, false, true, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case THROWABLE: return null;
            case FASTEXPOLDE: return new Bomb(range, BOMB_DAMAGE, false, true, BOMB_TIME_TO_EXPLODE / 2);
            case C4: return new Bomb(range, BOMB_DAMAGE, true, true);
            case HIGHRANGE: return new SquareBomb(range, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case NINJA: return null;
            case MINE: return new MineBomb(range, BOMB_DAMAGE);
            case HEAL: return new Bomb(range, -1, false, true, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case ICEBOMB: return null;
            case TELEPORT: return new TeleportBomb(range, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
        }
    }
    
    private static final class SquareBomb extends Bomb
    {
        public SquareBomb(int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode, true);
        }

        public SquareBomb(int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, true, timeToExplode);
        }
        
        private SquareBomb() {}
        
        @Override
        protected final void explode(TileManager tiles, Tile tileOnPlaced)
        {
            tiles.createSquareExplosion(tileOnPlaced.getRow(),tileOnPlaced.getColumn(),range);
        }
    }
    
    private static final class MineBomb extends Bomb
    {
        public MineBomb(int range, int damage)
        {
            super(range, damage, false, false);
        }
        
        private MineBomb() {}
        
        @Override
        public void onPlayerCollide(Player player) { explode(); }
    }
    
    private static final class TeleportBomb extends Bomb
    {
        public TeleportBomb(int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode, true);
        }

        public TeleportBomb(int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, true, timeToExplode);
        }
        
        private TeleportBomb() {}
        
        @Override
        protected final void explode(TileManager tiles, Tile tileOnPlaced)
        {
            int row = RAND.nextInt(tiles.getRows());
            int column = RAND.nextInt(tiles.getColumns());
            tiles.createCrossExplosion(row,column,range);
        }
    }
}
