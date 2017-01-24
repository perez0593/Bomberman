/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.placeable;

import java.awt.Graphics2D;
import java.io.IOException;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.sprites.Sprite;

/**
 *
 * @author Asus
 */
public class Rock extends Placeable
{
    private Sprite sprite;
    private boolean indestructible;
    private int resistence;
    
    private Rock(Sprite sprite, int resistence)
    {
        this.sprite = sprite;
        this.indestructible = resistence <= 0;
        this.resistence = resistence;
    }
    
    @Override
    public void onExplodeHit()
    {
        if(indestructible)
            return;
        resistence--;
        if(resistence > 0)
            return;
        destroy();
    }

    @Override
    public void onCreatureCollide(Creature creature)
    {
        
    }

    @Override
    public final boolean canCreatureWalk(Creature creature) { return false; }

    @Override
    public void update(double delta)
    {
        
    }

    @Override
    public void draw(Graphics2D g)
    {
        
    }

    @Override
    protected void innerSerialize(GameDataSaver gds) throws IOException
    {
        gds.writeSprite(sprite);
        gds.writeBoolean(indestructible);
        gds.writeInt(resistence);
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        sprite = gdl.readSprite();
        indestructible = gdl.readBoolean();
        resistence = gdl.readInt();
    }
    
}
