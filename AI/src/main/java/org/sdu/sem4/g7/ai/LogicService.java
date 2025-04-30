package org.sdu.sem4.g7.ai;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.ILogicService;

public class LogicService implements ILogicService {
    
    static List<List<Integer>> map;
    static GameData gameData;

    @Override
    public void init(List<List<Integer>> map, GameData gameData) {
        LogicService.map = map;
        LogicService.gameData = gameData;
    }

    HashMap<String, AStar> aStarCache = new HashMap<>();

    @Override
    public List<Vector2> findPath(Entity from, Vector2 to) {

        BayesianNetwork bayesianNetwork = new BayesianNetwork();
        float health = from.getHealth() / from.getMaxHealth();
        float range = (float) new Vector2(from.getPosition()).distance(to) / 5f;
        bayesianNetwork.evaluate(health, range, false, false);

        ArrayList<Vector2> path = new ArrayList<>();

        AStar aStar = null;

        if (aStarCache.containsKey(from.getID())) {
            aStar = aStarCache.get(from.getID());
            if (!aStar.isTo(to)) {
                aStar = new AStar(from, to, map, gameData);
                aStar.findPath();
                aStarCache.put(from.getID(), aStar);
            }
        } else {
            aStar = new AStar(from, to, map, gameData);
            aStar.findPath();
            aStarCache.put(from.getID(), aStar);
        }

        if (!aStar.isDone()) {
            path = (ArrayList) aStar.step();
        } else {
            path = (ArrayList) aStar.step();
            aStarCache.remove(from.getID());
        }
            
        
        
        return optimizePath(path);
        // return path;
    }

    private ArrayList<Vector2> optimizePath(ArrayList<Vector2> path) {
        long startTime = System.nanoTime();
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
        newPath.add(path.getLast());

        // reversed as A* finds the path from target to start

        ArrayList<Vector2> reversedPath = new ArrayList<>();
        for (int i = newPath.size() - 1; i >= 0; i--) {
            reversedPath.add(newPath.get(i));
        }
        

        return reversedPath;
    }

}
