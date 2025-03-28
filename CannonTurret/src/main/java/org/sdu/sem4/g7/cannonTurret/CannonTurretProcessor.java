package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Bullet;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurretProcessor implements IEntityProcessingService {
	@Override
	public void process(GameData gameData, WorldData worldData) {
		// Implementation of the process method
        for (Entity e : worldData.getEntities(CannonTurret.class)) {
            Turret t = (Turret) e;
        }

        for (Entity e : worldData.getEntities(CannonBullet.class)) {
            CannonBullet cb = (CannonBullet) e;
            Vector2 velocity = cb.getVelocity();
            
            cb.setPosition(velocity.getX() + cb.getPosition().getX(), velocity.getY() + cb.getPosition().getY());  
            
            // despawn bullet if outside the map
            if (e.getPosition().getX() > gameData.getMissionLoaderService().getMapSizeX() || e.getPosition().getY() > gameData.getMissionLoaderService().getMapSizeY() ||
                e.getPosition().getX() < 0 || e.getPosition().getY() < 0) { 
                worldData.removeEntity(e);
            }
        }

        // check for entities getting hit
        for (Bullet bullet : worldData.getEntities(CannonBullet.class)) {

            if (bullet.isDead()) {
                worldData.removeEntity(bullet);
                continue;
            }
        }
	}
}
