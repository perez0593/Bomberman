/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.event;

import md.games.bomberman.scenario.Scenario;

/**
 *
 * @author Asus
 */
public abstract class ScenarioEvent
{
    public abstract void dispatch(Scenario scenario, double delta);
    public boolean isConsumed() { return true; }
}
