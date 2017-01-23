/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.Matrix33;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.object.GameObject;
import nt.ntjg.NTJG;

/**
 *
 * @author Asus
 */
public final class Camera
{
    private static double PIXELS_PER_METER = 1f;
    private double zoom, rotation;
    private AffineTransform at_transform;
    private BoundingBox bounds;
    private final Vector2 pos, viewport;
    private Vector2 limit0, limit1;
    private boolean modif, nativeViewport;
    
    public Camera()
    {
        zoom = 1f;
        rotation = 0f;
        at_transform = null;
        bounds = null;
        pos = new Vector2();
        viewport = new Vector2(NTJG.ntjgGetWindowWidth(),NTJG.ntjgGetWindowHeight());
        limit0 = limit1 = null;
        modif = nativeViewport = true;
    }
    
    public static final void setPixelsPerMeter(float pixels) throws IllegalArgumentException
    {
        if(pixels <= 0d)
            throw new IllegalArgumentException("pixels cannot be less or equals than 0");
        PIXELS_PER_METER = pixels;
    }
    public static final double getPixelsPerMeter() { return PIXELS_PER_METER; }
    
    private void recalcTransform()
    {
        if(!modif) return;
        update();
    }
    
    public final void setLimitedScope(Vector2 p0, Vector2 p1)
    {
        limit0 = new Vector2(p0);
        limit1 = new Vector2(p1);
    }
    public final void unsetLimitedScope() { limit0 = limit1 = null; }
    
    public final void setNativeViewport()
    {
        nativeViewport = true;
    }
    
    public final void setCustomViewport(float x, float y)
    {
        nativeViewport = false;
        viewport.set(x,y);
    }
    
    public final void copyViewport(Camera cam)
    {
        if(cam.nativeViewport)
        {
            nativeViewport = true;
            return;
        }
        nativeViewport = false;
        viewport.set(cam.viewport);
    }
    
    public final void update()
    {
        double w;
        double h;
        if(nativeViewport)
        {
            w = NTJG.ntjgGetWindowWidth();
            h = NTJG.ntjgGetWindowHeight();
        }
        else
        {
            w = viewport.x;
            h = viewport.y;
        }
        fixLimits(w,h);
        
        /*at_transform = AffineTransform.getTranslateInstance(-pos.x,-pos.y);
        at_transform.rotate(rotation);
        at_transform.translate(w/2d,h/2d);
        at_transform.scale(1f/zoom*PIXELS_PER_METER,1f/zoom*PIXELS_PER_METER);*/
                
        Matrix33 m = new Matrix33();
        Matrix33 transform = Matrix33.newTraslation((float)-pos.x,(float)-pos.y)
                .multiply(Matrix33.newRotation(rotation))
                .multiply(Matrix33.newScale(1f/zoom*PIXELS_PER_METER,1f/zoom*PIXELS_PER_METER))
                .multiply(Matrix33.newTraslation(w/2,h/2));
        at_transform = transform.toAffineTransform();
        /*at_transform = AffineTransform.getTranslateInstance(-pos.x,-pos.y);
        at_transform.translate(w/2,h/2);
        at_transform.rotate(rotation);
        at_transform.scale(1f/zoom,1f/zoom);*/
        
                
        viewport.x = w;
        viewport.y = h;
        try
        {
            AffineTransform inv = new AffineTransform(at_transform);
            inv.invert();
            Point2D.Double vpos = new Point2D.Double(),
                    vsize = new Point2D.Double();
            inv.transform(new Point2D.Double(),vpos);
            inv.transform(new Point2D.Double(viewport.x,viewport.y),vsize);
            bounds = new BoundingBox(
                    new Vector2(vpos.x,vpos.y),
                    new Vector2(vsize.x,vsize.y));
            //bounds.translate(vpos.x,vpos.y);
            
        }
        catch(Exception ex)
        {
            
        }
        
        modif = false;
    }
    
    public final AffineTransform getAffineTransform()
    {
        recalcTransform();
        return at_transform;
    }
    
    private void fixLimits(double w, double h)
    {
        if(limit0 == null)
            return;
        
        w *= zoom * PIXELS_PER_METER;
        h *= zoom * PIXELS_PER_METER;
        
        double w2 = w / 2;
        double h2 = h / 2;
        
        if(w >= limit1.x - limit0.x)
            pos.x = limit0.x + (limit1.x - limit0.x) / 2;
        else
        {
            if(pos.x - w2 < limit0.x)
                pos.x = limit0.x + w2;
            else if(pos.x - w2 > limit1.x)
                pos.x = limit1.x - w2;
        }
        
        if(w >= limit1.y - limit0.y)
            pos.y = limit0.y + (limit1.y - limit0.y) / 2;
        else
        {
            if(pos.y - h2 < limit0.y)
                pos.y = limit0.y + h2;
            else if(pos.y + h2 > limit1.y)
                pos.y = limit1.y - h2;
        }
    }
    
    public final BoundingBox getBounds() { return bounds; }
    //public final Rectangle getBoundsAsRectangle() { return bounds.toRectangle(); }
    public final boolean contains(BoundingBox bb)
    {
        return bounds.hasCollision(bb);
    }
    
    public final boolean contains(GameObject go) { return go.canSee(this); }
    
    public final boolean contains(float x, float y, float width, float height)
    {
        return contains(new BoundingBox(x,y,x+width,y+height));
    }
    
    public final boolean contains(double x, double y, double width, double height)
    {
        return contains(new BoundingBox(
                new Vector2(x,y),
                new Vector2(x+width,y+height)));
    }
    
    public final boolean contains(Vector2 point)
    {
        return bounds.contains(point);
    }
    
    public final boolean contains(float x, float y)
    {
        return bounds.contains(x,y);
    }
    
    public final boolean contains(double x, double y)
    {
        return bounds.contains(new Vector2(x, y));
    }
    
    public final void setPosition(float x, float y)
    {
        modif = true;
        pos.x = x;
        pos.y = y;
    }
    public final void setPosition(Vector2 pos)
    {
        modif = true;
        this.pos.x = pos.x;
        this.pos.y = pos.y;
    }
    public final void traslate(float meters_x, float meters_y)
    {
        modif = true;
        pos.x += meters_x;
        pos.y += meters_y;
    }
    public final void traslate(double meters_x, double meters_y)
    {
        modif = true;
        pos.x += meters_x;
        pos.y += meters_y;
    }
    public final void traslate(Vector2 dv)
    {
        modif = true;
        pos.x += dv.x;
        pos.y += dv.y;
    }
    
    public final void lookAt(GameObject go)
    {
        modif = true;
        Vector2 gpos = go.getPosition();
        pos.x = gpos.x;
        pos.y = gpos.y;
    }
    
    public final void setRotation(float rad_angle)
    {
        modif = true;
        rotation = rad_angle;
    }
    
    public final void rotate(float rad_angle)
    {
        modif = true;
        rotation += rad_angle;
    }
    
    public final double getRotation() { return rotation; }
    
    public final void setZoom(float value)
    {
        this.zoom = value;
    }
    
    /*public final void setZoomPercentage(float percentage)
    {
        percentage = percentage < 0f ? -percentage : percentage == 0 ?
                0.000001f : percentage;
        modif = true;
        zoom = 1f / percentage;
    }*/
    
    public final void translateZ(float meters)
    {
        zoom += meters;
        modif = true;
    }
    
    public final void translateZ(double meters)
    {
        zoom += meters;
        modif = true;
    }
    
    /*public final Vector2 worldToScreen(Vector2 worldPos)
    {
        return Vector2.transform(pos,at_transform);
    }
    
    public final Vector2 screenToWorld(Vector2 screenPos)
    {
        Vector2 res = new Vector2();
        try {at_transform.inverseTransform(pos,res); }
        catch(Exception ex) { ex.printStackTrace(System.err); }
        return res;
    }*/
    
    public final double getZoom() { return zoom; }
    public final double getAngle() { return rotation; }
    public final double getX() { return pos.x; }
    public final double getY() { return pos.y; }
    
    /*public final CameraTrackingEvent createCameraTracking(Vector2 to, float speed, float zoom, float rotation)
    {
        return new CameraTrackingEvent(pos.copy(),this.zoom,this.rotation,to,zoom,rotation,speed);
    }
    public final CameraTrackingEvent createCameraTracking(Vector2 to, float speed, float zoom)
    {
        return new CameraTrackingEvent(pos.copy(),this.zoom,rotation,to,zoom,rotation,speed);
    }
    public final CameraTrackingEvent createCameraTracking(Vector2 to, float speed)
    {
        return new CameraTrackingEvent(pos.copy(),zoom,rotation,to,zoom,rotation,speed);
    }*/
}
