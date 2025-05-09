package org.sdu.sem4.g7.common.services;

import java.util.List;

import org.sdu.sem4.g7.common.data.*;

public interface IRayCastingService {

    /**
     * Checks the ray up against the map with the given resolution as scaling
     * @param start The starting point of the ray (in pixels)
     * @param direction The direction of the ray (as a normalized vector)
     * @param maxDistance The maximum distance the ray should check (in pixels)
     * @param resolution How much to upscaled the map (1 = no upscaling, 2 = 2x upscaled, etc.)
     * @return The point where the ray hits the map (in pixels) or null if it doesn't hit anything
     */
    public Vector2 isInMap(Vector2 start, Vector2 direction, int maxDistance, int resolution);
    

    /**
     * Checks the ray up against all entities provided.
     * @param start The starting point of the ray (in pixels)
     * @param direction The direction of the ray (as a normalized vector)
     * @param maxDistance The maximum distance the ray should check (in pixels)
     * @param stepSize The amount of distance between each point checked (in pixels)
     * @param entityClasses The rigidbodies to check up against
     * @return The point where the ray hits an entity (in pixels) or null if it doesn't hit anything
     */
    public Vector2 isInEntities(Vector2 start, Vector2 direction, int maxDistance, int stepSize, List<Entity> ignoreEntities, Class<? extends Entity>... entityClasses);
    public Vector2 isInEntities(Vector2 start, Vector2 direction, List<Entity> ignoreEntities, Class<? extends Entity>... entityClasses);
}
