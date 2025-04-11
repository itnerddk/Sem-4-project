package org.sdu.sem4.g7.audio;

import java.io.File;
import java.net.URI;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

import javafx.scene.media.AudioClip;

class CompositeKey {
    private SoundType soundType;
    private String soundName;

    public CompositeKey(SoundType soundType, String soundName) {
        this.soundType = soundType;
        this.soundName = soundName;
    }

    @Override
    public int hashCode() {
        if (soundName == null) {
            return soundType.hashCode();
        }
        return soundType.hashCode() + soundName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompositeKey) {
            CompositeKey other = (CompositeKey) obj;
            return this.soundType == other.soundType && this.soundName.equals(other.soundName);
        }
        return false;
    }
    
}

public class AudioProcessing implements IAudioProcessingService, IGamePluginService {

    private static ConcurrentHashMap<CompositeKey, AudioClip> soundMap = new ConcurrentHashMap<>();
    static {
        try {
            soundMap.put(new CompositeKey(SoundType.SHOOT, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Shoot.wav").toString()));
            soundMap.put(new CompositeKey(SoundType.HIT, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Hit.wav").toString()));
            soundMap.put(new CompositeKey(SoundType.EXPLOSION, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Explosion.wav").toString()));
            soundMap.put(new CompositeKey(SoundType.BUTTON_CLICK, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Button_Click.wav").toString()));
            soundMap.put(new CompositeKey(SoundType.GAME_START, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Game_Start.wav").toString()));
            soundMap.put(new CompositeKey(SoundType.GAME_END, ""), new AudioClip(AudioProcessing.class.getResource("/sounds/Game_End.wav").toString()));
        } catch (Exception e) {
            System.out.println("Error loading sound files: " + e.getMessage());
        }
    }

    @Override
    public void playSound(SoundType soundType, float volume) {
        playSound(soundType, "", volume);
    }

    @Override
    public boolean playSound(SoundType soundType, String soundName, float volume) {
        AudioClip sound = soundMap.get(new CompositeKey(soundType, soundName));
        double pitch = 0.8 + Math.random() * 0.4; // Random pitch between 0.8 and 1.2
        if (sound != null) {
            sound.setVolume(volume);
            sound.setRate(pitch);
            sound.play();
            return true;
        }
        System.out.println("Sound not found: " + soundType + " " + soundName);
        return false;
    }

    @Override
    public void addSound(SoundType soundType, URI filePath) {
        AudioClip sound = new AudioClip(filePath.toString());
        soundMap.put(new CompositeKey(soundType, filePath.toString()), sound);
    }

    @Override
    public void stopSound(SoundType soundType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stopSound'");
    }

    @Override
    public void start(GameData gameData, WorldData world) {
        // Allow itself to be injected into the game data and used
        gameData.setAudioProcessingService(this);
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        System.out.println("Stopping all sounds");
        // Stop all sounds
        
    }
    
}
