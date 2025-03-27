package org.sdu.sem4.g7.collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IRigidbodyService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;

public class CollisionDetector implements IPostEntityProcessingService {


    /**
     * Check if the two entities is colliding
     * 
     * @param entity1
     * @param entity2
     * @return true if the overlap
     */
    private void processCollision(Entity entity1, Entity entity2) {
        if (entity1 instanceof IRigidbodyService && entity2 instanceof IRigidbodyService) {
            IRigidbodyService col1 = (IRigidbodyService) entity1;
            IRigidbodyService col2 = (IRigidbodyService) entity2;

            Vector2 collisionVector = col1.getHitbox().collides(col2.getHitbox());
            if (collisionVector != null) {
                handleCollision(entity1, entity2, collisionVector);
            }
            // return entity1.getPosition().getX() < entity2.getPosition().getX() + 60 &&
            //     entity1.getPosition().getX() + 60 > entity2.getPosition().getX() &&
            //     entity1.getPosition().getY() < entity2.getPosition().getY() + 60 &&
            //     entity1.getPosition().getY() + 60 > entity2.getPosition().getY();
        }
    }

    public void handleCollision(Entity entity1, Entity entity2, Vector2 collisionVector) {
        System.out.println(collisionVector);
        // Check which is smaller
        if (true) {
            if (collisionVector.getX() < collisionVector.getY()) {
                // X is smaller
                // Set x multiplier to -1 if entity1 is to the left of entity2
                int xMult = entity1.getPosition().getX() < entity2.getPosition().getX() ? -1 : 1;
                // Move entity1 out of entity2
                entity1.getPosition().setX(entity1.getPosition().getX() + collisionVector.getX() * xMult);
            } else {
                // Y is smaller
                // Set y multiplier to -1 if entity1 is above entity2
                int yMult = entity1.getPosition().getY() < entity2.getPosition().getY() ? -1 : 1;
                // Move entity1 out of entity2
                entity1.getPosition().setY(entity1.getPosition().getY() + collisionVector.getY() * yMult);
            }
        }
    }

    @Override
    public void process(GameData gameData, WorldData world) {
        // Get all entities and sort them by their z index
        Collection<Entity> entities = world.getEntities();
        List<Entity> sortedEntities = new ArrayList<>(entities);
        sortedEntities.sort((e1, e2) -> e1.getzIndex() - e2.getzIndex());

        // two for loops for all entities in the world
        for (Entity entity1 : sortedEntities) {
            if (!entity1.isCollision() || entity1.isImmoveable()) continue;
            for (Entity entity2 : sortedEntities) {
                // if the two entities are identical, skip the iteration
                if (!entity2.isCollision() || entity1.getID().equals(entity2.getID())) continue;
                
                processCollision(entity1, entity2);
            }
        }
    }
}
