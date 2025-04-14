package org.sdu.sem4.g7.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.data.SettingGroup;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ISettingPluginService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class AudioSettings implements ISettingPluginService {

    @Override
    public void addSettings(List<SettingGroup> settings) {
        SettingGroup audioSettingGroup = new SettingGroup("Audio", "Audio settings");

        addSetting(audioSettingGroup, "Master Volume", "The game volume, affects all volumes", SoundType.MASTER);
        addSetting(audioSettingGroup, "SFX", "Sound effects (shooting, hitting, exploding, etc.)", SoundType.SHOOT, SoundType.EXPLOSION, SoundType.HIT);
        addSetting(audioSettingGroup, "UI", "User interface sounds (button clicks, etc.)", SoundType.BUTTON_CLICK);

        settings.add(audioSettingGroup);
    }

    private float getSoundVolume(SoundType... soundType) {
        String key = Audio.class.getName();
        for (SoundType st : soundType) {
            key += st.name();
        }
        float volume = 1.0f;

        if (ServiceLocator.getPersistenceService().isPresent()) {
            String volumeString = ServiceLocator.getPersistenceService().get().getString(key);
            if (volumeString != null) {
                try {
                    volume = Float.parseFloat(volumeString);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing volume: " + e.getMessage());
                }
            }
        }

        return volume;
    }

    public BiConsumer<GameData, Object> getSoundCallback(SoundType... soundType) {
        return (gd, value) -> {
            if (value instanceof Float) {
                for (SoundType st : soundType) {
                    gd.setSoundVolume(st, (Float) value);
                }
                ServiceLocator.getPersistenceService().ifPresent(
                    service -> {
                        String key = Audio.class.getName();
                        for (SoundType st : soundType) {
                            key += st.name();
                        }
                        service.setString(key, String.valueOf(value));
                    }
                );
            }
        };
    }

    private void addSetting(SettingGroup group, String name, String description, SoundType... soundType) {
        float volume = getSoundVolume(soundType);
        Setting setting = new Setting(name, description, volume, getSoundCallback(soundType));
        group.addSetting(setting);
    }
    
}
