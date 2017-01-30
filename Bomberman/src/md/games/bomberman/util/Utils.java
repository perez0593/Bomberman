/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.util;

import md.games.bomberman.geom.BoundingBox;
import md.games.bomberman.geom.Vector2;

/**
 *
 * @author Asus
 */
public final class Utils
{
    private Utils() {}
    
    public static final SweptInfo sweptBoundingBox(BoundingBox b1, BoundingBox b2, Vector2 speed)
    {
        if(speed.isZero())
            return SweptInfo.EMPTY;
        
        double xInvEntry, yInvEntry;
        double xInvExit, yInvExit;

        // find the distance between the objects on the near and far sides for both x and y
        if (speed.x > 0.0f)
        {
            xInvEntry = b2.x0 - b1.x1;
            xInvExit = b2.x1 - b1.x0;
        }
        else 
        {
            xInvEntry = b2.x1 - b1.x0;
            xInvExit = b2.x0 - b1.x1;
        }

        if (speed.y > 0.0f)
        {
            yInvEntry = b2.y0 - b1.y1;
            yInvExit = b2.y1 - b1.y0;
        }
        else
        {
            yInvEntry = b2.y1 - b1.y0;
            yInvExit = b2.y0 - b1.y1;
        }

        // find time of collision and time of leaving for each axis (if statement is to prevent divide by zero)
        double xEntry, yEntry;
        double xExit, yExit;

        if (speed.x == 0.0f)
        {
            xEntry = -Double.MAX_VALUE;
            xExit = Double.MAX_VALUE;
        }
        else
        {
            xEntry = xInvEntry / speed.x;
            xExit = xInvExit / speed.x;
        }

        if (speed.y == 0.0f)
        {
            yEntry = -Double.MAX_VALUE;
            yExit = Double.MAX_VALUE;
        }
        else
        {
            yEntry = yInvEntry / speed.y;
            yExit = yInvExit / speed.y;
        }

        // find the earliest/latest times of collision
        double entryTime = StrictMath.max(xEntry, yEntry);
        double exitTime = StrictMath.min(xExit, yExit);

        // if there was no collision
        /*if (entryTime > exitTime || xEntry < 0.0f && yEntry < 0.0f || xEntry > 1.0f || yEntry > 1.0f)
        {
            return SweptInfo.EMPTY;
        }*/
        if(entryTime > exitTime) return SweptInfo.EMPTY;
        if(xEntry < 0d && yEntry < 0d) return SweptInfo.EMPTY;
        if(xEntry < 0d)
            if(b1.x1 <= b2.x0 || b1.x0 >= b2.x1) return SweptInfo.EMPTY;
        if(yEntry < 0d)
            if(b1.y1 <= b2.y0 || b1.y0 >= b2.y1) return SweptInfo.EMPTY;     		
            // calculate normal of collided surface
        if (xEntry > yEntry)
        {
            if (xInvEntry < 0.0f)
                return new SweptInfo(entryTime,1,0);
            else return new SweptInfo(entryTime,-1,0);
        }
        else
        {
            if (yInvEntry < 0.0f)
                return new SweptInfo(entryTime,0,1);
            else return new SweptInfo(entryTime,0,-1);
        }
    }
    
    public static final class SweptInfo
    {
        public final double time;
        public final double normalX;
        public final double normalY;
        
        private SweptInfo(double time, double nx, double ny)
        {
            this.time = time;
            this.normalX = nx;
            this.normalY = ny;
        }
        
        public static final SweptInfo EMPTY = new SweptInfo(1,0,0);
    }
}
