/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author mpasc
 */
public final class Explosion
{
    private final BufferedImage[][] ssprites;
    private float maxDelay;
    private float speed;
    private final int width, height;
    
    private Explosion(BufferedImage[][] ssprites, int width, int height)
    {
        this.ssprites = ssprites;
        maxDelay = -1;
        speed = 1f;
        this.width = width;
        this.height = height;
    }
    
    //TODO: Cargar y asignar los sprites de las explosiones
    public static final Explosion getManager()
    {
        return null;
    }
    
    public final ExplosionReference getReference() { return new ExplosionReference(); }
    
    public void setDelay(float seconds)
    {
        maxDelay = seconds;
    }
    public double getDelay() { return maxDelay; }

    public void setSpeed(float speed)
    {
        this.speed = speed;
    }
    
    public final class ExplosionReference
    {
        private ExplosionId explosionId;
        private float iterator;
        private float delay = maxDelay;
        private boolean end = true;
        
        public final void explode(ExplosionId explosionId)
        {
            if(explosionId == null)
                throw new NullPointerException();
            this.explosionId = this.explosionId != null
                    ? this.explosionId.join(explosionId) : explosionId;
            delay = maxDelay;
            end = false;
            iterator = 0;
        }
        
        public boolean isEnd() { return end; }
    
        private boolean canContinue()
        {
            return iterator >= 0d && iterator < ssprites[explosionId.id].length;
        }

        private void reallocIterator()
        {
            if(end || !canContinue())
                    iterator = 0f;
        }

        public void update(double delta)
        {
            delay -= delta;
            if(delay <= 0d)
            {
                delay = maxDelay;
                iterator += delta * speed;
            }
            if(canContinue()) return;
            end = true;
        }

        public void draw(Graphics2D g, AffineTransform transf)
        {
            reallocIterator();
            g.drawImage(ssprites[explosionId.id][(int)iterator],transf,null);
        }
        
        public final void draw(Graphics2D g, double x, double y, double w, double h)
        {
            AffineTransform af = new AffineTransform();
            af.translate(x,y);
            af.scale(w/width,h/height);
            draw(g,af);
        }
    }
    
    public enum ExplosionId
    {
        END_UP,
        END_DOWN,
        END_LEFT,
        END_RIGHT,
        VERTICAL,
        HORIZONTAL,
        CROSS;
        
        private final int id = ordinal();
        
        public final ExplosionId join(ExplosionId id)
        {
            switch(this)
            {
                case END_UP:
                    switch(id)
                    {
                        case END_UP: return END_UP;
                        case END_DOWN: case VERTICAL: return VERTICAL;
                        default: return CROSS;
                    }
                case END_DOWN:
                    switch(id)
                    {
                        case END_DOWN: return END_DOWN;
                        case END_UP: case VERTICAL: return VERTICAL;
                        default: return CROSS;
                    }
                case END_LEFT:
                    switch(id)
                    {
                        case END_LEFT: return END_LEFT;
                        case END_RIGHT: case HORIZONTAL: return HORIZONTAL;
                        default: return CROSS;
                    }
                case END_RIGHT:
                    switch(id)
                    {
                        case END_RIGHT: return END_RIGHT;
                        case END_LEFT: case HORIZONTAL: return HORIZONTAL;
                        default: return CROSS;
                    }
                case VERTICAL:
                    switch(id)
                    {
                        case END_UP: case END_DOWN: case VERTICAL: return VERTICAL;
                        default: return CROSS;
                    }
                case HORIZONTAL:
                    switch(id)
                    {
                        case END_LEFT: case END_RIGHT: case HORIZONTAL: return HORIZONTAL;
                        default: return CROSS;
                    }
                case CROSS:
                default: return CROSS;
            }
        }
    }
}
