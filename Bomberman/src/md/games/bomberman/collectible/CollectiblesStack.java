/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.collectible;

import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;
import md.games.bomberman.creature.Creature;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public class CollectiblesStack extends Collectible
{
    private final LinkedList<Collectible> stack = new LinkedList<>();
    
    public final void addCollectible(Collectible c)
    {
        if(c.isStack())
            stack.addAll(((CollectiblesStack)c).stack);
        else stack.add(c);
    }
    
    public final void addCollectible(CollectiblesStack cstack)
    {
        stack.addAll(cstack.stack);
    }
    
    @Override
    public final boolean isStack() { return true; }
    
    @Override
    public void onCollect(Creature creature)
    {
        while(!stack.isEmpty())
        {
            Collectible c = stack.pop();
            c.onCollect(creature);
        }
    }

    @Override
    protected void innerDestroy()
    {
        for(Collectible c : stack)
            c.destroy();
        stack.clear();
    }

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
        gds.writeInt(stack.size());
        for(Collectible c : stack)
            gds.writeSerializableObject(c);
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
            stack.add(gdl.readSerializableObject());
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        return null;
    }
    
}
