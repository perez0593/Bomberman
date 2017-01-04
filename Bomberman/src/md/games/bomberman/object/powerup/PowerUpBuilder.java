/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object.powerup;

import md.games.bomberman.object.Creature;
import md.games.bomberman.object.Player;
import md.games.bomberman.object.PowerUp;

/**
 *
 * @author Asus
 */
public final class PowerUpBuilder
{
    /*public static final PowerUp createPowerUp(PowerUpType type)
    {
        
    }*/
    
    private static final class RangeUp extends PowerUp
    {
        private RangeUp()
        {
            
        }
        
        @Override
        public final void onCollect(Creature creature)
        {
            if(creature.isPlayer())
                ((Player)creature).increaseBombRange(1);
        }
    }
}
