/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.collectible;

import md.games.bomberman.creature.Creature;
import md.games.bomberman.scenario.GameObject;

/**
 *
 * @author Asus
 */
public abstract class Collectible extends GameObject
{
    @Override
    public final boolean isCollectible() { return true; }
    
    public boolean isStack() { return false; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_COLLECTIBLE; }
    
    public abstract void onCollect(Creature creature);
    
    public static final CollectiblesStack join(Collectible c1, Collectible c2)
    {
        if(c1.isStack())
        {
            CollectiblesStack cs = (CollectiblesStack) c1;
            cs.addCollectible(c2);
            return cs;
        }
        CollectiblesStack cs = new CollectiblesStack();
        cs.addCollectible(c1);
        cs.addCollectible(c2);
        return cs;
    }
}
