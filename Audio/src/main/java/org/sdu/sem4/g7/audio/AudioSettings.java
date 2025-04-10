package org.sdu.sem4.g7.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.data.SettingGroup;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ISettingPluginService;

public class AudioSettings implements ISettingPluginService {

    @Override
    public void addSettings(List<SettingGroup> settings) {
        List<Setting> audioSettings = new ArrayList<>();
        
        audioSettings.add(new Setting("Master Volume", "The game volume, affects all volumes", 1.0f, getSoundCallback(SoundType.MASTER)));
        audioSettings.add(new Setting("SFX", "Sound effects (shooting, hitting, exploding, etc.)", 1.0f, getSoundCallback(SoundType.SHOOT, SoundType.EXPLOSION, SoundType.HIT)));
        audioSettings.add(new Setting("UI", "User interface sounds (button clicks, etc.)", 1.0f, getSoundCallback(SoundType.BUTTON_CLICK)));
        
        SettingGroup audioSettingGroup = new SettingGroup("Audio", "Audio settings", audioSettings);
        settings.add(audioSettingGroup);
    }

    public BiConsumer<GameData, Object> getSoundCallback(SoundType... soundType) {
        return (gd, value) -> {
            if (value instanceof Float) {
                for (SoundType st : soundType) {
                    gd.setSoundVolume(st, (Float) value);
                }
            }
        };
    }
    
}
