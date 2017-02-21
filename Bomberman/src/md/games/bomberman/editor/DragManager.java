/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.editor;

import md.games.bomberman.geom.Vector2;
import md.games.bomberman.scenario.Camera;
import md.games.bomberman.scenario.Scenario;

/**
 *
 * @author Asus
 */
public class DragManager
{
    private Vector2 camPosition;
    private Vector2 basePosition;
    
    public final void start(Scenario scenario, MousePosition position)
    {
        if(camPosition != null)
            throw new IllegalStateException();
        camPosition = scenario.getCamera().getPosition();
        basePosition = new Vector2(position.x,position.y);
    }
    
    public final void updateCamera(Scenario scenario, MousePosition currentPosition)
    {
        if(camPosition == null)
            throw new IllegalStateException();
        Camera cam = scenario.getCamera();
        Vector2 distance = new Vector2(currentPosition.x,currentPosition.y).subtract(basePosition);
        scenario.getCamera().setPosition(camPosition.difference(distance.quotient(1d/cam.getZoom())));
    }
    
    public final void stop()
    {
        if(camPosition != null)
        {
            camPosition = null;
            basePosition = null;
        }
    }
    
    public final boolean isDragging() { return camPosition != null; }
}
