package org.sdu.sem4.g7.common.services;
import java.net.URI;

import org.sdu.sem4.g7.common.enums.SoundType;

public interface IAudioProcessingService {

    /**
     * Play a sound
     * @param soundType the name of the sound to play (shoot, click, explosion, etc.)
     * @param volume the volume of the sound (0.0 - 1.0)
     */
    void playSound(SoundType soundType, float volume);
    void playSound(SoundType soundType, float volume, boolean pitch);

    boolean playSound(SoundType soundType, String soundName, float volume, boolean pitch);

    void addSound(SoundType soundType, URI soundFile);

    /**
     * Stop a sound
     * @param soundType the name of the sound to stop (shoot, click, explosion, etc.)
     */
    void stopSound(SoundType soundType);
}