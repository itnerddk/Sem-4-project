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
            
            // check if player is dead
            if (player.isDead()) {
                System.out.println("Player died!");
                world.removeEntity(player);
            }
            
            // Forward and backward
            if (gameData.getKeys().isDown(GameKeys.UP)) {
                player.setSpeed(
                    player.lerp(
                        player.getSpeed(),
                        Math.min(player.getSpeed() + (player.getAcceleration()), player.getMaxSpeed()),
                        0.7
                    )
                );
            } else if (gameData.getKeys().isDown(GameKeys.DOWN)) {
                player.setSpeed(
                    player.lerp(
                        player.getSpeed(),
                        Math.max(player.getSpeed() - (player.getDeceleration()), -(player.getMaxSpeed()/2)),
                        0.5
                    )
                );
            }

            // Rotate
            if (gameData.getKeys().isDown(GameKeys.LEFT)) {
                player.setRotation(player.getRotation() - player.getRotationSpeed());
            } else if (gameData.getKeys().isDown(GameKeys.RIGHT)) {
                player.setRotation(player.getRotation() + player.getRotationSpeed());
            }

            player.processPosition(gameData);
        }
    }
}
