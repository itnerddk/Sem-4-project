package org.sem4.g7.enemysystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.playersystem.Player;

public class EnemyControlSystem implements IEntityProcessingService {

    private static final double SHOOT_INTERVAL = 3.5; // 3.5 seconds
    private static final long MAX_PATH_TIME = 4000; // Microseconds
    private static final long MAX_PROCESSING_TIME = 1000 + MAX_PATH_TIME; // Microseconds

    private int lastEnemyIndex = 0; // 👈 Persistent enemy index

    @Override
    public void process(GameData gameData, WorldData world) {
        long startTime = TimeUnit.NANOSECONDS.toMicros(System.nanoTime());
        List<Enemy> enemies = world.getEntities(Enemy.class);

        if (enemies.isEmpty()) return;

        int count = enemies.size();
        for (int i = 0; i < count; i++) {
            int currentIndex = (lastEnemyIndex + i) % count;
            Enemy enemy = enemies.get(currentIndex);

            if (enemy.isDead()) {
                gameData.playAudio(SoundType.EXPLOSION);
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
                continue;
            }

            Player player = world.getEntities(Player.class).stream().findFirst().orElse(null);
            if (player == null) continue;

            double dx = player.getPosition().getX() - enemy.getPosition().getX();
            double dy = player.getPosition().getY() - enemy.getPosition().getY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            double tileSize = CommonConfig.getTileSize();

            if (distance <= 2 * tileSize) {
                double angle = Math.toDegrees(Math.atan2(dy, dx)) + 90;
                double currentTime = gameData.getTime();
                double lastShotTime = enemy.getLastShotTime();

                if (currentTime - lastShotTime >= SHOOT_INTERVAL) {
                    enemy.shoot(gameData, world);
                    enemy.setLastShotTime(currentTime);
                }
            }

            enemy.processPosition(gameData);

            if (TimeUnit.NANOSECONDS.toMicros(System.nanoTime()) - startTime > MAX_PROCESSING_TIME) {
                // System.out.println("Time budget hit, stopping at enemy " + enemy.getID());
                lastEnemyIndex = (currentIndex + 1) % count; // ⏩ Save index for next frame
                break;
            }

            Vector2 playerPosition = new Vector2(player.getPosition());
            if (enemy.getTarget() == null) enemy.setTarget(new Vector2(enemy.getPosition()));

            ServiceLocator.getLogicService().ifPresentOrElse(
                service -> {
                    enemy.logicalAction = service.getAction(enemy, playerPosition);
                    enemy.setTarget(service.evaluateAction(enemy, playerPosition));

                    if (enemy.getPath() == null || enemy.getPath().isEmpty()) {
                        ArrayList<Vector2> path = null;
                        long timeStarted = System.nanoTime();
                        if (enemy.getTarget() == null) return;

                        while (path == null && (MAX_PATH_TIME / count) > TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - timeStarted)) {
                            path = service.findPath(enemy, enemy.getTarget());
                        }

                        if (path == null || path.isEmpty()) return;

                        enemy.setPath(path);
                        drawPath(gameData, path);
                    }
                },
                () -> System.out.println("Logic service not present!")
            );

            if (enemy.getPath() != null && !enemy.getPath().isEmpty()) {
                drawPath(gameData, enemy.getPath());
                Vector2 goal = new Vector2(enemy.getTarget()).divideInt((int)tileSize);
                if (!enemy.getPath().get(enemy.getPath().size() - 1).equals(goal)) {
                    enemy.setPath(null);
                }
            }
        }
    }

    private void drawPath(GameData gd, List<Vector2> path) {
        if (CommonConfig.isDEBUG()) {
            gd.gc.save();
            gd.gc.setLineWidth(8);
            gd.gc.setStroke(javafx.scene.paint.Color.RED);
            int sz = CommonConfig.getTileSize();
            for (int i = 0; i < path.size() - 1; i++) {
                gd.gc.strokeLine(path.get(i).getX() * sz, path.get(i).getY() * sz,
                                 path.get(i + 1).getX() * sz, path.get(i + 1).getY() * sz);
            }
            gd.gc.restore();
        }
    }
}
