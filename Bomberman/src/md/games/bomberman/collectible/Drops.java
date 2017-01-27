/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.collectible;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.io.SerializableObject;
import md.games.bomberman.util.RNG;

/**
 *
 * @author Asus
 */
public final class Drops implements Iterable<Drops.Drop>, SerializableObject
{
    private final LinkedList<Drop> drops = new LinkedList<>();
    
    public final Drop registerDrop(CollectibleId id, double probabilities)
    {
        Drop d;
        drops.add(d = new Drop(id,probabilities));
        return d;
    }
    
    public final Drop getDrop(int index) { return drops.get(index); }
    
    public final int getDropCount() { return drops.size(); }
    
    public final boolean removeDrop(Drop drop)
    {
        return drops.remove(drop);
    }
    
    public final Collectible generateDrop(RNG rng)
    {
        if(drops.isEmpty())
            return null;
        if(drops.size() == 1)
            return drops.getFirst().build(rng);
        Collectible c = null;
        for(Drop drop : drops)
        {
            if(c == null)
                c = drop.build(rng);
            else
            {
                Collectible c2 = drop.build(rng);
                if(c2 != null)
                    c = Collectible.join(c,c2);
            }
        }
        return c;
    }

    @Override
    public final Iterator<Drop> iterator() { return drops.iterator(); }

    @Override
    public void serialize(GameDataSaver gds) throws IOException
    {
        gds.writeInt(drops.size());
        for(Drop drop : drops)
        {
            gds.writeInt(drop.id.asInt());
            gds.writeDouble(drop.prob);
        }
    }

    @Override
    public void unserialize(GameDataLoader gdl) throws IOException
    {
        int len = gdl.readInt();
        for(int i=0;i<len;i++)
            drops.add(new Drop(CollectibleId.decode(gdl.readInt()),gdl.readDouble()));
    }
    
    public static final class Drop
    {
        private final CollectibleId id;
        private double prob;
        
        private Drop(CollectibleId id, double prob)
        {
            this.id = id;
            this.prob = prob < 0d ? 0d : prob > 1d ? 1d : prob;
        }
        private Drop(CollectibleId id)
        {
            this.id = id;
            this.prob = 1d;
        }
        
        public final CollectibleId getId() { return id; }
        public final void setProbability(double prob) { this.prob = prob < 0d ? 0d : prob > 1d ? 1d : prob; }
        public final double getProbability() { return prob; }
        
        private Collectible build(RNG rng)
        {
            return rng.getDouble() < prob ? CollectibleFactory.create(id) : null;
        }
    }
}
