/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.placeable;

import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.TileManager;
import md.games.bomberman.sprites.SpriteManager;

/**
 *
 * @author Asus
 */
public final class RockFactory
{
    private RockFactory() {}
    
    public static final Rock create(SpriteManager sprites, RockId id, double width, double height)
    {
        switch(id)
        {
            default: throw new IllegalStateException();
            case UNBREAKABLE:
                return new Rock(sprites.getSprite("theme.rock.unbreakable"),-1,width,height);
        }
    }
    public static final Rock create(SpriteManager sprites, RockId id, Scenario scenario)
    {
        TileManager tiles = scenario.getTileManager();
        return create(sprites,id,tiles.getTileWidth(),tiles.getTileHeight());
    }
    public static final Rock create(SpriteManager sprites, RockId id, TileManager tiles)
    {
        return create(sprites,id,tiles.getTileWidth(),tiles.getTileHeight());
    }
    
    public enum RockId
    {
        UNBREAKABLE;
    }
}
