package org.sdu.sem4.g7.audio;

import java.net.URI;
import org.sdu.sem4.g7.common.enums.SoundType;

public class Audio {
    private SoundType soundType;
    private String soundName;
    private URI soundURI;
    private float volume;

    public Audio(SoundType soundType, String soundName, URI soundUri, float volume) {
        this.soundType = soundType;
        this.soundName = soundName;
        this.soundURI = soundUri;
        this.volume = volume;
    }

    public SoundType getSoundType() {
        return soundType;
    }

    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
    }

    public URI getSoundURI() {
        return soundURI;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}