/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import md.games.bomberman.geom.Vector2;
import md.games.bomberman.object.GameObject;
import md.games.bomberman.object.Player;

/**
 *
 * @author Asus
 */
public final class DeathBorder
{
    private final HashMap<UUID, Straight> cache;
    private final Vector2 position;
    private final Vector2 size;
    private final Straight up, down, left, right;
    private final Random rand;
    
    public DeathBorder()
    {
        position = new Vector2();
        size = new Vector2();
        
        up = new Straight(Orientation.UP);
        down = new Straight(Orientation.DOWN);
        left = new Straight(Orientation.LEFT);
        right = new Straight(Orientation.RIGHT);
        
        up.leftLink = right;
        up.rightLink = left;
        down.leftLink = left;
        down.rightLink = right;
        left.leftLink = up;
        left.rightLink = down;
        right.leftLink = down;
        right.rightLink = up;
        
        cache = new HashMap<>();
        rand = new Random();
    }
    
    public final void situate(Vector2 position, Vector2 size)
    {
        situate(position.x, position.y, size.x, size.y);
    }
    public final void situate(double x, double y, double width, double height)
    {
        if(width <= 0 || height <= 0)
            throw new IllegalArgumentException();
        position.set(x,y);
        size.set(width,height);
        
        up.situate(x + width, y, -width, 0);
        down.situate(x, y + height, width, 0);
        left.situate(x, y, 0, height);
        right.situate(x + width, y + height, 0, -height);
    }
    
    private Straight randomStraight()
    {
        switch(rand.nextInt(4))
        {
            default: return up;
            case 1: return down;
            case 2: return left;
            case 3: return right;
        }
    }
    
    public final void insertPlayer(Player p)
    {
        if(p == null)
            throw new NullPointerException();
        if(cache.containsKey(p.getId()))
            throw new IllegalArgumentException();
        Straight s = randomStraight();
        Vector2 pos = s.randomPosition();
        p.setPosition(pos);
        p.setDirection(s.orientationDegrees());
        cache.put(p.getId(),s);
    }
    
    public final void removePlayer(Player p)
    {
        if(p == null)
            throw new NullPointerException();
        if(!cache.containsKey(p.getId()))
            throw new IllegalArgumentException();
        cache.remove(p.getId());
    }
    
    public final void movePlayer(Player p, double delta)
    {
        if(p == null)
            throw new NullPointerException();
        if(!cache.containsKey(p.getId()))
            throw new IllegalArgumentException();
        Straight s = cache.get(p.getId());
        s.move(p,delta);
    }
    
    
    private final class Straight
    {
        private final Vector2 leftPoint = new Vector2();
        private final Vector2 rightPoint = new Vector2();
        private final Orientation orientation;
        private Straight leftLink;
        private Straight rightLink;
        
        private Straight(Orientation orientation)
        {
            this.orientation = orientation;
        }
        
        private void situate(double x, double y, double width, double height)
        {
            leftPoint.set(x,y);
            rightPoint.set(x + width, y + height);
        }
        
        private Vector2 randomPosition()
        {
            Vector2 p = new Vector2(
                    Math.min(leftPoint.x,rightPoint.x),
                    Math.min(leftPoint.y,rightPoint.y)
            );
            p.add(
                    rand.nextDouble() * (Math.max(leftPoint.x,rightPoint.x) - p.x),
                    rand.nextDouble() * (Math.max(leftPoint.y,rightPoint.y) - p.y)
            );
            return p;
        }
        
        private double orientationDegrees()
        {
            switch(orientation)
            {
                default: throw new IllegalStateException();
                case UP: return 180d;
                case DOWN: return 0d;
                case LEFT: return 270d;
                case RIGHT: return 90d;
            }
        }
        
        private void move(Player p, double delta)
        {
            switch(orientation)
            {
                default: throw new IllegalStateException();
                case UP: {
                    p.translate(-delta,0);
                    if(p.getPositionX() < rightPoint.x)
                        changeStraight(p,false);
                    else if(p.getPositionX() > leftPoint.x)
                        changeStraight(p,true);
                } break;
                case DOWN: {
                    p.translate(delta,0);
                    if(p.getPositionX() < leftPoint.x)
                        changeStraight(p,true);
                    else if(p.getPositionX() > rightPoint.x)
                        changeStraight(p,false);
                } break;
                case LEFT: {
                    p.translate(0,delta);
                    if(p.getPositionY() < leftPoint.y)
                        changeStraight(p,true);
                    else if(p.getPositionY() > rightPoint.y)
                        changeStraight(p,false);
                } break;
                case RIGHT: {
                    p.translate(0,-delta);
                    if(p.getPositionY() < rightPoint.y)
                        changeStraight(p,false);
                    else if(p.getPositionY() > leftPoint.y)
                        changeStraight(p,true);
                } break;
            }
        }
        
        private void changeStraight(Player p, boolean isLeft)
        {
            switch(orientation)
            {
                default: throw new IllegalStateException();
                case UP: case DOWN: {
                    if(isLeft)
                    {
                        double delta = p.getPositionX() - leftPoint.x;
                        p.setPosition(leftLink.rightPoint);
                        p.translate(0,delta);
                    }
                    else
                    {
                        double delta = rightPoint.x - p.getPositionX();
                        p.setPosition(rightLink.rightPoint);
                        p.translate(0,delta);
                    }
                } break;
                case LEFT: case RIGHT: {
                    if(isLeft)
                    {
                        double delta = leftPoint.y - p.getPositionY();
                        p.setPosition(leftLink.rightPoint);
                        p.translate(delta,0);
                    }
                    else
                    {
                        double delta = p.getPositionY() - rightPoint.y;
                        p.setPosition(rightLink.rightPoint);
                        p.translate(delta,0);
                    }
                } break;
            }
            cache.put(p.getId(), isLeft ? leftLink : rightLink);
        }
    }
    
    private enum Orientation { UP, DOWN, LEFT, RIGHT }
}
