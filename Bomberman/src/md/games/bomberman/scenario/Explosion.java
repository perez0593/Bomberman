/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import md.games.bomberman.io.Resource;

/**
 *
 * @author mpasc
 */
public final class Explosion
{
    private final BufferedImage ssprites;
    private float maxDelay;
    private float speed;
    private final int width, height;
    private final int length;
    
    private Explosion(BufferedImage ssprites, int width, int height, int length)
    {
        this.ssprites = ssprites;
        maxDelay = -1;
        speed = 15f;
        this.width = width;
        this.height = height;
        this.length = length;
    }
    
    //TODO: Cargar y asignar los sprites de las explosiones
    public static final Explosion getManager()
    {
        try
        {
            BufferedImage i = Resource.SPRITES.loadRawImage("explosions.png");
            return new Explosion(i,20,20,7);
        }
        catch(IOException ex)
        {
            ex.printStackTrace(System.err);
            return new Explosion(null,1,1,1);
        }
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
            return iterator >= 0d && iterator < length;
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
            explosionId = null;
            end = true;
        }

        /*public void draw(Graphics2D g, AffineTransform transf)
        {
            reallocIterator();
            int x0 = ((int)iterator) * width;
            int y0 = explosionId.id * height;
            int x1 = x0 + width;
            int y1 = y0 + height;
            transf.rotate(explosionId.radians);
            AffineTransform old = g.getTransform();
            g.transform(transf);
            g.drawImage(ssprites,0,0,width,height,x0,y0,x1,y1,null);
            g.setTransform(old);
        }*/
        
        public final void draw(Graphics2D g, double x, double y, double w, double h)
        {
            reallocIterator();
            AffineTransform af = new AffineTransform();
            int x0 = ((int)iterator) * width;
            int y0 = explosionId.id * height;
            int x1 = x0 + width;
            int y1 = y0 + height;
            af.translate(x,y);
            af.rotate(explosionId.radians,w/2,h/2);
            af.scale(w/width,h/height);
            AffineTransform old = g.getTransform();
            g.transform(af);
            g.drawImage(ssprites,0,0,width,height,x0,y0,x1,y1,null);
            g.setTransform(old);
        }
    }
    
    public enum ExplosionId
    {
        END_UP(2,3),
        END_DOWN(2,1),
        END_LEFT(2,2),
        END_RIGHT(2,0),
        VERTICAL(1,1),
        HORIZONTAL(1,0),
        CROSS(0,0);
        
        private final int id;
        private final double radians;
        
        private ExplosionId(int id, int dir)
        {
            this.id = id;
            this.radians = dir * (Math.PI / 2);
        }
        
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
