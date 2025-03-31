package org.sdu.sem4.g7.audio;

import java.net.URI;

public class Audio {
    private String soundName;
    private URI soundURI;
    private float volume;

    public Audio(String soundName, URI soundUri, float volume) {
        this.soundName = soundName;
        this.soundURI = soundUri;
        this.volume = volume;
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