package org.sem4.g7.enemysystem;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.EntityActions;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.playersystem.Player;

public class EnemyControlSystem implements IEntityProcessingService {

    // Constant for shoot interval in seconds
    private static final double SHOOT_INTERVAL = 3.5; // 3.5 seconds


    private static final long MAX_TIME = 4000; // Microseconds, 4000 is 4ms

    @Override
    public void process(GameData gameData, WorldData world) {
        List<Enemy> enemies = world.getEntities(Enemy.class);
        for (Enemy enemy : enemies) {
            // Check if the enemy is dead
            if (enemy.isDead()) {
                gameData.playAudio(SoundType.EXPLOSION);
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
                continue;
            }



            // Find the player entity
            Player player = world.getEntities(Player.class).stream().findFirst().orElse(null);
            if (player == null) {
                continue; // No player found, skip
            }

            // Calculate the distance between the enemy and the player
            double dx = player.getPosition().getX() - enemy.getPosition().getX();
            double dy = player.getPosition().getY() - enemy.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);

            // Check if the enemy is within x amount tiles of the player
            double tileSize = CommonConfig.getTileSize(); // Each tile is 96x96 pixels
            if (distance <= 2 * tileSize) {
                // Calculate the angle to face the player
                double angle = Math.toDegrees(Math.atan2(dy, dx));

                // Adjust the angle to account for the enemy's default orientation
                angle += 90; // Add 90 degrees if the "front" of the enemy is offset by 90 degrees

                // Update the enemy's rotation
                //enemy.setRotation((float) angle);

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
            Vector2 playerPosition = new Vector2(player.getPosition());
            
            if (enemy.getTarget() == null) {
                enemy.setTarget(new Vector2(enemy.getPosition()));
            }
            
            // If it doesn't have a path, find one
            ServiceLocator.getLogicService().ifPresentOrElse(
                service -> {
                    // Actions

                    enemy.logicalAction = service.getAction(enemy, playerPosition);

                    enemy.setTarget(service.evaluateAction(enemy, playerPosition));
                    // System.out.println("Target: " + enemy.getTarget());
                    // Path finding
                    if (enemy.getPath() == null || enemy.getPath().isEmpty()) {
                        ArrayList<Vector2> path = null;
                        long timeStarted = System.nanoTime();
                        if (enemy.getTarget() == null) {
                            return;
                        }
                        while (path == null && (MAX_TIME / enemies.size()) > TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - timeStarted)) {
                            path = service.findPath(enemy, enemy.getTarget());
                        }
                        if (path == null || path.isEmpty()) {
                            return;
                        }
                        enemy.setPath(path);
                        drawPath(gameData, path);
                    }
                },
                () -> {
                    System.out.println("NOT PRESENT!");
                }
            );

            // Checking if path has the right goal
            if (enemy.getPath() != null && !enemy.getPath().isEmpty()) {
                drawPath(gameData, enemy.getPath());
                // System.out.println(enemy.getPath().get(enemy.getPath().size() - 1) + " == " + new Vector2(enemy.getTarget()).divideInt(CommonConfig.getTileSize()));
                if (!enemy.getPath().get(enemy.getPath().size() - 1).equals(new Vector2(enemy.getTarget()).divideInt(CommonConfig.getTileSize()))) {
                    // System.out.println(enemy.getPath().getLast() + " != " + new Vector2(target).divideInt(CommonConfig.getTileSize()));
                    // System.out.println("Path is no longer valid, removing it");
                    enemy.setPath(null);
                }
            }
        }
    }

    private void drawPath(GameData gd, List<Vector2> path) {
        // System.out.println("Drawing path: " + path);
        gd.gc.save();
        gd.gc.setLineWidth(8);
        gd.gc.setStroke(javafx.scene.paint.Color.RED); // Replace with desired color
        int sz = CommonConfig.getTileSize(); // sz : Sizing
        for (int i = 0; i < path.size()-1; i++) {
            gd.gc.strokeLine(path.get(i).getX() * sz, path.get(i).getY() * sz, path.get(i+1).getX() * sz, path.get(i+1).getY() * sz);
        }
        gd.gc.restore();
    }
}
