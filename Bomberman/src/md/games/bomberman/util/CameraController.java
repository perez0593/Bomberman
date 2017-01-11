/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import md.games.bomberman.peripheral.KeyID;
import md.games.bomberman.peripheral.PeripheralMaskEvent;
import md.games.bomberman.peripheral.PeripheralMaskingListener;
import md.games.bomberman.scenario.Camera;

/**
 *
 * @author Asus
 */
public final class CameraController implements PeripheralMaskingListener
{
    private final Camera camera;
    
    private int upCode      = KeyID.encode(KeyID.VK_I),
                downCode    = KeyID.encode(KeyID.VK_K),
                leftCode    = KeyID.encode(KeyID.VK_J),
                rightCode   = KeyID.encode(KeyID.VK_L),
                zoomInCode  = KeyID.encode(KeyID.VK_U),
                zoomOutCode = KeyID.encode(KeyID.VK_O);
    
    private double camSpeed = 10d;
    private double camZoomSpeed = 0.01d;
    
    private boolean move[] = new boolean[6];
    
    
    
    
    public CameraController(Camera camera)
    {
        if(camera == null)
            throw new NullPointerException();
        this.camera = camera;
    }
    
    public final void setUpCode(int code) { upCode = code; }
    public final void setDownCode(int code) { downCode = code; }
    public final void setLeftCode(int code) { leftCode = code; }
    public final void setRightCode(int code) { rightCode = code; }
    public final void setZoomInCode(int code) { zoomInCode = code; }
    public final void setZoomOutCode(int code) { zoomOutCode = code; }
    
    public final void update(double delta)
    {
        if(move[0])
            camera.traslate(0,-camSpeed);
        if(move[1])
            camera.traslate(0,camSpeed);
        if(move[2])
            camera.traslate(-camSpeed,0);
        if(move[3])
            camera.traslate(camSpeed,0);
        if(move[4])
            camera.translateZ(camZoomSpeed);
        if(move[5])
            camera.translateZ(-camZoomSpeed);
    }

    @Override
    public final void dispatchMaskedEvent(PeripheralMaskEvent event)
    {
        int code = event.getCode();
        if(code == upCode)
            move[0] = event.isPressed();
        if(code == downCode)
            move[1] = event.isPressed();
        if(code == leftCode)
            move[2] = event.isPressed();
        if(code == rightCode)
            move[3] = event.isPressed();
        if(code == zoomInCode)
            move[4] = event.isPressed();
        if(code == zoomOutCode)
            move[5] = event.isPressed();
    }
}
