package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityPluginService;


public class PlayerPlugin implements IEntityPluginService {
    @Override
    public void start(GameData gameData, Mission mission) {
        System.out.println("Player plugin started");
        mission.addEntityType("Players", Player.class);
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        // Implement entity
    }
}
