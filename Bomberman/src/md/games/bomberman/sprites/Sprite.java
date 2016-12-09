/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.sprites;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;


public abstract class Sprite<A extends Sprite<A>>
{
    
    public Sprite()
    {
        
    }
    
    public abstract int width();
    public abstract int height();
    
    public abstract void update(double delta);
    public abstract void draw(Graphics2D g, AffineTransform transf);
    
    public abstract boolean isAnimatedSprite();
    public abstract boolean isMultiSprite();
    public abstract boolean isStaticSprite();
    
    public abstract Sprite duplicate();
    
    public abstract SpriteKind kind();
    
    public enum SpriteKind { STATIC, ANIMATED, MULTI }
    
    /*Default methods*/
    
    public final void draw(Graphics2D g, float x, float y, float w, float h)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.scale(w/width(),h/height());
        draw(g,af);
    }
    public final void draw(Graphics2D g, int x, int y, int w, int h)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.scale((float)w/width(),(float)h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, double x, double y, double w, double h)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.scale(w/width(),h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, float x, float y, float w, float h, float rotationRadians)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.rotate(rotationRadians,w/2,h/2);
        af.scale(w/width(),h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, int x, int y, int w, int h, float rotationRadians)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.rotate(rotationRadians,w/2,h/2);
        af.scale((float)w/width(),(float)h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, float x, float y, float w, float h, double rotationRadians)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.rotate(rotationRadians,w/2,h/2);
        af.scale(w/width(),h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, int x, int y, int w, int h, double rotationRadians)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.rotate(rotationRadians,w/2,h/2);
        af.scale((float)w/width(),(float)h/height());
        draw(g,af);
    }
    
    public final void draw(Graphics2D g, double x, double y, double w, double h, double rotationRadians)
    {
        AffineTransform af = new AffineTransform();
        af.translate(x,y);
        af.rotate(rotationRadians,w/2,h/2);
        af.scale(w/width(),h/height());
        draw(g,af);
    }
}
