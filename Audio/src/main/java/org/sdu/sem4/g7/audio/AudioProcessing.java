package org.sdu.sem4.g7.audio;

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
        String location = "/sounds/";
        for (SoundType soundType : SoundType.values()) {
            if (soundType == SoundType.MASTER) continue;
            try {
                CompositeKey key = new CompositeKey(soundType, "");
                AudioClip clip = new AudioClip(AudioProcessing.class.getResource(location + soundType.name().toLowerCase() + ".wav").toString());
                soundMap.put(key, clip);
            } catch (Exception e) {
                System.err.println("Sound " + soundType.name() + " could not load default .wav file.");
                e.printStackTrace();
            }
        }
    }


    @Override
    public void playSound(SoundType soundType, float volume) {
        playSound(soundType, "", volume, true);
    }

    @Override
    public void playSound(SoundType soundType, float volume, boolean pitch) {
        playSound(soundType, "", volume, pitch);
    }

    @Override
    public boolean playSound(SoundType soundType, String soundName, float volume, boolean pitch) {
        AudioClip sound = soundMap.get(new CompositeKey(soundType, soundName));
        if (sound != null) {
            double _pitch = 1.0;
            if (pitch) {
                _pitch = 0.8 + Math.random() * 0.4; // Random pitch between 0.8 and 1.2
            }
            sound.play(volume, 0, _pitch, 0, (int) System.currentTimeMillis());
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
