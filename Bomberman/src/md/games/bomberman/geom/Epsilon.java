/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.geom;

/**
 *
 * @author Marc
 */
public class Epsilon
{
    public static final double E = Epsilon.compute();
	
    /**
     * Hidden default constructor.
     */
    private Epsilon() {}

    /**
     * Computes an approximation of machine epsilon.
     * @return double
     */
    public static final double compute()
    {
        double e = 0.5;
        while(1.0 + e > 1.0)
        {
            e *= 0.5;
        }
        return e;
    }
}
