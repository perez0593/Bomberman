/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package md.games.bomberman.peripheral;

import net.java.games.input.Component;

/**
 *
 * @author Marc
 */
abstract class GamePadComponent
{
    final Component component;
    final GamePadInputID id;
    boolean lastPoll;
    
    private GamePadComponent(Component component, GamePadInputID id)
    {
        this.component = component;
        this.id = id;
        this.lastPoll = false;
    }
    
    public final Component getComponent() { return component; }
    public GamePadInputID getID() { return id; } 
    public abstract boolean isPressed();
    
    @Override
    public String toString()
    {
        return getID().toString();
    }
    
    static final class ButtonReference extends GamePadComponent
    {
        ButtonReference(Component component, GamePadInputID id)
        {
            super(component,id);
        }

        @Override
        public final boolean isPressed()
        {
            return component.getPollData() != 0f;
        }
    }
    
    static final class PovReference extends GamePadComponent
    {
        private final PovDirection dir;
        
        PovReference(Component component, GamePadInputID id, PovDirection dir)
        {
            super(component,id);
            if(dir == PovDirection.NONE)
                throw new IllegalArgumentException("Invalid POV direction");
            this.dir = dir;
        }
        
        @Override
        public final boolean isPressed()
        {
            return PovDirection.cast(component.getPollData()) == dir;
        }
        
        @Override
        public final String toString()
        {
            return component.toString() + "direction = " + dir.name() + ";";
        }
    }
    
    private static float axisTolerance = 0.5f;
    public static final void setAxisTolerance(float value)
    {
        axisTolerance = value <= 0f ? 0.1f : value > 1f ? 1f : value;
    }
    
    static final class AxisReference extends GamePadComponent
    {
        private final boolean positive;
        
        AxisReference(Component component, GamePadInputID id, boolean positive)
        {
            super(component,id);
            this.positive = positive;
        }

        @Override
        public final boolean isPressed()
        {
            float val = component.getPollData();
            return positive ? val >= axisTolerance : val <= -axisTolerance;
        }
        
        @Override
        public final String toString()
        {
            return component.toString() + "["+(positive?"+":"-")+"]";
        }
    }
}
