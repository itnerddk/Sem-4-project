package org.sem4.g7.enemysystem;

import java.util.HashMap;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.playersystem.Player;

import javafx.scene.paint.Paint;


public class EnemyControlSystem implements IEntityProcessingService {

    // Constant for shoot interval in seconds
    private static final double SHOOT_INTERVAL = 3.5; // 3.5 seconds

    // Paths for enemies
    private static HashMap<String, List<Vector2>> paths = new HashMap<>();

    @Override
    public void process(GameData gameData, WorldData world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            // gameData.addDebug("Enemy"+entity.getID(), entity.getVelocity().toString());
            Enemy enemy = (Enemy) entity;
            // Check if the enemy is dead
            if (enemy.isDead()) {
                gameData.playAudio(SoundType.EXPLOSION);
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
                continue;
            }



            // Find the player entity
            Player player = (Player) world.getEntities(Player.class).stream().findFirst().orElse(null);
            if (player == null) {
                continue; // No player found, skip
            }

            // Calculate the distance between the enemy and the player
            double dx = player.getPosition().getX() - enemy.getPosition().getX();
            double dy = player.getPosition().getY() - enemy.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Check if the enemy is within x amount tiles of the player
            double tileSize = 96; // Each tile is 96x96 pixels
            if (distance <= 2 * tileSize) {
                // Calculate the angle to face the player
                double angle = Math.toDegrees(Math.atan2(dy, dx));

                // Adjust the angle to account for the enemy's default orientation
                angle += 90; // Add 90 degrees if the "front" of the enemy is offset by 90 degrees

                // Update the enemy's rotation
                enemy.setRotation((float) angle);

                // Check if enough time has passed to shoot
                double currentTime = gameData.getTime(); // Assuming `gameData.getTime()` returns time in seconds
                double lastShotTime = enemy.getLastShotTime(); // Assuming enemy has a field for last shot time

                // If 2 seconds have passed, shoot and update last shot time
                if (currentTime - lastShotTime >= SHOOT_INTERVAL) {
                    enemy.shoot(gameData, world); // Trigger the shoot action
                    enemy.setLastShotTime(currentTime); // Update the last shot time
                }
            }

            // Process the enemy's position
            enemy.processPosition(gameData);

            
            // Logics
            List<Vector2> specPath = paths.get(entity.getID());
            // if (paths.containsKey(entity.getID())) {
            //     if (specPath != null && !specPath.isEmpty()) {
            //         Vector2 nextPoint = specPath.get(0);
            //         enemy.setPosition(nextPoint.multiply(96));
            //         if (enemy.getPosition().equals(nextPoint)) {
            //             specPath.remove(0);
            //         }
            //     }
            // }
            // if (specPath == null || specPath.isEmpty()) {
                ServiceLocator.getLogicService().ifPresentOrElse(
                    service -> {
                        List<Vector2> path = service.findPath(entity, new Vector2(player.getPosition()));
                        if (path == null || path.isEmpty()) {
                            return;
                        }
                        paths.put(entity.getID(), path);
                        drawPath(gameData, path);
                    },
                    () -> {
                        System.out.println("NOT PRESENT!");
                    }
                );
            // }

        }
    }

    private void drawPath(GameData gd, List<Vector2> path) {
        System.out.println("Drawing path: " + path);
        gd.gc.save();
        gd.gc.setLineWidth(8);
        gd.gc.setStroke(javafx.scene.paint.Color.RED); // Replace with desired color
        for (int i = 0; i < path.size()-1; i++) {
            gd.gc.strokeLine(path.get(i).getX() * 64, path.get(i).getY() * 64, path.get(i+1).getX() * 64, path.get(i+1).getY() * 64);
        }
        gd.gc.restore();
    }
}
