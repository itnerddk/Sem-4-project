package org.sdu.sem4.g7.common.services;

import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.EntityActions;

public interface ILogicService {

    /**
     * Adds the map to the static variable for caching
     * @param map
     */
    public void init(List<List<Integer>> map, GameData gameData);


    /**
     * Returns the action for the entity based on the player position
     * @param entity
     * @param playerPosition Used for range checking
     * @return
     */
    public EntityActions getAction(Entity entity, Vector2 playerPosition);

    /**
     * Finds a path between the points, returning only corner points
     * @param from Where the path starts
     * @param to where the path ends
     * @return List of corner coordinates
     */
    public ArrayList<Vector2> findPath(Entity from, Vector2 to);

    /**
     * Evaluates an action and returns a position to path find to
     * @param entity The entity itself
     * @param targetPosition The target (Player or other enemy)
     * @return Where to go
     */
    public Vector2 evaluateAction(Entity entity, Vector2 targetPosition);
}
