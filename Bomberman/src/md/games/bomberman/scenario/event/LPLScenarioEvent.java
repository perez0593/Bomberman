/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.event;

import md.games.bomberman.scenario.Scenario;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public class LPLScenarioEvent extends ScenarioEvent
{
    private final LPLValue closure;
    
    public LPLScenarioEvent(LPLValue closure)
    {
        if(closure == null)
            throw new NullPointerException();
        this.closure = closure;
    }
    
    @Override
    public void dispatch(Scenario scenario, double delta)
    {
        closure.call();
    }
}
