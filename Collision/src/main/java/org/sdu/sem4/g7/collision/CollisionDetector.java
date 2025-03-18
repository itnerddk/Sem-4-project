package org.sdu.sem4.g7.collision;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;

public class CollisionDetector implements IPostEntityProcessingService {
    @Override
    public void process(GameData gameData, WorldData world) {
        // two for loops for all entities in the world
        for (Entity entity1 : world.getEntities()) {
            for (Entity entity2 : world.getEntities()) {

                // if the two entities are identical, skip the iteration
                if (entity1.getID().equals(entity2.getID())) {
                    continue;
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
