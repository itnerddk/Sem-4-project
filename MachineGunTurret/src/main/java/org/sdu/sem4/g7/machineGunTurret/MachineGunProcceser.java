package org.sdu.sem4.g7.machineGunTurret;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.tank.parts.Bullet;

public class MachineGunProcceser implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, WorldData world) {
        for (Entity e : world.getEntities(MachineGunBullet.class)) {
            MachineGunBullet cb = (MachineGunBullet) e;
            Vector2 velocity = cb.getVelocity();
            
            cb.setPosition(velocity.getX() + cb.getPosition().getX(), velocity.getY() + cb.getPosition().getY());  
        }

        // check for entities getting hit
        for (Bullet bullet : world.getEntities(MachineGunBullet.class)) {
            if (bullet.isDead()) {
                world.removeEntity(bullet);
                continue;
            }
        }
    }

}
