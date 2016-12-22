/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

/**
 *
 * @author Asus
 */
public abstract class Collectible extends GameObject
{
    @Override
    public final boolean isCollectible() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_COLLECTIBLE; }
    
    public abstract void onCollect(Creature creature);
}
