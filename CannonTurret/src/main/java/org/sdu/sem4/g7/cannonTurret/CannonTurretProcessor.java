package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurretProcessor implements IEntityProcessingService {

	@Override
	public void process(GameData gameData, WorldData mission) {
		// Implementation of the process method
        for (Entity e : mission.getEntities(CannonTurret.class)) {
            Turret t = (Turret) e;
        }

        for (Entity e : mission.getEntities(CannonBullet.class)) {
            CannonBullet cb = (CannonBullet) e;
            Vector2 velocity = cb.getVelocity();
            
            // System.out.println("Bullet velocity: " + velocity);
            cb.setPosition(velocity.getX() + cb.getPosition().getX(), velocity.getY() + cb.getPosition().getY());            
        }
	}
}
