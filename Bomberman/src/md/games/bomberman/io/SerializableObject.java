/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.io;

/**
 *
 * @author Asus
 */
public interface SerializableObject
{
    void serialize(GameDataSaver gds);
    void unserialize(GameDataLoader gdl);
}
