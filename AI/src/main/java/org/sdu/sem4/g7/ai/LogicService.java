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
            
        
        

        return path;
    }
}
