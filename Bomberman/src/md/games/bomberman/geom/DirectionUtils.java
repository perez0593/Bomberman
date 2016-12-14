/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.geom;

/**
 *
 * @author Asus
 */
public final class DirectionUtils
{
    private DirectionUtils() {}
    
    public static final double PI2 = StrictMath.PI * 2;
    
    @SuppressWarnings("empty-statement")
    public static final double degToRad(double degrees)
    {
        if(degrees < 0)
            while((degrees += 360d) < 0);
        else if(degrees > 0)
            while((degrees -= 360d) >= 360d);
        return degrees / 180.0 * StrictMath.PI;
    }
    
    public static final double degToRad(int degrees)
    {
        switch(degrees)
        {
            default: return degToRad((double)degrees);
            case 315: case -315: return StrictMath.PI + (StrictMath.PI * 3d / 4d);
            case 270: case -270: return StrictMath.PI + (StrictMath.PI / 2d);
            case 225: case -225: return StrictMath.PI + (StrictMath.PI / 4d);
            case 180: case -180: return StrictMath.PI;
            case 135: case -135: return StrictMath.PI * 3d / 4d;
            case 90:  case -90:  return StrictMath.PI / 2d;
            case 45:  case -45:  return StrictMath.PI / 4d;
        }
    }
    
    @SuppressWarnings("empty-statement")
    public static final double radToDeg(double radians)
    {
        if(radians < 0)
            while((radians += PI2) < 0);
        else if(radians > 0)
            while((radians -= PI2) >= PI2);
        return radians * 180.0 / StrictMath.PI;
    }
}
