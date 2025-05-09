package org.sdu.sem4.g7.rayCasting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.aware.IMapAware;
import org.sdu.sem4.g7.common.aware.IWorldAware;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IRayCastingService;
import org.sdu.sem4.g7.common.services.IRigidbodyService;

public class RayCasting implements IRayCastingService, IMapAware, IWorldAware {

    private static ConcurrentHashMap<Integer, List<List<Integer>>> scaledMaps = new ConcurrentHashMap<>();
    private static WorldData worldData;

    @Override
    public void initMap(List<List<Integer>> map) {
        RayCasting.scaledMaps.put(1, map);
    }

    @Override
    public void initWorld(WorldData worldData) {
        RayCasting.worldData = worldData;
    }

    @Override
    public Vector2 isInMap(Vector2 _start, Vector2 direction, int _maxDistance, int resolution) {
        Vector2 start = new Vector2(_start).divideInt(CommonConfig.getTileSize()).multiply(resolution);
        int maxDistance = ((_maxDistance/CommonConfig.getTileSize()) * resolution);

        List<List<Integer>> localMap;
        if (scaledMaps.containsKey(resolution)) {
            localMap = scaledMaps.get(resolution);
        } else {
            scaleMap(scaledMaps.get(1), resolution);
            localMap = scaledMaps.get(resolution);
        }

        Vector2 point = new Vector2(start);
        for (int i = 0; i < maxDistance; i++) {
            point.add(direction);
            int dx = (int) Math.round(point.getX());
            int dy = (int) Math.round(point.getY());

            if (dx < 0 || dy < 0 || dx >= localMap.get(0).size() || dy >= localMap.size()) {
                return null;
            }

            if (localMap.get(dy).get(dx) == 1) {
                return point.divide(resolution).multiply(CommonConfig.getTileSize());
            }
        }

        return null;
    }

    @Override
    public Vector2 isInEntities(Vector2 start, Vector2 direction, int maxDistance, int stepSize, List<Entity> ignoreEntities, Class<? extends Entity>... entityClasses) {
        
        if (stepSize > maxDistance) return null;

        Vector2 point = new Vector2(start);
        Vector2 change = new Vector2(direction).multiply(stepSize);
        for (int i = 0; i < maxDistance; i += stepSize) {
            point.add(change);
            Hitbox pointHitbox = new Hitbox(point, new Vector2(stepSize, stepSize), 0);
            for (IRigidbodyService rb : RayCasting.worldData.getRigidBodyEntities(entityClasses)) {
                if (ignoreEntities != null && ignoreEntities.contains((Entity) rb)) {
                    continue;
                }
                if (rb.getHitbox().checkCollision(pointHitbox) != null) {
                    return point;
                }
            }
        }

        return null;
    }

    @Override
    public Vector2 isInEntities(Vector2 start, Vector2 direction, List<Entity> ignoreEntities, Class<? extends Entity>... entityClasses) {
        return this.isInEntities(start, direction, 5 * CommonConfig.getTileSize(), (int) (0.1 * CommonConfig.getTileSize()), ignoreEntities, entityClasses);
    }
    

    private static void scaleMap(List<List<Integer>> map, int scale) {
        List<List<Integer>> scaledMap = new ArrayList<>();
        for (int i = 0; i < map.size(); i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 0; j < map.get(i).size(); j++) {
                for (int k = 0; k < scale; k++) {
                    row.add(map.get(i).get(j));
                }
            }
            for (int k = 0; k < scale; k++) {
                scaledMap.add(row);
            }
        }
        RayCasting.scaledMaps.put(scale, scaledMap);
    }

}
