/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.editor;

import java.awt.Component;
import java.awt.Point;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;
import md.games.bomberman.scenario.TileManager;

/**
 *
 * @author Asus
 */
public final class MousePosition
{
    public final int x, y, width, height;
    
    public MousePosition(Point p, Component component)
    {
        this(p.x,p.y,component);
    }
    
    public MousePosition(int x, int y, Component component)
    {
        this.x = x;
        this.y = y;
        
        width = component.getWidth();
        height = component.getHeight();
    }
    
    public static final MousePosition get(Component component)
    {
        Point p = component.getMousePosition();
        return p == null ? null : new MousePosition(p,component);
    }
    
    public final float getRatio(boolean isX, int base)
    {
        return (float) base / (isX ? width : height);
    }
    
    public final Vector2 getPositionInScenario(Scenario scenario)
    {
        Camera cam = scenario.getCamera();
        TileManager tiles = scenario.getTileManager();
        Vector2 pos = new Vector2(x,y);
        double scale = 1d / cam.getZoom();
        pos.x = (x + tiles.getPositionX() + cam.getX()) * scale;
        pos.y = (y + tiles.getPositionY() + cam.getY()) * scale;
        return pos;
    }
}
