/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.event;

import java.util.LinkedList;
import java.util.ListIterator;
import md.games.bomberman.scenario.Scenario;

/**
 *
 * @author Asus
 */
public final class ScenarioEventManager
{
    private LinkedList<ScenarioEvent> queue, queue2;
    
    public ScenarioEventManager()
    {
        queue = new LinkedList<>();
        queue2 = new LinkedList<>();
    }
    
    public final void addEvent(ScenarioEvent event)
    {
        if(event == null)
            throw new NullPointerException();
        queue.add(event);
    }
    
    public final void dispatchEvents(Scenario scenario, double delta)
    {
        LinkedList<ScenarioEvent> aux = queue2;
        queue2 = queue;
        queue = aux;
        ListIterator<ScenarioEvent> it = queue2.listIterator();
        while(it.hasNext())
        {
            ScenarioEvent event = it.next();
            event.dispatch(scenario,delta);
            if(event.isConsumed())
                it.remove();
        }
        if(!queue.isEmpty())
        {
            queue2.addAll(queue);
            queue.clear();
        }
        queue = queue2;
        queue2 = aux;
    }
}
