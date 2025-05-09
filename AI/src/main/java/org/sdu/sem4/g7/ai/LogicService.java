package org.sdu.sem4.g7.ai;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.aware.IGameDataAware;
import org.sdu.sem4.g7.common.aware.IMapAware;
import org.sdu.sem4.g7.common.aware.IWorldAware;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.EntityActions;
import org.sdu.sem4.g7.common.services.ILogicService;
import org.sdu.sem4.g7.common.services.IRigidbodyService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

class CompositeKey {
    String id;

    public CompositeKey(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CompositeKey) {
            CompositeKey other = (CompositeKey) obj;
            return this.id.equals(other.id);
        }
        return false;
    }

    @Override
    public String toString() {
        return "CompositeKey [id=" + id + "]";
    }
}
class CompositeValue {
    public AStar aStar;
    public Vector2 to;
    public EntityActions action;
    public long decisionTaken;

    public CompositeValue(AStar aStar, Vector2 to, EntityActions action, long decisionTaken) {
        this.aStar = aStar;
        this.to = to;
        this.action = action;
        this.decisionTaken = decisionTaken;
    }

    @Override
    public String toString() {
        return "CompositeValue [aStar=" + aStar + ", to=" + to + ", action=" + action
                + ", decisionTaken=" + decisionTaken + "]";
    }
}

public class LogicService implements ILogicService, IMapAware, IGameDataAware, IWorldAware {
    
    static List<List<Integer>> map;
    static GameData gameData;
    static WorldData worldData;

    private static final EnumMap<EntityActions, BiFunction<Entity, Vector2, Vector2>> actionMap = new EnumMap<>(EntityActions.class);

    static {
        actionMap.put(EntityActions.IDLE, (entity, playerPosition) -> {return null;});
        actionMap.put(EntityActions.MOVING, (entity, playerPosition) -> {return null;});

        actionMap.put(EntityActions.ATTACK, (entity, playerPosition) -> attack(entity, playerPosition));
        actionMap.put(EntityActions.FLEE, (entity, playerPosition) -> flee(entity, playerPosition));
        actionMap.put(EntityActions.GROUP_UP, (entity, playerPosition) -> groupUp(entity, playerPosition));
        actionMap.put(EntityActions.MOVE_ASIDE, (entity, playerPosition) -> moveAside(entity, playerPosition));
    }

    @Override
    public void initMap(List<List<Integer>> map) {
        LogicService.map = map;
    }

    @Override
    public void initGameData(GameData gameData) {
        LogicService.gameData = gameData;
    }

    @Override
    public void initWorld(WorldData worldData) {
        LogicService.worldData = worldData;
    }

    BayesianNetwork bayesianNetwork = new BayesianNetwork();
    HashMap<CompositeKey, CompositeValue> decisionMap = new HashMap<>();


    private CompositeValue getOrCreateValue(CompositeKey key, Entity from, Vector2 to) {
        CompositeValue compValue = decisionMap.computeIfPresent(key, (_key, val) -> {return val;});
        if (compValue == null) {
            decisionMap.put(key, new CompositeValue(new AStar(from, to, map, gameData), to, EntityActions.IDLE, 0));
            compValue = decisionMap.get(key);
            getAction(from, to);
        }
        return compValue;
    }


    @Override
    public EntityActions getAction(Entity entity, Vector2 playerPosition) {
        // Get the health of the entity
        float health = (float) entity.getHealth() / (float) entity.getMaxHealth();
        // Get the distance to the player
        float distance = (float) entity.getPosition().distance(playerPosition);
        // Get the range modifier
        float rangeModifier = (float) ((-0.17f * Math.pow(distance, 2)) + (1.5f * distance) - 2.33f);
        rangeModifier = Math.clamp(rangeModifier, 0.0f, 1.0f);

        CompositeValue compValue = getOrCreateValue(new CompositeKey(entity.getID()), entity, playerPosition);

        // If 5 seconds has passed since last decision, re-evaluate
        if ((System.currentTimeMillis() - compValue.decisionTaken) > (5*1000) * (Math.random() + 0.5f)) {
            // System.out.println("Time passed re-evaluate");

            // Filter only the two closest entities of the same type
            final List<Entity> nearbyEntities = new ArrayList<>();
            if (worldData != null) {
                nearbyEntities.addAll((List<Entity>) LogicService.worldData.getEntities(entity.getClass()).stream()
                    .filter(e -> e.getID() != entity.getID())
                    .filter(e -> e.getPosition().distance(entity.getPosition()) < 5 * CommonConfig.getTileSize())
                    .sorted((e1, e2) -> Double.compare(e1.getPosition().distance(entity.getPosition()), e2.getPosition().distance(entity.getPosition())))
                    .limit(2)
                    .toList());
            }

            final boolean[] raycasts = {false, false, false, false}; // 0: inline, 1: in range, 2: group 1, 3: group 2
            // Check if can see player
            ServiceLocator.getRayCastingService().ifPresent(
                (rayCasting) -> {
                    Vector2 directionVector = new Vector2(playerPosition).subtract(entity.getPosition()).normalize();
                    
                    // System.out.println("Direction vector: " + directionVector);
                    if (rayCasting.isInEntities(entity.getPosition(), directionVector, List.of(entity), entity.getClass()) != null) {
                        // System.out.println("Hitting another of same entity type");
                        raycasts[0] = true;
                    }
                    if (rayCasting.isInMap(entity.getPosition(), directionVector, (int) distance, 5) == null) {
                        // System.out.println("No tiles in the way");
                        raycasts[1] = true;
                    }
                    Vector2 directionVector2;
                    if (nearbyEntities.size() == 0) {
                        return;
                    }
                    for (Entity e : nearbyEntities) {
                        directionVector2 = new Vector2(e.getPosition()).subtract(entity.getPosition()).normalize();
                        int distance2 = (int) new Vector2(e.getPosition()).distance(entity.getPosition());
                        if (rayCasting.isInEntities(entity.getPosition(), directionVector2, distance2, 5, List.of(entity, e), entity.getClass()) != null) { 
                            // System.out.println("Hitting another entity");
                            if (raycasts[2] == false) {
                                raycasts[2] = true;
                            } else {
                                raycasts[3] = true;
                            }
                        }
                    }
                }
            );
            
            if (raycasts[1]) {
                rangeModifier = 0;
            }

            float inGroup = 0;
            if (raycasts[2]) {
                inGroup = 0.5f;
            } else if (raycasts[3]) {
                inGroup = 1.0f;
            }

            boolean teammateOnMap = false;
            if (raycasts[0] || raycasts[2] || raycasts[3]) {
                teammateOnMap = true;
            } else {
                teammateOnMap = LogicService.worldData.getEntities(entity.getClass()).size() > 1;
            }

            HashMap<EntityActions, Float> chances = bayesianNetwork.evaluate(health, rangeModifier, inGroup, teammateOnMap, raycasts[0]);
            // Get the action with the highest chance
            EntityActions newAction = bayesianNetwork.pickAction(chances);
            // If the old action still has a high chance, keep it
            // System.out.println(compValue.action);
            if (!chances.containsKey(compValue.action)) {
            } else if (compValue.action != null && chances.get(compValue.action) > 0.4f && chances.get(compValue.action) > chances.get(newAction) + 0.1f) {
                newAction = compValue.action;
            } else {
                compValue.aStar = null;
            }
            // Set the new action
            compValue.action = newAction;
            compValue.decisionTaken = System.currentTimeMillis();
        }
        // Return the action
        return compValue.action;
    }

    @Override
    public ArrayList<Vector2> findPath(Entity from, Vector2 _to) {

        Vector2 to = new Vector2(_to).divideInt(CommonConfig.getTileSize());

        CompositeKey key = new CompositeKey(from.getID());
        // Check if decision map has a value if not, make one.
        CompositeValue compValue = getOrCreateValue(key, from, to);
        compValue.to = to;

        ArrayList<Vector2> path = null;
        if (compValue.aStar == null) {
            compValue.aStar = new AStar(from, to, map, gameData);
            path = compValue.aStar.step();
        } else if (compValue.aStar.getTo().equals(compValue.to)) {
            path = compValue.aStar.step();
        } else {
            compValue.aStar = new AStar(from, to, map, gameData);
            path = compValue.aStar.step();
        }

        // If the path isn't empty set astar to null
        if (path != null && !path.isEmpty()) {
            compValue.aStar = null;
        }

        return optimizePath(path);
        // return path;
    }

    private ArrayList<Vector2> optimizePath(ArrayList<Vector2> path) {
        if (path == null || path.size() < 3) {
            return path;
        }
        ArrayList<Vector2> newPath = new ArrayList<>();
        Vector2 past;
        Vector2 current;
        Vector2 future;

        past = path.get(0);
        newPath.add(past);
        for (int i = 1; i < path.size() - 1; i++) {
            past = path.get(i-1);
            current = path.get(i);
            future = path.get(i+1);

            if (new Vector2(past).subtract(current).equals(new Vector2(current).subtract(future))) {
                continue;
            }

            newPath.add(current);

        }
        newPath.add(path.get(path.size() - 1));

        // reversed as A* finds the path from target to start

        ArrayList<Vector2> reversedPath = new ArrayList<>();
        for (int i = newPath.size() - 1; i >= 0; i--) {
            reversedPath.add(newPath.get(i));
        }
        

        return reversedPath;
    }


    @Override
    public Vector2 evaluateAction(Entity entity, Vector2 targetPosition) {
        CompositeValue compValue = getOrCreateValue(new CompositeKey(entity.getID()), entity, targetPosition);
        if (compValue.to.equals(new Vector2(targetPosition).divideInt(CommonConfig.getTileSize()))) {
            return targetPosition;
        }
        // System.out.println("Evaluating " + compValue.action.name() + " action for entity: " + entity.getID() + " with target position: " + new Vector2(targetPosition).divideInt(CommonConfig.getTileSize()));
        return LogicService.actionMap.get(compValue.action).apply(entity, targetPosition);
    }


    private static boolean isPathClear(Vector2 from, Vector2 to) {
        // Placeholder for actual raycast or collision check
        // This should check if the path between from and to is clear
        final boolean[] clear = {true};
        ServiceLocator.getRayCastingService().ifPresent(
            (rayCasting) -> {
                Vector2 directionVector = new Vector2(to).subtract(from).normalize();
                if (rayCasting.isInMap(from, directionVector, (int) from.distance(to), 5) != null) {
                    // System.out.println("Hitting a tile");
                    clear[0] = false;
                }
            }
        );
        return clear[0];
    }

    private static Vector2 attack(Entity entity, Vector2 targetPosition) {
        // This should only be called if the entity is in range of the target
        // Path towards the target but stay 3 tiles away from the target
        Vector2 target = new Vector2(targetPosition);
        Vector2 from = new Vector2(entity.getPosition());
        Vector2 direction = new Vector2(target).subtract(from).normalize();
        Vector2 newTarget = target.subtract(direction.multiply(3 * CommonConfig.getTileSize()));
        
        return closestPoint(newTarget);
    }
    
    private static Vector2 flee(Entity entity, Vector2 targetPosition) {
        // Path towards the target but stay 8 tiles away from the target
        Vector2 target = new Vector2(targetPosition);
        Vector2 from = new Vector2(entity.getPosition());
        Vector2 direction = new Vector2(target).subtract(from).normalize();
        Vector2 newTarget = target.subtract(direction.multiply(8 * CommonConfig.getTileSize()));
        
        return closestPoint(newTarget);
    }
    
    private static Vector2 groupUp(Entity entity, Vector2 targetPosition) {
        // System.out.println("Trying to group up");
        // Find nearest entity of the same type and move towards it

        Optional<? extends Entity> closestEntity = LogicService.worldData.getEntities(entity.getClass()).stream()
            .filter(e -> !e.getID().equals(entity.getID()))
            .sorted((e1, e2) -> Double.compare(e1.getPosition().distance(entity.getPosition()), e2.getPosition().distance(entity.getPosition())))
            .findFirst();

        if (!closestEntity.isPresent()) {
            System.out.println("No other entity found");
            return null; // No other entity found
        }
        // System.out.println("I (" + entity.getID() + ") am trying to group up with " + closestEntity.get().getID());
        Vector2 target = new Vector2(closestEntity.get().getPosition());
        Vector2 from = new Vector2(entity.getPosition());
        Vector2 direction = new Vector2(target).subtract(from).normalize();
        Vector2 newTarget = target.subtract(direction.multiply(3 * CommonConfig.getTileSize()));

        return closestPoint(newTarget);
    }

    private static Vector2 moveAside(Entity entity, Vector2 targetPosition) {
        Vector2 currentPos = entity.getPosition();
        Vector2 toTarget = targetPosition.copy().subtract(currentPos);
        
        // Get a perpendicular direction to sidestep
        Vector2 perp = new Vector2(-toTarget.getY(), toTarget.getX()).normalize(); // Clockwise perpendicular
        
        float sidestepDistance = 2.0f * CommonConfig.getTileSize(); // Tweak this value as needed
    
        // Try both sides and pick the one that’s clear
        Vector2 option1 = currentPos.copy().add(perp.copy().multiply(sidestepDistance));
        Vector2 option2 = currentPos.copy().subtract(perp.copy().multiply(sidestepDistance));
    
        // Placeholder for actual raycast or collision check
        boolean option1Clear = isPathClear(currentPos, option1);
        boolean option2Clear = isPathClear(currentPos, option2);
    
        if (option1Clear && !option2Clear) return option1;
        if (!option1Clear && option2Clear) return option2;
        if (option1Clear && option2Clear) {
            // Pick the one closer to the original direction of motion
            return option1.distance(targetPosition) < option2.distance(targetPosition) ? closestPoint(option1) : closestPoint(option2);
        }
    
        // If both are blocked, just try to move back a bit
        return closestPoint(currentPos.copy().subtract(toTarget.normalize().multiply(0.5f)));
    }
    

    private static Vector2 closestPoint(Vector2 from) {
        if (from == null) {
            return null;
        }
        // Check the map of the from point and find the closest open point.
        Vector2 point = new Vector2(from).divideInt(CommonConfig.getTileSize());

        if (point.getX() < 0) point.setX(0);
        if (point.getY() < 0) point.setY(0);
        if (point.getX() >= map.get(0).size()) point.setX(map.get(0).size() - 1);
        if (point.getY() >= map.size()) point.setY(map.size() - 1);

        // Check if the point is open
        if (map.get((int) point.getY()).get((int) point.getX()) == 0) {
            return point.multiply(CommonConfig.getTileSize());
        }
        
        // Walk in a circle until we find an open point
        for (int i = 1; i < 10; i++) {
            for (int j = -i; j <= i; j++) {
                for (int k = -i; k <= i; k++) {
                    if (Math.abs(j) + Math.abs(k) == i) {
                        Vector2 newPoint = new Vector2(point).add(j, k);
                        if (newPoint.getX() >= 0 && newPoint.getX() < map.get(0).size() && newPoint.getY() >= 0 && newPoint.getY() < map.size()) {
                            if (map.get((int) newPoint.getY()).get((int) newPoint.getX()) == 0) {
                                return newPoint.multiply(CommonConfig.getTileSize());
                            }
                        }
                    }
                }
            }
        }
        // If we don't find an open point, return null
        return null;
    }

}
