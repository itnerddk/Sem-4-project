package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;


public class EnemyControlSystem implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, Mission world) {
        for (Entity entity : world.getEntities(Enemy.class)) {
            Enemy enemy = (Enemy) entity;

            // check if enemy is dead
            if (enemy.isDead()) {
                System.out.println("Enemy died!");
                world.removeEntity(enemy);
                continue;
            }
        }
    }
}
