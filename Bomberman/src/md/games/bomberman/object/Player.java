/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import java.awt.Graphics2D;
import java.util.HashMap;
import md.games.bomberman.io.GameDataLoader;
import md.games.bomberman.io.GameDataSaver;
import nt.lpl.types.LPLValue;

/**
 *
 * @author David
 */
public class Player extends Creature {
    
    private String name;
    private PlayerColor color;
    
    /*stats*/
    private int lives;
    private double speed;
    private int maxbombs;
    
    @Override
    protected void innerDestroy() {}
    
    /*stats*/
    
    @Override
    public final CreatureType getCreatureType() { return CreatureType.PLAYER; }

    @Override
    public void update(double delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void draw(Graphics2D g) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void innserSerialize(GameDataSaver gds) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void innerUnserialize(GameDataLoader gdl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected LPLValue getAttribute(String key) {
        return null;
    }
    
}
