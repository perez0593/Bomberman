/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.font;

import java.awt.Color;
import java.awt.Graphics2D;
import md.games.bomberman.sprites.StaticSprite;

/**
 *
 * @author Marc
 */
public interface Font
{
    public void print(Graphics2D g, String text, int x, int y);
    public void printCentre(Graphics2D g, String text, int x, int y);
    public void printFinal(Graphics2D g, String text, int x, int y);
    public void setColor(Color color);
    public void setDimensions(int size);
    
    public StaticSprite generateImage(String text);
}
