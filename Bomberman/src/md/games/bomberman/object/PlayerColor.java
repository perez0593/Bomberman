/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

/**
 *
 * @author David
 */
public enum PlayerColor {
    WHITE,
    RED,
    BLUE,
    BLACK,
    GREEN,
    YELLOW;
    
    private static final PlayerColor[] VALUES = values();
    
    public static final PlayerColor decode(int ordinal) { return VALUES[ordinal]; }
}
