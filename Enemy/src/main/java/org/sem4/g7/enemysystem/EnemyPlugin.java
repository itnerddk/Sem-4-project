package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityPluginService;

public class EnemyPlugin implements IEntityPluginService {
    @Override
    public void start(GameData gameData, Mission mission) {
        System.out.println("Enemy plugin started");
        mission.addEntityType("Enemies", Enemy.class);
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        // Implement entity
    }
}