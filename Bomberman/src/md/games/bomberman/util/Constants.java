/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import nt.dal.data.DALData;

/**
 *
 * @author Asus
 */
public final class Constants
{
    private Constants() {}
    
    /* DAL STRINGS */
    public static final DALData DAL_STR_CLASS = DALData.valueOf("__class");
    
    public static final Version VERSION = Version.valueOf("0.0.0.1");
    
    
    public static final int PLAYER_WIDTH = 32;
    public static final int PLAYER_HEIGHT = 56;
    
    public static final int PLAYER_AABB_WIDTH = 32;
    public static final int PLAYER_AABB_HEIGHT = 32;
    
    public static final int TILE_WIDTH = 50;
    public static final int TILE_HEIGHT = 50;
    
    
    
    public static final int CREATURE_ANIMATION_STOPPED = 0;
    public static final int CREATURE_ANIMATION_WALK_UP = 3;
    public static final int CREATURE_ANIMATION_WALK_LEFT = 2;
    public static final int CREATURE_ANIMATION_WALK_DOWN = 1;
    public static final int CREATURE_ANIMATION_WALK_RIGHT = 2;
    
    
}
