package org.sdu.sem4.g7.audio;

import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioProcessing implements IAudioProcessingService, IGamePluginService {

    public static ConcurrentHashMap<Audio, MediaPlayer> audioList = new ConcurrentHashMap<>();

    @Override
    public void playSound(String soundName, float volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        else if (soundName == null || soundName.isEmpty()) {
            throw new IllegalArgumentException("Sound name cannot be null or empty");
        }
        
        final Audio audio;
        try {
            // Attempt to load the audio
            audio = new Audio(soundName, this.getClass().getResource("/sounds/" + soundName + ".wav").toURI(), volume);
        } catch (URISyntaxException ex) {
            System.err.println("Error loading sound: " + soundName);
            ex.printStackTrace();
            return;
        }
        
        // Create media from audio
        Media sound = new Media(audio.getSoundURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(audio.getVolume());
        // When the player ends it'll remove itself from the list and stop the playback
        mediaPlayer.setOnEndOfMedia(() -> {
            mediaPlayer.stop();
            audioList.remove(audio);
        });
        // mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        // Add the audio to the list of playing audios
        audioList.put(audio, mediaPlayer);
    }

    @Override
    public void stopSound(String soundName) {
        System.out.println("Stopping all " + soundName + " sounds");
        if (soundName == null || soundName.isEmpty()) {
            throw new IllegalArgumentException("Sound name cannot be null or empty");
        }
        // Stop all sounds with the given name
        audioList.forEach((audio, mediaPlayer) -> {
            if (audio.getSoundName().equals(soundName)) {
                mediaPlayer.stop();
                audioList.remove(audio);
            }
        });
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
        audioList.forEach((audio, mediaPlayer) -> {
            mediaPlayer.stop();
            audioList.remove(audio);
        });
    }
    
}
