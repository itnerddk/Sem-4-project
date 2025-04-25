package org.sdu.sem4.g7.common.services;

import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;

public interface ILogicService {

    /**
     * Adds the map to the static variable for caching
     * @param map
     */
    public void init(List<List<Integer>> map, GameData gameData);

    /**
     * Finds a path between the points, returning only corner points
     * @param from Where the path starts
     * @param to where the path ends
     * @return List of corner coordinates
     */
    public List<Vector2> findPath(Entity from, Vector2 to);
}
