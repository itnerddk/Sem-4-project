package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class PlayerControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
        // Borrowing some process space... TODO: Fjern denne linje

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

            // Rotate turret
            player.getTurret().aimTowards(new Vector2(gameData.getRelativeMouseX(), gameData.getRelativeMouseY()));

            // Shoot
            if (gameData.isDown(GameData.Keys.SPACE) || gameData.isMouseDown()) {
                player.shoot(gameData, world);
            }

            player.processPosition(gameData);
        }
    }
}
