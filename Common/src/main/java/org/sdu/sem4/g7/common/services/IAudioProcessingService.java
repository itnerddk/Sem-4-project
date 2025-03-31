package org.sdu.sem4.g7.common.services;

public interface IAudioProcessingService {

    /**
     * Play a sound
     * @param soundName the name of the sound to play (shoot, click, explosion, etc.)
     * @param volume the volume of the sound (0.0 - 1.0)
     */
    void playSound(String soundName, float volume);
    /**
     * Stop a sound
     * @param soundName the name of the sound to stop (shoot, click, explosion, etc.)
     */
    void stopSound(String soundName);
}