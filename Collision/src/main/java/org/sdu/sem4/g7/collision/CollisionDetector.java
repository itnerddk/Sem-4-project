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
            ICollidableService col1 = (ICollidableService) entity1;
            ICollidableService col2 = (ICollidableService) entity2;

            return col1.getShape().intersects(col2.getShape().getBoundsInParent());
        }
        return false;
    }

    @Override
    public void process(GameData gameData, Mission world) {
        // two for loops for all entities in the world 
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // if the two entities are identical, skip the iteration
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
                }

                // check for collision
                if (collision(entity1, entity2)) {
                    System.out.println("Collision detected between " + entity1.getClass().getSimpleName() + " and " + entity2.getClass().getSimpleName());
                }

                // CollisionDetection
/*                if (!entity1.getID().equals(entity2.getID())) {
                    System.out.println("Collision between " + entity1 + " and " + entity2 + " detected");
                    continue;
                }
            */
            }
        }
    }
}
