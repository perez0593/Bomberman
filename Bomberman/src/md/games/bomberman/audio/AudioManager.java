/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package md.games.bomberman.audio;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import kuusisto.tinysound.Music;
import md.games.bomberman.io.Resource;

/**
 *
 * @author Asus
 */
public final class AudioManager
{
    private static final HashMap<String, SoundItem> CACHE = new HashMap<>();
    private static final HashMap<String, SoundItem> SOUNDS = new HashMap<>();
    private static final LinkedList<SoundItem> SOUND_LIST = new LinkedList<>();
    private static Music MUSIC = null;
    
    public final void loadSound(String path, String tag, boolean loopable) throws IOException
    {
        if(SOUNDS.containsKey(tag))
            throw new IllegalArgumentException();
        SoundItem s = CACHE.get(path);
        if(s != null)
            s.addReference(this);
        else
        {
            File file = Resource.SOUNDS.getFile(path);
            Sound sound = Sound.loadSound(file,loopable);
            s = new SoundItem(this,sound);
            CACHE.put(path,s);
        }
        SOUNDS.put(tag,s);
    }
    
    public static final void playSound(String tag)
    {
        SoundItem s = SOUNDS.get(tag);
        if(s != null)
            s.play();
    }
    
    public static final void loopSound(String tag)
    {
        SoundItem s = SOUNDS.get(tag);
        if(s != null)
            s.loop();
    }
    
    public static final void stopSound(String tag)
    {
        SoundItem s = SOUNDS.get(tag);
        if(s != null)
            s.stop();
    }
    
    public static final void update()
    {
        for(SoundItem s : SOUND_LIST)
        {
            switch(s.play)
            {
                case -1:
                    s.sound.stop();
                    s.play = 0;
                    break;
                case 1:
                    s.sound.play(false);
                    s.play = 0;
                    break;
                case 2:
                    s.loop();
                    s.play = 3;
                    break;
            }
        }
    }
    
    private static final class SoundItem
    {
        private final List<AudioManager> refs = new LinkedList<>();
        private final Sound sound;
        private int play = 0;
        
        private SoundItem(AudioManager reference, Sound sound)
        {
            refs.add(reference);
            this.sound = sound;
        }
        
        private void addReference(AudioManager ref)
        {
            if(!refs.contains(ref))
                refs.add(ref);
        }
        
        private void deleteReference(AudioManager ref)
        {
            refs.remove(ref);
        }
        
        private boolean hasReferences() { return !refs.isEmpty(); }
        
        private void play()
        {
            if(play == 0)
                play = 1;
        }
        
        private void loop() { play = 2; }
        
        private void stop() { play = -1; }
    }
}
