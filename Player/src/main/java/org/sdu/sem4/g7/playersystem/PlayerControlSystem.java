package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.playersystem.Player;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, Level world) {
        for (Entity player : world.getEntities(Player.class)) {
            // Process player
        }
    }
    
}
