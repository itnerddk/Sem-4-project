package org.sdu.sem4.g7.collision;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.ICollidableService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;

public class CollisionDetector implements IPostEntityProcessingService {


    /**
     * Check if the two entities is colliding
     * 
     * @param entity1
     * @param entity2
     * @return true if the overlap
     */
    private boolean collision(Entity entity1, Entity entity2) {
        if (entity1 instanceof ICollidableService && entity2 instanceof ICollidableService) {
            // ICollidableService col1 = (ICollidableService) entity1;
            // ICollidableService col2 = (ICollidableService) entity2;

            // return col1.getBounds().intersects(col2.getBounds());
            return entity1.getPosition().getX() < entity2.getPosition().getX() + 60 &&
                entity1.getPosition().getX() + 60 > entity2.getPosition().getX() &&
                entity1.getPosition().getY() < entity2.getPosition().getY() + 60 &&
                entity1.getPosition().getY() + 60 > entity2.getPosition().getY();
        }
        return false;
    }

    @Override
    public void process(GameData gameData, Mission world) {
        // two for loops for all entities in the world 
        for (Entity entity1 : world.getEntities()) {
            
            if (!entity1.isCollision()) continue;
            for (Entity entity2 : world.getEntities()) {
                // if the two entities are identical, skip the iteration
                if (!entity2.isCollision() || entity1.getID().equals(entity2.getID())) continue;


                // check for collision
                if (collision(entity1, entity2)) {
                    System.out.println("Collision detected between " + entity1.getClass().getSimpleName() + " and " + entity2.getClass().getSimpleName() + System.currentTimeMillis());
                    while (collision(entity1, entity2) && !((entity1.getVelocity().getX() == 0 && entity1.getVelocity().getY() == 0) && (entity2.getVelocity().getX() == 0 && entity2.getVelocity().getY() == 0))) {
                        entity1.getPosition().subtract(entity1.getVelocity());
                        entity2.getPosition().subtract(entity2.getVelocity());
                    }
                    entity1.getVelocity().set(0, 0);
                    entity2.getVelocity().set(0, 0);

                    // if (entity1.getVelocity() != null && entity2.getVelocity() != null) {
                    //     Vector2 transferredVelocity = new Vector2();
                    //     transferredVelocity = transferredVelocity.add(entity1.getVelocity()).add(entity2.getVelocity());
                    //     transferredVelocity.set(transferredVelocity.getX() / 2, transferredVelocity.getY() / 2);
                    //     entity1.getVelocity().set(transferredVelocity);
                    //     entity2.getVelocity().set(transferredVelocity);
                    // }
                }
            }
        }
    }
}
