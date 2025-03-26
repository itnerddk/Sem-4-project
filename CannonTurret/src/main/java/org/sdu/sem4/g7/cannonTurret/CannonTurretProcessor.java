package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.services.ICollidableService;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Bullet;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurretProcessor implements IEntityProcessingService {

    /**
     * Check if the two entities is colliding
     * 
     * @param entity1
     * @param entity2
     * @return true if the overlap
     */
    private boolean collision(Entity bullet, Entity entity) {
        if (entity instanceof ICollidableService) {
            return bullet.getPosition().getX() < entity.getPosition().getX() + 60 &&
                bullet.getPosition().getX() + 60 > entity.getPosition().getX() &&
                bullet.getPosition().getY() < entity.getPosition().getY() + 60 &&
                bullet.getPosition().getY() + 60 > entity.getPosition().getY();
        }
        return false;
    }

	@Override
	public void process(GameData gameData, WorldData worldData) {
		// Implementation of the process method
        for (Entity e : worldData.getEntities(CannonTurret.class)) {
            Turret t = (Turret) e;
        }

        for (Entity e : worldData.getEntities(CannonBullet.class)) {
            CannonBullet cb = (CannonBullet) e;
            Vector2 velocity = cb.getVelocity();
            
            // System.out.println("Bullet velocity: " + velocity);
            cb.setPosition(velocity.getX() + cb.getPosition().getX(), velocity.getY() + cb.getPosition().getY());  
            
            // despawn bullet if outside the map
            if (e.getPosition().getX() > gameData.getMissionLoaderService().getMapSizeX() || e.getPosition().getY() > gameData.getMissionLoaderService().getMapSizeY() ||
                e.getPosition().getX() < 0 || e.getPosition().getY() < 0) { 
                worldData.removeEntity(e);
            }
        }

        // check for entities getting hit
        for (Bullet bullet : worldData.getEntities(CannonBullet.class)) {

            for (Entity e : worldData.getEntities()) {

                // skip itself
                if (bullet.equals(e)) { continue; }

                // skip createdBy entity
                if (bullet.getCreatedBy().equals(e)) {continue; }

                // check collission
                if (collision(bullet, e)) {
                    //System.out.println("Bullet: " + bullet.getID() + " Hit: " + e.getID() + " (" + e.getClass().getSimpleName() + ")");
                    CannonBullet cannonBullet = (CannonBullet) bullet;
                    e.setHealth(e.getHealth() - cannonBullet.getDamage());
                }

            }

        }
	}
}
