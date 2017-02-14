/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.event;

import md.games.bomberman.scenario.Scenario;
import nt.lpl.types.LPLFunction;
import nt.lpl.types.LPLObject;
import nt.lpl.types.LPLValue;

/**
 *
 * @author Asus
 */
public final class LPLScenarioEventBuilder extends LPLObject
{
    private final Scenario scenario;
    private ScenarioEventManager events;
    
    public LPLScenarioEventBuilder(Scenario scenario)
    {
        this.scenario = scenario;
        events = scenario.getEventManager();
    }
    
    @Override
    public final LPLValue getAttribute(LPLValue key)
    {
        switch(key.toJavaString())
        {
            default: return UNDEFINED;
            case "delayed": return DELAY_EVENT;
            case "normal": return NORMAL_EVENT;
        }
    }
    
    private final LPLValue NORMAL_EVENT = LPLFunction.createVFunction((arg0) -> {
        events.addEvent(new LPLScenarioEvent(arg0));
    });
    private final LPLValue DELAY_EVENT = LPLFunction.createVFunction((arg0, arg1) -> {
        LPLScenarioEvent event = new LPLScenarioEvent(arg1);
        events.addEvent(new DelayedScenarioEvent(arg0.toJavaDouble(),event));
    });
}
