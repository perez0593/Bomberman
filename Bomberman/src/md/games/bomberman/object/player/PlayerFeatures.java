/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import md.games.bomberman.object.Player;
import md.games.bomberman.object.PlayerColor;
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
    private PlayerFeatures(DataInputStream dis) throws IOException
    {
        hp = new HitPoints(1);
        unserialize(dis);
    }
    
    public final Player createPlayer(String name, PlayerColor color)
    {
        Player player = new Player(name,color);
        player.getHitPointsManager().set(hp);
        player.setSpeed(speed);
        player.setKevlar(kevlar);
        player.setMaxBombs(maxBombs);
        player.setBombRange(bombRange);
        player.setPrimaryBombType(primaryBombType);
        player.setSecondaryBombType(primaryBombType);
        return player;
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
    public final void setkevlar(boolean kevlar) { this.kevlar = kevlar; }
    public final void setMaxBombs(int max) { this.maxBombs = max; }
    public final void setBombRange(int range) { this.bombRange = range; }
    public final void setPrimaryBombType(BombType type) { this.primaryBombType = type; }
    public final void setSecondaryBombType(BombType type) { this.secondaryBombType = type; }
    
    public final void serialize(DataOutputStream dos) throws IOException
    {
        hp.serialize(dos);
        dos.writeDouble(speed);
        dos.writeBoolean(kevlar);
        dos.writeInt(maxBombs);
        dos.writeInt(bombRange);
        dos.writeInt(primaryBombType.ordinal());
        dos.writeInt(secondaryBombType.ordinal());
    }
    
    public final void unserialize(DataInputStream dis) throws IOException
    {
        hp.unserialize(dis);
        speed = dis.readInt();
        kevlar = dis.readBoolean();
        maxBombs = dis.readInt();
        bombRange = dis.readInt();
        primaryBombType = BombType.decode(dis.readInt());
        secondaryBombType = BombType.decode(dis.readInt());
    }
    
    public static final PlayerFeatures read(DataInputStream dis) throws IOException { return new PlayerFeatures(dis); }
}
