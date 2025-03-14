package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameKeys;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {
    @Override
    public void process(GameData gameData, Mission world) {
        for (Entity entity : world.getEntities(Player.class)) {
            // Controls
            Player player = (Player) entity;
            
            if (gameData.isDebugMode()) {
                gameData.addDebugRectangle(player.getID(), player, 15*8+2, 15*8+2);
            }

            // check if player is dead
            if (player.isDead()) {
                System.out.println("Player died!");
                world.removeEntity(player);
            }
            
            // Forward and backward
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                player.accelerate();
            } else if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                player.decelerate();
            }

            // Rotate
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.turnLeft();
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.turnRight();
            }

            // Shoot
            if (gameData.getKeys().isPressed(GameKeys.SPACE)) {
                player.shoot(gameData, world);
            }

            player.processPosition(gameData);
        }
    }
}
