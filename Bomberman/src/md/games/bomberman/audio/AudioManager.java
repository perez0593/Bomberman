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
import java.util.ListIterator;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
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
    
    private AudioManager() {}
    
    public static final void init()
    {
        TinySound.init();
    }
    
    public static final void shutdown()
    {
        if(MUSIC != null)
        {
            if(MUSIC.playing())
                MUSIC.stop();
            MUSIC.unload();
            MUSIC = null;
        }
        CACHE.clear();
        SOUNDS.clear();
        SOUND_LIST.clear();
        TinySound.shutdown();
    }
    
    public static final AudioManager createSoundsReference() { return new AudioManager(); }
    
    public final void loadSound(String path, String tag, boolean loopable) throws IOException
    {
        if(SOUNDS.containsKey(tag))
            return;
        SoundItem s = CACHE.get(path);
        if(s != null)
            s.addReference(this);
        else
        {
            File file = Resource.SOUNDS.getFile(path);
            Sound sound = Sound.loadSound(file,loopable);
            s = new SoundItem(this,tag,sound);
            CACHE.put(path,s);
        }
        SOUNDS.put(tag,s);
    }
    
    public final void clearSounds()
    {
        ListIterator<SoundItem> it = SOUND_LIST.listIterator();
        while(it.hasNext())
        {
            SoundItem s = it.next();
            s.deleteReference(this);
            SOUNDS.remove(s.tag);
            if(!s.hasReferences())
            {
                CACHE.remove(s.tag);
                it.remove();
            }
        }
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
    
    public static final void loadMusic(String path) throws IOException
    {
        if(MUSIC != null)
            throw new IllegalStateException();
        File file = Resource.MUSICS.getFile(path);
        MUSIC = TinySound.loadMusic(file);
    }
    
    public static final void playMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.play(false);
    }
    
    public static final void loopMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.play(true);
    }
    
    public static final void pauseMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.pause();
    }
    
    public static final void stopMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.stop();
    }
    
    public static final void resumeMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.resume();
    }
    
    public static final void rewindMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.rewind();
    }
    
    public static final void rewindMusicToLoopPosition()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.rewindToLoopPosition();
    }
    
    public static final void setMusicLoopPositionByFrame(int frameIndex)
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.setLoopPositionByFrame(frameIndex);
    }
    
    public static final void setMusicLoopPositionBySeconds(double seconds)
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.setLoopPositionBySeconds(seconds);
    }
    
    public static final int getMusicLoopPositionByFrame()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        return MUSIC.getLoopPositionByFrame();
    }
    
    public static final double getMusicLoopPositionBySeconds()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        return MUSIC.getLoopPositionBySeconds();
    }
    
    public static final boolean isMusicPlaying()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        return MUSIC.playing();
    }
    
    public static final boolean isMusicLoaded()
    {
        return MUSIC != null;
    }
    
    public static final void unloadMusic()
    {
        if(MUSIC == null)
            throw new IllegalStateException();
        MUSIC.unload();
        MUSIC = null;
    }
    
    private static final class SoundItem
    {
        private final List<AudioManager> refs = new LinkedList<>();
        private final String tag;
        private final Sound sound;
        private int play = 0;
        
        private SoundItem(AudioManager reference, String tag, Sound sound)
        {
            refs.add(reference);
            this.tag = tag;
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
