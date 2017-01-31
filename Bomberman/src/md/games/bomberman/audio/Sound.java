/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.audio;

import java.io.File;
import kuusisto.tinysound.TinySound;

/**
 *
 * @author Asus
 */
public abstract class Sound
{
    public abstract boolean isLoopable();
    public abstract void play(boolean loop, double volume, double pan);
    public abstract void play(boolean loop, double volume);
    public abstract void play(boolean loop);
    public abstract void stop();
    public abstract boolean isPlaying();
    abstract void unload();
    
    static final Sound loadSound(File file, boolean loopable)
    {
        return loopable ? new NormalSound(file) : new LoopableSound(file);
    }
    
    
    private static final class NormalSound extends Sound
    {
        private final kuusisto.tinysound.Sound sound;
        
        private NormalSound(File file)
        {
            this.sound = TinySound.loadSound(file);
        }
        
        @Override
        public final boolean isLoopable() { return false; }
        
        @Override
        public void play(boolean loop, double volume, double pan) { sound.play(volume,pan); }
        
        @Override
        public void play(boolean loop, double volume) { sound.play(volume); }

        @Override
        public void play(boolean loop) { sound.play(); }

        @Override
        public final void stop() { sound.stop(); }
        
        @Override
        public final boolean isPlaying() { return false; }

        @Override
        final void unload() { sound.unload(); }
    }
    
    private static final class LoopableSound extends Sound
    {
        private final kuusisto.tinysound.Music sound;
        
        private LoopableSound(File file)
        {
            this.sound = TinySound.loadMusic(file);
        }
        
        @Override
        public final boolean isLoopable() { return false; }
        
        @Override
        public void play(boolean loop, double volume, double pan) { sound.play(loop,volume,pan); }
        
        @Override
        public void play(boolean loop, double volume) { sound.play(loop,volume); }

        @Override
        public void play(boolean loop) { sound.play(loop); }

        @Override
        public final void stop() { sound.stop(); }
        
        @Override
        public final boolean isPlaying() { return sound.playing(); }

        @Override
        final void unload() { sound.unload(); }
    }
}
