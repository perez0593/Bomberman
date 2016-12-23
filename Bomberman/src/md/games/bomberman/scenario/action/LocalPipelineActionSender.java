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
public class LocalPipelineActionSender extends ActionSender
{
    private final LocalPipelineActionReceiver receiver = new LocalPipelineActionReceiver();

    @Override
    public final void sendAction(Action action)
    {
        if(action == null)
            throw new NullPointerException();
        receiver.addAction(action);
    }
    
    public final LocalPipelineActionReceiver getAssociatedReceiver() { return receiver; }
}
