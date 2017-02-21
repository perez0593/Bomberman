/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.editor;

import java.awt.Component;
import java.awt.MouseInfo;
import java.awt.Point;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.scenario.Scenario;

/**
 *
 * @author Asus
 */
public final class MousePosition
{
    public final int x, y;
    private final boolean isOutX, isOutY;
    
    private MousePosition(Point p, boolean isOutX, boolean isOutY)
    {
        this(p.x,p.y,isOutX,isOutY);
    }
    
    private MousePosition(int x, int y, boolean isOutX, boolean isOutY)
    {
        this.x = x;
        this.y = y;
        this.isOutX = isOutX;
        this.isOutY = isOutY;
    }
    
    /*public static final MousePosition get(Component component, Camera cam)
    {
        Point p = component.getMousePosition();
        if(p == null)
            return null;
        Vector2 view = cam.getCustomViewport();
        if(view != null)
        {
            p = cam.integerPointToWorld(p);
            //p.x -= (int) (view.x / 2);
            //p.y -= (int) (view.y / 2);
        }
        return new MousePosition(p,component);
    }*/
    public static final MousePosition get(Component component)
    {
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point cp = component.getLocationOnScreen();
        int x = p.x - cp.x;
        int y = p.y - cp.y;
        return new MousePosition(
                x,
                y,
                x < 0 || x > component.getWidth(),
                y < 0 || y > component.getHeight()
        );
    }
    
    public final boolean isOutOfRangeX() { return isOutX; }
    public final boolean isOutOfRangeY() { return isOutY; }
    public final boolean isOutOfRange() { return isOutX || isOutY; }
    
    public final Vector2 getPositionInScenario(Scenario scenario)
    {
        return scenario.getCamera().vectorToLocal(new Vector2(x,y));
    }
}
