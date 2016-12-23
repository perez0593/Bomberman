/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.action;

/**
 *
 * @author Asus
 */
public abstract class ActionReceiver
{
    public abstract boolean hasActions();
    public abstract Action nextAction();
}
