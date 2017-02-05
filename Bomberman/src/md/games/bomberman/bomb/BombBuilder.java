/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.bomb;

import java.util.Random;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.TileManager;
import md.games.bomberman.sprites.AnimatedSprite;
import md.games.bomberman.sprites.Sprite;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.Constants;

/**
 *
 * @author Asus
 */
public final class BombBuilder
{
    private final SpriteManager sprites;
    
    public BombBuilder(SpriteManager sprites)
    {
        if(sprites == null)
            throw new NullPointerException();
        this.sprites = sprites;
    }
    
    public static final int BOMB_DAMAGE = 1;
    public static final double BOMB_TIME_TO_EXPLODE = 5f; //seconds
    
    private static final Random RAND = new Random();
    
    public final Bomb createBomb(BombType type, int range)
    {
        if(range < 1)
            throw new IllegalArgumentException();
        switch(type)
        {
            default: throw new IllegalStateException();
            case NORMAL: return new Bomb(range, BOMB_DAMAGE, false, normalBombSprite(sprites), BOMB_TIME_TO_EXPLODE);
            case SPIKES: return new Bomb(range * 2, BOMB_DAMAGE, false, normalBombSprite(sprites), BOMB_TIME_TO_EXPLODE);
            /*TODO*/case THROWABLE: return null;
            case FASTEXPOLDE: return new Bomb(range, BOMB_DAMAGE, false, normalBombSprite(sprites), BOMB_TIME_TO_EXPLODE / 2);
            case C4: return new Bomb(range, BOMB_DAMAGE, true, normalBombSprite(sprites));
            case HIGHRANGE: return new SquareBomb(sprites, range, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
            /*TODO*/case NINJA: return null;
            case MINE: return new MineBomb(range, BOMB_DAMAGE);
            case HEAL: return new Bomb(range, -1, false, normalBombSprite(sprites), BOMB_TIME_TO_EXPLODE);
            /*TODO*/case ICEBOMB: return null;
            case TELEPORT: return new TeleportBomb(sprites, range, BOMB_DAMAGE, false, BOMB_TIME_TO_EXPLODE);
        }
    }
    
    private static Sprite normalBombSprite(SpriteManager sprites)
    {
        Sprite s = sprites.getSprite(Constants.SPRITE_BOMB_NORMAL);
        if(!s.isAnimatedSprite())
            return s;
        AnimatedSprite as = (AnimatedSprite) s;
        as.setLoop(true);
        as.setSpeed(6);
        as.start();
        return as;
    }
    
    private static final class SquareBomb extends Bomb
    {
        public SquareBomb(SpriteManager sprites, int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode, normalBombSprite(sprites));
        }

        public SquareBomb(SpriteManager sprites, int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, normalBombSprite(sprites), timeToExplode);
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
            super(range, damage, false, null);
        }
        
        private MineBomb() {}
        
        @Override
        public void onCreatureCollide(Creature creature) { explode(); }
    }
    
    private static final class TeleportBomb extends Bomb
    {
        public TeleportBomb(SpriteManager sprites, int range, int damage, boolean remoteMode)
        {
            super(range, damage, remoteMode, normalBombSprite(sprites));
        }

        public TeleportBomb(SpriteManager sprites, int range, int damage, boolean remoteMode, double timeToExplode)
        {
            super(range, damage, remoteMode, normalBombSprite(sprites), timeToExplode);
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
