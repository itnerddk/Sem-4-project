package org.sdu.sem4.g7.ai;

import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.*;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Cost {
    Vector2 key;
    int value;

    public Cost(Vector2 key, int value) {
        this.key = key;
        this.value = value;
    }
}

public class AStar {

    long started = System.nanoTime();
    long completed = 0;


    private EntityShell from; // The starting entity
    private Vector2 to; // The target position
    private GameData gameData;

    private List<List<Integer>> map; // The map representation

    private PriorityQueue<Vector2> openSet;
    private HashMap<Vector2, Vector2> cameFrom;

    private HashMap<Vector2, Integer> gScore = new HashMap<>();
    private PriorityQueue<Cost> fScore = new PriorityQueue<Cost>((a, b) -> Integer.compare(a.value, b.value)); // Lowest value out first

    public AStar(Entity from, Vector2 to, List<List<Integer>> map, GameData gameData) {
        this.gameData = gameData;
        // Initialize the A* algorithm with the starting entity and target position
        this.from = new EntityShell(from);
        this.from.setPosition(Vector2.round(this.from.getPosition().divide(CommonConfig.getTileSize())));
        this.to = Vector2.round((new Vector2(to)).divide(CommonConfig.getTileSize()));
        this.map = map;
        System.out.println("From: " + this.from.getPosition() + " To: " + this.to);

        this.openSet = new PriorityQueue<>((a, b) -> {
            return Integer.compare(heuristics(a), heuristics(b));
        });
        this.cameFrom = new HashMap<>();
    }

    public boolean isDone() {
        return openSet.isEmpty();
    }

    int steps = 0;

    public List<Vector2> step() {
        steps++;
        // System.out.println("Step: " + steps);
        Cost currentCost = fScore.peek();
        Vector2 current = currentCost.key;

        drawCircle(current, Color.YELLOW);

        if (current.equals(this.to)) {

            List<Vector2> path = reconstruct_path(cameFrom, current);
            if (completed == 0) {
                completed = System.nanoTime();
            }
            System.out.println(((double) TimeUnit.NANOSECONDS.toMicros(completed - started)) / 1000.0 + " ms");

            return path;
        }

        openSet.remove(current);
        fScore.remove(currentCost);
        for (Vector2 neighbor : getNeighbours(current)) {
            // d(current,neighbor) is the weight of the edge from current to neighbor
            // tentative_gScore is the distance from start to the neighbor through current
            // tentative_gScore := gScore[current] + d(current, neighbor)
            int tentative_gScore = gScore.get(current) + 1; // Assuming each step has a cost of 1
            // if tentative_gScore < gScore[neighbor]
            if (!gScore.containsKey(neighbor) || tentative_gScore < gScore.get(neighbor)) {
                // This path to neighbor is better than any previous one. Record it!
                // cameFrom[neighbor] := current
                // gScore[neighbor] := tentative_gScore
                // fScore[neighbor] := tentative_gScore + h(neighbor)
                // if neighbor not in openSet
                //     openSet.add(neighbor)
                cameFrom.put(neighbor, current);
                gScore.put(neighbor, tentative_gScore);
                int h = heuristics(neighbor);
                if (steps == 1) {
                    // First step, taking rotation into account
                    float rotation = (float) Math.toDegrees((Math.atan2(neighbor.getX() - current.getX(), neighbor.getY() - current.getY()) / (2 * Math.PI)));
                    float rotationDiff = Math.abs(rotation - from.getRotation());
                    h += rotationDiff / 45.0f;
                }
                fScore.add(new Cost(neighbor, tentative_gScore + h));
                if (!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                }
            }
        }

        drawCircle(this.to, Color.GREEN);

        return null;
    }

    private int heuristics(Vector2 point) {
        int h = 0;
        // Heuristic function: Manhattan distance
        // return Math.abs(point.getX() - to.getX()) + Math.abs(point.getY() - to.getY());
        // Heuristic function: Euclidean distance
        int distance = (int) Math.sqrt(Math.pow(point.getX() - to.getX(), 2) + Math.pow(point.getY() - to.getY(), 2));
        h = distance * 10;
        
        Vector2 from = this.cameFrom.containsKey(point) ? this.cameFrom.get(point) : null;
        // If it has a from, check if it's going diagonal
        if (from != null) {
            // Check if the point is diagonal to the from point
            if (Math.abs(point.getX() - from.getX()) == 1 && Math.abs(point.getY() - from.getY()) == 1) {
                h += 4; // Add a penalty for diagonal movement
                // System.out.print("Diagonal ");;
            }
        }

        // System.out.println("H: " + h);
        return h;
    }

    public List<Vector2> getNeighbours(Vector2 point) {
        // System.out.println("Getting neighbours for: " + point);
        List<Vector2> neighbours = new ArrayList<>();

        // Check the possible directions
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
        for (int[] dir : directions) {
            int newX = (int) point.getX() + dir[0];
            int newY = (int) point.getY() + dir[1];

            // Check if the new position is within bounds and not an obstacle
            if (newX >= 0 && newX < map.get(0).size() && newY >= 0 && newY < map.size()) {
                if (map.get(newY).get(newX) == 0) {
                    neighbours.add(new Vector2(newX, newY));
                    drawCircle(new Vector2(newX, newY), Color.BLUE);
                }
            }
        }
        return neighbours;
    }

    public List<Vector2> findPath() {

        Vector2 start = from.getPosition();
        openSet.add(start);
        
        
        // For node n, gScore[n] is the currently known cost of the cheapest path from start to n.
        // gScore := map with default value of Infinity
        gScore = new HashMap<>();

        // gScore[start] := 0
        gScore.put(start, 0);

        // For node n, fScore[n] := gScore[n] + h(n). fScore[n] represents our current best guess as to
        // how cheap a path could be from start to finish if it goes through n.
        // fScore := map with default value of Infinity
        fScore.clear();

        // fScore[start] := heuristics(start)
        fScore.add(new Cost(start, heuristics(start)));

        // while openSet is not empty
        return step();
        // Open set is empty but goal was never reached
        
    }

    private List<Vector2> reconstruct_path(HashMap<Vector2, Vector2> cameFrom, Vector2 current) {
        List<Vector2> totalPath = new ArrayList<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.add(current);
        }
        return totalPath;
    }


    public boolean isTo(Vector2 to) {
        // Check if the target position is the same as the current position
        Vector2 inToClone = new Vector2(to);
        inToClone = Vector2.round(inToClone.divide(CommonConfig.getTileSize()));
        return this.to.equals(inToClone);
    }


    private void drawCircle(Vector2 vector2, Color color) {
        gameData.gc.save();
        // Draw circle at vector2 position
        gameData.gc.setFill(color);
        gameData.gc.setStroke(color);
        gameData.gc.setLineWidth(Math.min(steps, CommonConfig.DEFAULT_SCALING*2));
        gameData.gc.strokeOval(
                (int) vector2.getX() * CommonConfig.getTileSize() - CommonConfig.getTileSize()/2,
                (int) vector2.getY() * CommonConfig.getTileSize() - CommonConfig.getTileSize()/2,
                CommonConfig.getTileSize(),
                CommonConfig.getTileSize()
        );
        gameData.gc.restore();
    }
}
