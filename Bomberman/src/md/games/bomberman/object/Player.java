/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;
import java.util.ListIterator;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.object.bomb.BombBuilder;
import md.games.bomberman.object.bomb.BombType;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.action.Action;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public class Player extends Creature
{
    private String name;
    private PlayerColor color;
    
    /*stats*/
    private int lives;
    private double speed;
    private int maxBombs;
    private int bombRange;
    private BombType primaryBombType;
    private BombType secondaryBombType;
    /*stats*/
    
    private final LinkedList<Bomb> bombs = new LinkedList<>();
    
    public Player(String name, PlayerColor color)
    {
        if(name == null || color == null)
            throw new NullPointerException();
        this.name = name;
        this.color = color;
        speed = 1f;
        maxBombs = 1;
        bombRange = 3;
        primaryBombType = BombType.NORMAL;
        secondaryBombType = BombType.NORMAL;
    }
    
    private Player() {}
    
    public final String getName() { return name; }
    public final PlayerColor getColor() { return color; }
    public final int getLives() { return lives; }
    public final double getSpeed() { return speed; }
    public final int getMaxBombs() { return maxBombs; }
    public final BombType getPrimaryBombType() { return primaryBombType; }
    public final BombType getSecondaryBombType() { return secondaryBombType; }
    
    @Override
    protected void innerDestroy() {}
    
    public final void putBomb(boolean primary)
    {
        if(!hasScenarioReference() || bombs.size() >= maxBombs)
            return;
        Scenario scenario = getScenarioReference();
        Tile tile = scenario.getTileManager().getLookAtTile(this);
        if(tile == null || !tile.canPutPlaceable())
            return;
        Bomb bomb = BombBuilder.createBomb(primary ? primaryBombType : secondaryBombType,bombRange);
        scenario.registerGameObject(bomb);
        scenario.sendAction(Action.playerPutBomb(getId(),bomb.getId(),primary ? primaryBombType : secondaryBombType,bombRange,tile));
        bombs.add(bomb);
        tile.putPlaceable(bomb);
    }
    
    
    @Override
    public final CreatureType getCreatureType() { return CreatureType.PLAYER; }

    @Override
    public void update(double delta)
    {
        updateBombs();
    }
    
    private void updateBombs()
    {
        if(bombs.isEmpty())
            return;
        ListIterator<Bomb> it = bombs.listIterator();
        while(!it.hasNext())
        {
            Bomb bomb = it.next();
            if(bomb.hasExploited() || bomb.isDestroid())
                it.remove();
        }
    }

    @Override
    public void draw(Graphics2D g)
    {
        
    }

    @Override
    protected void innserSerialize(GameDataSaver gds) throws IOException
    {
        gds.writeUTF(name);
        gds.writeInt(color.ordinal());
        gds.writeInt(lives);
        gds.writeDouble(speed);
        gds.writeInt(maxBombs);
        gds.writeInt(bombRange);
        gds.writeInt(primaryBombType.ordinal());
        gds.writeInt(secondaryBombType.ordinal());
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        
        name = gdl.readUTF();
        color = PlayerColor.decode(gdl.readInt());
        lives = gdl.readInt();
        speed = gdl.readDouble();
        maxBombs = gdl.readInt();
        bombRange = gdl.readInt();
        primaryBombType = BombType.decode(gdl.readInt());
        secondaryBombType = BombType.decode(gdl.readInt());
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        return null;
    }
    
}
