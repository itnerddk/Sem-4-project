package org.sdu.sem4.g7.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.EntityActions;
import org.sdu.sem4.g7.common.services.ILogicService;

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

public class LogicService implements ILogicService {
    
    static List<List<Integer>> map;
    static GameData gameData;

    @Override
    public void init(List<List<Integer>> map, GameData gameData) {
        LogicService.map = map;
        LogicService.gameData = gameData;
    }

    BayesianNetwork bayesianNetwork = new BayesianNetwork();
    HashMap<CompositeKey, CompositeValue> decisionMap = new HashMap<>();


    private CompositeValue getOrCreateValue(CompositeKey key, Entity from, Vector2 to) {
        CompositeValue compValue = decisionMap.computeIfPresent(key, (_key, val) -> {return val;});
        if (compValue == null) {
            decisionMap.put(key, new CompositeValue(new AStar(from, to, map, gameData), to, EntityActions.IDLE, 0));
            compValue = decisionMap.get(key);
        }
        return compValue;
    }


    @Override
    public EntityActions getAction(Entity entity, Vector2 playerPosition) {
        // Get the health of the entity
        float health = entity.getHealth() / entity.getMaxHealth();
        // Get the distance to the player
        float distance = (float) entity.getPosition().distance(playerPosition);
        // Get the range modifier
        float rangeModifier = (float) ((-0.17f * Math.pow(distance, 2)) + (1.5f * distance) - 2.33f);
        rangeModifier = Math.clamp(rangeModifier, 0.0f, 1.0f);

        CompositeValue compValue = getOrCreateValue(new CompositeKey(entity.getID()), entity, playerPosition);

        // If 5 seconds has passed since last decision, re-evaluate
        if ((System.currentTimeMillis() - compValue.decisionTaken) > 5*1000) {
            System.out.println("Time passed re-evaluate");
            compValue.action = bayesianNetwork.evaluate(health, rangeModifier, false, false);
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

}
