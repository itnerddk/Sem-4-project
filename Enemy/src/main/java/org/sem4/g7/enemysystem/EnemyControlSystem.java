package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;


public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            gameData.addDebug("Enemy"+entity.getID(), entity.getVelocity().toString());
            Enemy enemy = (Enemy) entity;
            enemy.processPosition(gameData);

            // check if enemy is dead
            if (enemy.isDead()) {
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
                continue;
            }
        }
    }
}
