package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
        // Borrowing some process space... TODO: Fjern denne linje
        // System.out.println("Won: " + world.isGameWon() + " Lost: " + world.isGameLost());

        for (Entity entity : world.getEntities(Player.class)) {
            // Controls
            Player player = (Player) entity;

            // check if player is dead
            if (player.isDead()) {
                System.out.println("Player died!");
                world.removeEntity(player);
            }
            
            // Forward and backward
            if (gameData.isDown(GameData.Keys.UP)) {
                player.accelerate();
            } else if (gameData.isDown(GameData.Keys.DOWN)) {
                player.decelerate();
            }

            // Rotate
            if (gameData.isDown(GameData.Keys.LEFT)) {
                player.turnLeft();
            } else if (gameData.isDown(GameData.Keys.RIGHT)) {
                player.turnRight();
            }

            // Shoot
            if (gameData.isPressed(GameData.Keys.SPACE)) {
                player.shoot(gameData, world);
            }

            player.processPosition(gameData);
        }
    }
}
