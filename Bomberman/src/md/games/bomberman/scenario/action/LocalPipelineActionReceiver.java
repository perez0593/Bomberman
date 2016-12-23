/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.scenario.action;

import java.util.LinkedList;

/**
 *
 * @author Asus
 */
public class LocalPipelineActionReceiver extends ActionReceiver
{
    private final LinkedList<Action> queue = new LinkedList<>();
    
    LocalPipelineActionReceiver() {}
    
    final void addAction(Action action) { queue.add(action); }
    
    @Override
    public final boolean hasActions() { return !queue.isEmpty(); }

    @Override
    public final Action nextAction() { return queue.removeFirst(); }
    
}
