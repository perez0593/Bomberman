/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.particle;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.sprites.Sprite;

/**
 *
 * @author mpasc
 */
public class Particle
{
    protected static final AlphaComposite __ALPHA = AlphaComposite.SrcOver;
    
    public final Vector2 position, speed, acceleration;
    public final Vector2 size;
    public Sprite sprite;
    public float angle, angularSpeed, alpha, alphaSpeed, ttl;
    
    public Particle(Sprite sprite)
    {
        position = new Vector2();
        speed = new Vector2();
        acceleration = new Vector2();
        size = new Vector2();
        this.sprite = sprite;
        angle = 0f;
        angularSpeed = 0f;
        alpha = 1f;
        alphaSpeed = 0f;
        ttl = 0f;
    }
    public Particle()
    {
        this(null);
    }
    
    public void update(double delta)
    {
        if(ttl <= 0f) return;
        ttl -= delta;
        position.x += speed.x * delta;
        position.y += speed.y * delta;
        speed.x += acceleration.x * delta;
        speed.y += acceleration.y * delta;
        angle += angularSpeed * delta;
        alpha += alphaSpeed * delta;
        alpha = alpha > 1f ? 1f : alpha < 0f ? 0f : alpha;
        if(sprite != null)
            sprite.update(delta);
    }
    
    public void draw(Graphics2D g)
    {
        if(sprite == null || ttl <= 0f) return;
        if(alpha != 1f)
        {
            Composite old = g.getComposite();
            g.setComposite(__ALPHA.derive(alpha));
            sprite.draw(g,position.x,position.y,size.x,size.y,angle);
            g.setComposite(old);
            return;
        }
        sprite.draw(g,position.x,position.y,size.x,size.y,angle);
    }
    
    /*public final boolean isOutOfScreen()
    {
        return position.x + size.x < 0 ||
                position.x > (Utils.NATIVE_WIDTH + size.x) ||
                position.y + size.y < 0 ||
                position.y > (Utils.NATIVE_HEIGHT + size.y);
    }*/
}
