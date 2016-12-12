/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.object;

import nt.dal.data.DALData;

/**
 *
 * @author Asus
 */
public enum GameObjectClass
{
    ;
    private final DALData asDal = DALData.valueOf(name());
    
    public final DALData nameAsDal() { return asDal; }
}
