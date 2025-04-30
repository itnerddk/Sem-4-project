package org.sdu.sem4.g7.ai;

import java.util.ArrayList;
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

        List<Vector2> path = new ArrayList<>();

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
            path = aStar.step();
        } else {
            path = aStar.step();
            aStarCache.remove(from.getID());
        }
            
        
        

        // return optimizePath(path);
        return path;
    }

    private List<Vector2> optimizePath(List<Vector2> path) {
        System.out.println("Path points before: " + path.size());
        List<Vector2> newPath = new ArrayList<>();
        Vector2 past;
        Vector2 current;
        Vector2 future;

        past = path.get(0);
        newPath.add(past);
        for (int i = 1; i < path.size(); i++) {
            current = path.get(i);
            future = path.get(i+1);

            // X Positive
            if(past.getX() < current.getX() && current.getX() < future.getX()) {
                // Check if they have any y difference
                if (past.getY() == current.getY() && current.getY() == future.getY()) {

                }
            } else if (past.getX() > current.getX() && current.getX() > current.getX()) {
                
            }


            past = current;
        }

        System.out.println("Path points after: " + path.size());
        return newPath;
    }

}
