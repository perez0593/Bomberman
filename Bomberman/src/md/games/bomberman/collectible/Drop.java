/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.collectible;

import java.util.Random;
import md.games.bomberman.scenario.GameObject;

/**
 *
 * @author Asus
 */
public final class Drop
{
    private DropType type;
    private double prob;
    
    public Drop(DropType type, double prob)
    {
        if(type == null)
            throw new NullPointerException();
        this.type = type;
        this.prob = prob < 0 ? 0 : prob > 1 ? 1 : prob;
    }
    
    public final DropType getType() { return type; }
    public final double getProbability() { return prob; }
    
    public final GameObject generate(Random rand)
    {
        return rand.nextDouble() < prob ? buildDrop(type) : null;
    }
    
    private static GameObject buildDrop(DropType type)
    {
        return null; //TODO
    }
    
    public enum DropType {  } //TODO
}
