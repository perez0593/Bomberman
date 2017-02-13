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
public class DelayedScenarioEvent extends ScenarioEvent
{
    private final ScenarioEvent event;
    private double timeRemaining;
    
    public DelayedScenarioEvent(double time, ScenarioEvent event)
    {
        if(event == null)
            throw new NullPointerException();
        this.event = event;
        timeRemaining = time;
    }
    
    @Override
    public void dispatch(Scenario scenario, double delta)
    {
        if(timeRemaining <= 0)
            event.dispatch(scenario,delta);
        else
        {
            timeRemaining -= delta;
            if(timeRemaining <= 0)
                event.dispatch(scenario,delta);
        }
    }
    
    @Override
    public boolean isConsumed()
    {
        return timeRemaining <= 0 && event.isConsumed();
    }
    
}
