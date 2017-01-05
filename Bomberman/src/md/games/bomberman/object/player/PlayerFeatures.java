/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.player;

import md.games.bomberman.object.bomb.BombType;
import md.games.bomberman.object.creature.HitPoints;

/**
 *
 * @author Asus
 */
public final class PlayerFeatures
{
    private final HitPoints hp;
    private double speed;
    private boolean kevlar;
    private int maxBombs;
    private int bombRange;
    private BombType primaryBombType;
    private BombType secondaryBombType;
    
    public PlayerFeatures()
    {
        hp = new HitPoints(1);
        speed = 1f;
        kevlar = false;
        maxBombs = 1;
        bombRange = 3;
        primaryBombType = BombType.NORMAL;
        secondaryBombType = BombType.NORMAL;
    }
    private PlayerFeatures(PlayerFeatures other)
    {
        hp = other.hp.copy();
        speed = other.speed;
        kevlar = other.kevlar;
        maxBombs = other.maxBombs;
        bombRange = other.bombRange;
        primaryBombType = other.primaryBombType;
        secondaryBombType = other.secondaryBombType;
    }
    
    public final PlayerFeatures copy() { return new PlayerFeatures(this); }
    
    public final HitPoints getHitPoints() { return hp; }
    public final double getSpeed() { return speed; }
    public final boolean getKevlar() { return kevlar; }
    public final int getMaxBombs() { return maxBombs; }
    public final int getBombRange() { return bombRange; }
    public final BombType getPrimaryBombType() { return primaryBombType; }
    public final BombType getSecondaryBombType() { return secondaryBombType; }
    
    public final void setSpeed(double speed) { this.speed = speed < 0 ? 0 : speed; }
}
