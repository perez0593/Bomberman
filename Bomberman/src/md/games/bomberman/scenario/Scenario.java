/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

/**
 *
 * @author mpasc
 */
public final class Scenario
{
    private final TileManager tiles;
    private DeathBorder dborder;
    
    private Scenario(int rows, int columns)
    {
        tiles = new TileManager(rows, columns);
        dborder = null;
    }
    
    public final TileManager getTileManager() { return tiles; }
    public final DeathBorder getDeathBorder() { return dborder; }
}
