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
public abstract class Creature extends GameObject
{
    @Override
    public final boolean isCreature() { return true; }
    
    @Override
    public final int getGameObjectType() { return GameObject.GAME_OBJECT_TYPE_CREATURE; }
    
    public abstract CreatureType getCreatureType();
    
    public final boolean isPlayer() { return getCreatureType() == CreatureType.PLAYER; }
}
