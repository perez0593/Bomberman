/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

import java.io.IOException;

/**
 *
 * @author Asus
 */
public interface SerializableObject
{
    void serialize(GameDataSaver gds) throws IOException;
    void unserialize(GameDataLoader gdl) throws IOException;
}
