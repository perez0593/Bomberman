/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.io.DataOutputStream;
import java.io.OutputStream;

/**
 *
 * @author mpasc
 */
public final class GameDataSaver extends DataOutputStream
{
    public GameDataSaver(OutputStream out) {
        super(out);
    }
    
}
