/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.action;

/**
 *
 * @author Asus
 */
public enum ActionId
{
    MOVE_OBJECT,
    SET_OBJECT_POSITION,
    CREATE_BOMB,
    PLAYER_PUT_BOMB,
    //EXPLODE_BOMB,
    //CREATE_FIRE_EXPLOSION,
    COLLECT_COLLECTIBLE,
    COLLECT_COLLECTIBLE_ON_TILE,
    //COLLIDE_PLACEABLE,
    DAMAGE_CREATURE,
    EXECUTE_SCRIPT;
    
    private static final ActionId[] VALUES = values();
    
    public static final ActionId decode(int ordinal) { return VALUES[ordinal]; }
}
