/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import md.games.bomberman.object.powerup.PowerUpType;
import java.awt.Graphics2D;
import java.io.IOException;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import md.games.bomberman.sprites.Sprite;
import md.games.bomberman.sprites.SpriteUtils;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public abstract class PowerUp extends Collectible
{
    protected Sprite sprite;
    
    @Override
    protected void innerDestroy() {}

    @Override
    public void update(double delta)
    {
        if(sprite != null)
            sprite.update(delta);
    }

    @Override
    public void draw(Graphics2D g)
    {
        if(sprite != null)
            SpriteUtils.drawGameObjectSprite(this,g,sprite);
    }

    @Override
    protected void innserSerialize(GameDataSaver gds) throws IOException
    {
        gds.writeIfNonNull(sprite,() -> gds.writeSprite(sprite));
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) throws IOException
    {
        gdl.readIfNonNull(() -> sprite = gdl.readSprite());
    }

    @Override
    protected LPLValue getAttribute(String key)
    {
        return null;
    }
    
}
