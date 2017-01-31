/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.placeable;

import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.TileManager;
import md.games.bomberman.sprites.SpriteManager;
import md.games.bomberman.util.Constants;

/**
 *
 * @author Asus
 */
public final class RockFactory
{
    private RockFactory() {}
    
    public static final Rock create(SpriteManager sprites, RockType type, double width, double height)
    {
        switch(type)
        {
            default: throw new IllegalStateException();
            case UNBREAKABLE:
                return new Rock(sprites.getSprite(Constants.THEME_ROCK_UNBREAKABLE),-1,width,height);
            case BREAKABLE_WEAK:
                return new Rock(sprites.getSprite(Constants.THEME_ROCK_BREAKABLE_WEAK),1,width,height);
            case BREAKABLE_HARD:
                return new Rock(sprites.getSprite(Constants.THEME_ROCK_BREAKABLE_HARD),2,width,height);
        }
    }
    public static final Rock create(SpriteManager sprites, RockType type, Scenario scenario)
    {
        TileManager tiles = scenario.getTileManager();
        return create(sprites,type,tiles.getTileWidth(),tiles.getTileHeight());
    }
    public static final Rock create(SpriteManager sprites, RockType type, TileManager tiles)
    {
        return create(sprites,type,tiles.getTileWidth(),tiles.getTileHeight());
    }
}
