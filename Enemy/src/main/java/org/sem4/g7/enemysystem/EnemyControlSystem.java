package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.playersystem.Player;


public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;

            // Find the player entity
            Player player = (Player) world.getEntities(Player.class).stream().findFirst().orElse(null);
            if (player == null) {
                continue; // No player found, skip
            }

            // Calculate the distance between the enemy and the player
            double dx = player.getPosition().getX() - enemy.getPosition().getX();
            double dy = player.getPosition().getY() - enemy.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Check if the enemy is within 5 tiles of the player
            double tileSize = 32; // Assuming each tile is 32x32 pixels
            if (distance <= 7 * tileSize) {
                // Calculate the angle to face the player
                double angle = Math.toDegrees(Math.atan2(dy, dx));

                // Adjust the angle to account for the enemy's default orientation
                angle += 90; // Add 90 degrees if the "front" of the enemy is offset by 90 degrees

                // Update the enemy's rotation
                enemy.setRotation((float) angle);
            }

            // Process the enemy's position
            enemy.processPosition(gameData);

            // Check if the enemy is dead
            if (enemy.isDead()) {
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
            }
        }
    }
}
