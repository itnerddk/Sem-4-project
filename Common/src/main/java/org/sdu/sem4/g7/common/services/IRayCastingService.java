package org.sdu.sem4.g7.common.services;

import java.util.ArrayList;
import org.sdu.sem4.g7.common.data.*;

public interface IRayCastingService {

    /**
     * Initializes the ray casting service with a map
     * @param map The map to be used for ray casting
     */    
    public void init(ArrayList<ArrayList<Integer>> map);


    /**
     * Checks the ray up against the map with the given resolution as scaling
     * @param start The starting point of the ray (in pixels)
     * @param direction The direction of the ray (as a normalized vector)
     * @param maxDistance The maximum distance the ray should check (in pixels)
     * @param resolution How much to upscaled the map (1 = no upscaling, 2 = 2x upscaled, etc.)
     * @return The point where the ray hits the map (in pixels) or null if it doesn't hit anything
     */
    public Vector2 isInMap(Vector2 start, Vector2 direction, int maxDistance, int resolution);
    
    public Vector2 isInEntities(Vector2 start, Vector2 direction, int maxDistance, int stepSize, Class<? extends Entity>... entityClasses);
    public Vector2 isInEntities(Vector2 start, Vector2 direction, Class<? extends Entity> entityClasses);
}
