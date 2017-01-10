/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman;

import java.awt.Color;
import java.io.IOException;
import md.games.bomberman.util.Constants;
import nt.ntjg.NTJG;

/**
 *
 * @author mpasc
 */
public class Main {
    public static void main(String[] args) throws IOException {
        startTest();
    }
    
    private static void startTest() throws IOException
    {
        Game game = new Game();
        
        NTJG.ntjgCreateWindow("Bomberman " + Constants.VERSION,NTJG.ntjgGetValidDisplayMode(640,480));
        NTJG.ntjgEnableDebugInfo(Color.GREEN,12,true,true,false);
        NTJG.ntjgStart(game);
        
    }
}
