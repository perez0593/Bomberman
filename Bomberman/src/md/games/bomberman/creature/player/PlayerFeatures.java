/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.creature.player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import md.games.bomberman.bomb.BombType;
import md.games.bomberman.creature.HitPoints;
import md.games.bomberman.sprites.SpriteManager;
import nt.lpl.LPLRuntimeException;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public final class PlayerFeatures extends LPLObject
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
    
    public final Player createPlayer(SpriteManager sprites, String name, PlayerColor color)
    {
        Player player = new Player(sprites,name,color);
        player.getHitPointsManager().set(hp);
        player.setSpeedRatio(speed);
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
    public final void setKevlar(boolean kevlar) { this.kevlar = kevlar; }
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
    
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "hp": return getHitPoints();
            case "speed": return valueOf(getSpeed());
            case "kevlar": return valueOf(getKevlar());
            case "maxBombs": return valueOf(getMaxBombs());
            case "bombRange": return valueOf(getBombRange());
            case "primaryBombType": return valueOf(getPrimaryBombType().name());
            case "secondaryBombType": return valueOf(getSecondaryBombType().name());
        }
    }
    
    @Override
    public final LPLValue setAttribute(LPLValue key, LPLValue value)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "hp": throw new LPLRuntimeException("hp attribute cannot set");
            case "speed":
                setSpeed(value.toJavaDouble());
                return valueOf(getSpeed());
            case "kevlar":
                setKevlar(value.toJavaBoolean());
                return valueOf(getKevlar());
            case "maxBombs":
                setMaxBombs(value.toJavaInt());
                return valueOf(getMaxBombs());
            case "bombRange":
                setBombRange(value.toJavaInt());
                return valueOf(getBombRange());
            case "primaryBombType":
                setPrimaryBombType(BombType.decode(value));
                return valueOf(getPrimaryBombType().name());
            case "secondaryBombType":
                setSecondaryBombType(BombType.decode(value));
                return valueOf(getSecondaryBombType().name());
        }
    }
}
