package org.sdu.sem4.g7.audio;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

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

    private HashMap<SoundType, Media> soundPlayers = new HashMap<>();

    private static int soundCount = 0;
    private static int maxSounds = 5; // Even if spamming I can't play more than 5 sounds at once (atm)

    @Override
    public void playSound(SoundType soundType, float volume) {
        // Check if the sound count exceeds the maximum allowed sounds
        if (soundCount >= maxSounds) {
            System.out.println("Maximum number of sounds reached. Cannot play more sounds.");
            return;
        }
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        if (soundType == null) {
            throw new IllegalArgumentException("soundType cannot be null");
        }

        String soundName = soundMap.get(soundType);

        // Play the sound
        if (!soundPlayers.containsKey(soundType)) {
            try {
                // Load the sound file
                Media sound = new Media(getClass().getResource("/sounds/" + soundName + ".wav").toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setVolume(volume);
                soundPlayers.put(soundType, sound);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        
        MediaPlayer mediaPlayer = new MediaPlayer(soundPlayers.get(soundType));
        mediaPlayer.setVolume(volume);
        mediaPlayer.play();
        soundCount++;
        // Dispose of the media player when done
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            soundCount--;
        });
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
