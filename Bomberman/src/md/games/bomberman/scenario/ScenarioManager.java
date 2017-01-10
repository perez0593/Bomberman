/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario;

import java.awt.Graphics2D;
import java.io.IOException;
import md.games.bomberman.sprites.SpriteManager;

/**
 *
 * @author Asus
 */
public final class ScenarioManager
{
    private Scenario scenario;
    private final SpriteManager sprites;
    
    public ScenarioManager(SpriteManager sprites)
    {
        this.sprites = sprites == null ? new SpriteManager() : sprites;
        this.scenario = null;
    }
    public ScenarioManager() { this(null); }
    
    public final SpriteManager getSpriteManager() { return sprites; }
    public final Scenario getScenario() { return scenario; }
    
    public final Scenario createDebugScenario(int rows, int columns)
    {
        return scenario = new Scenario(sprites,rows,columns);
    }
    
    public final void loadTheme(String theme) throws IOException
    {
        sprites.loadScenarioTheme(theme);
    }
    
    public final void update(double delta)
    {
        scenario.update(delta);
    }
    
    public final void draw(Graphics2D g)
    {
        scenario.draw(g);
    }
}
