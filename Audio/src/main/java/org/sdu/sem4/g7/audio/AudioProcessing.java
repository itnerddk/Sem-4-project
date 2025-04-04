package org.sdu.sem4.g7.audio;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

import javafx.scene.media.AudioClip;

public class AudioProcessing implements IAudioProcessingService, IGamePluginService {

    public static ConcurrentHashMap<SoundType, String> soundMap = new ConcurrentHashMap<>();
    static {
        soundMap.put(SoundType.SHOOT, "Shoot");
        soundMap.put(SoundType.EXPLOSION, "Explosion");
        soundMap.put(SoundType.HIT, "Hit");
        soundMap.put(SoundType.BUTTON_CLICK, "Click");
        soundMap.put(SoundType.GAME_START, "Start");
        soundMap.put(SoundType.GAME_END, "End");
    }

    private HashMap<SoundType, AudioClip> soundPlayers = new HashMap<>();

    @Override
    public void playSound(SoundType soundType, float volume) {
        long startTime = System.nanoTime();
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        else if (soundType == null) {
            throw new IllegalArgumentException("soundType cannot be null");
        }
        // Play the sound
        if (!soundPlayers.containsKey(soundType)) {
            try {
                String soundName = soundMap.get(soundType);
                // Load the sound file
                String soundPath = getClass().getResource("/sounds/" + soundName + ".wav").toURI().toString();
                AudioClip audioClip = new AudioClip(soundPath);
                soundPlayers.put(soundType, audioClip);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        AudioClip audioClip = soundPlayers.get(soundType);
        audioClip.setVolume(volume);
        double pitch = 0.8f + Math.random() * 0.4f; // Random pitch between 0.8 and 1.2
        audioClip.setRate(pitch);
        audioClip.play();
        
        long endTime = System.nanoTime();
        System.out.println("Sound played: " + soundType.toString() + " | Volume: " + volume + " | Pitch: " + pitch + " | Time taken: " + ((double)(endTime - startTime))/1000000 + "ms");
    }

    @Override
    public void stopSound(SoundType soundType) { // TODO not possible at the moment
        if (soundType == null) {
            throw new IllegalArgumentException("soundType cannot be null");
        }

        System.out.println("Stopping all " + soundType.toString() + " sounds");

        
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
