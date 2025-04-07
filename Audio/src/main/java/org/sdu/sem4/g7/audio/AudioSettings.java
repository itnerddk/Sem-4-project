package org.sdu.sem4.g7.audio;

import java.util.List;

import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ISettingPluginService;

public class AudioSettings implements ISettingPluginService {

    @Override
    public void addSettings(List<Setting> settings) {
        Setting allSounds = new Setting("All Sounds", "The volume of all sounds.", 1.0f, (gameData, value) -> {
            // Making sure the value passed is a float
            if (value instanceof Float) {
                float volume = (Float) value;
                for (SoundType soundType : SoundType.values()) {
                    gameData.setSoundVolume(soundType, volume);
                }
            }
        });
        settings.add(allSounds);

        // settings.add(new Setting("SFX", "The volume of sound effects (Shoot, Hit, etc.)", 1.0f, (gameData, value) -> {
        //     // Making sure the value passed is a float
        //     if (value instanceof Float) {
        //         float volume = (Float) value;
        //         gameData.setSoundVolume(SoundType.SHOOT, volume);
        //         gameData.setSoundVolume(SoundType.HIT, volume);
        //         gameData.setSoundVolume(SoundType.EXPLOSION, volume);
        //     }
        // }));

        // TODO: Remove these, just for testing
        settings.add(new Setting("Shoot", "The volume of the shoot sound.", 1.0f, (gameData, value) -> {
            // Making sure the value passed is a float
            if (value instanceof Float) {
                float volume = (Float) value;
                gameData.setSoundVolume(SoundType.SHOOT, volume);
            }
        }));

        settings.add(new Setting("Hit", "The volume of the hit sound.", 1.0f, (gameData, value) -> {
            // Making sure the value passed is a float
            if (value instanceof Float) {
                float volume = (Float) value;
                gameData.setSoundVolume(SoundType.HIT, volume);
            }
        }));

        settings.add(new Setting("Explosion", "The volume of the explosion sound.", 1.0f, (gameData, value) -> {
            // Making sure the value passed is a float
            if (value instanceof Float) {
                float volume = (Float) value;
                gameData.setSoundVolume(SoundType.EXPLOSION, volume);
            }
        }));

    }
    
}
