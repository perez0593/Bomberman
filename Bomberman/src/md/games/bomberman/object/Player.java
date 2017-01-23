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
import java.util.Objects;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.object.bomb.BombType;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.Tile;
import md.games.bomberman.scenario.action.Action;
import md.games.bomberman.sprites.Sprite.FlipMode;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.Constants;
import md.games.bomberman.util.Direction;
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
    private boolean kevlar;
    private int maxBombs;
    private int bombRange;
    private BombType primaryBombType;
    private BombType secondaryBombType;
    /*stats*/
    
    private final LinkedList<Bomb> bombs = new LinkedList<>();
    
    public Player(SpriteManager sprites, String name, PlayerColor color)
    {
        if(name == null || color == null)
            throw new NullPointerException();
        this.name = name;
        this.color = color;
        kevlar = false;
        maxBombs = 1;
        bombRange = 3;
        primaryBombType = BombType.NORMAL;
        secondaryBombType = BombType.NORMAL;
        
        loadAnimation(sprites,"bomberman." + color.getLowName());
        setAnimationSize(Constants.PLAYER_WIDTH,Constants.PLAYER_HEIGHT);
        animation.setSpeed(10);
        init();
    }
    
    private Player()
    {
        init();
    }
    
    private void init()
    {
        setSize(Constants.PLAYER_AABB_WIDTH,Constants.PLAYER_AABB_HEIGHT);
        createBoundingBox();
    }
    
    public final String getName() { return name; }
    public final PlayerColor getColor() { return color; }
    public final boolean hasKevlar() { return kevlar; }
    public final int getMaxBombs() { return maxBombs; }
    public final BombType getPrimaryBombType() { return primaryBombType; }
    public final BombType getSecondaryBombType() { return secondaryBombType; }
    
    public final void setName(String name) { this.name = Objects.requireNonNull(name); }
    public final void setMaxBombs(int max) { maxBombs = max < 0 ? 0 : max; }
    public final void setBombRange(int range) { bombRange = range < 1 ? 1 : range; }
    public final void setKevlar(boolean flag) { kevlar = flag; }
    public final void setPrimaryBombType(BombType type)
    {
        if(type == null)
            throw new NullPointerException();
        primaryBombType = type;
    }
    public final void setSecondaryBombType(BombType type)
    {
        if(type == null)
            throw new NullPointerException();
        secondaryBombType = type;
    }
    
    public final void increaseMaxBombs(int amount) { maxBombs += amount < 0 ? -amount : amount; }
    public final void decreaseMaxBombs(int amount)
    {
        maxBombs -= amount < 0 ? -amount : amount;
        maxBombs = maxBombs < 0 ? 0 : maxBombs;
    }
    
    public final void increaseBombRange(int amount) { bombRange += amount < 0 ? -amount : amount; }
    public final void decreaseBombRange(int amount)
    {
        bombRange -= amount < 0 ? -amount : amount;
        bombRange = bombRange < 1 ? 1 : bombRange;
    }
    
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
        Bomb bomb = scenario.getBombBuilder().createBomb(primary ? primaryBombType : secondaryBombType,bombRange);
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
        super.update(delta);
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
        double x = getPositionX() - Constants.PLAYER_AABB_WIDTH / 2;
        double y = getPositionY() - (Constants.PLAYER_HEIGHT - Constants.PLAYER_AABB_HEIGHT / 2);
        double w = Constants.PLAYER_WIDTH;
        double h = Constants.PLAYER_HEIGHT;
        if(getWalkingDirection() == Direction.LEFT)
            animation.draw(g,FlipMode.HORIZONTAL,x,y,w,h);
        else animation.draw(g,x,y,w,h);
    }
    
    @Override
    public final void die()
    {
        
    }

    @Override
    protected void innerSerialize(GameDataSaver gds) throws IOException
    {
        super.innerSerialize(gds);
        gds.writeUTF(name);
        gds.writeInt(color.ordinal());
        gds.writeBoolean(kevlar);
        gds.writeInt(maxBombs);
        gds.writeInt(bombRange);
        gds.writeInt(primaryBombType.ordinal());
        gds.writeInt(secondaryBombType.ordinal());
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        super.innerUnserialize(gdl);
        name = gdl.readUTF();
        color = PlayerColor.decode(gdl.readInt());
        kevlar = gdl.readBoolean();
        maxBombs = gdl.readInt();
        bombRange = gdl.readInt();
        primaryBombType = BombType.decode(gdl.readInt());
        secondaryBombType = BombType.decode(gdl.readInt());
        loadAnimation(gdl.getSpriteManager(),"bomberman." + color.getLowName());
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        return null;
    }
    
}
