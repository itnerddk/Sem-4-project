package org.sdu.sem4.g7.bounceTurret;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.tank.parts.Bullet;

public class BounceTurretProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
            for (Bullet bullet : world.getEntities(BounceBullet.class)) {
            Vector2 velocity = bullet.getVelocity();
            
            bullet.setPosition(velocity.getX() + bullet.getPosition().getX(), velocity.getY() + bullet.getPosition().getY());

            bullet.increaseFlightTime(gameData.getDelta());

            if (bullet.isDead()) {
                world.removeEntity(bullet);
                continue;
            }
        }
    }

}
