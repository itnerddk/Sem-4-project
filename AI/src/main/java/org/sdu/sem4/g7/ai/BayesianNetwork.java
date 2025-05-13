package org.sdu.sem4.g7.ai;

import java.util.*;
import java.util.concurrent.Callable;

import org.sdu.sem4.g7.common.enums.EntityActions;

public class BayesianNetwork {
    // Node System
    class BayesianNode {
        String name;
        int iteration = 0;
        float savedValue = 0f;
        Map<BayesianNode, Float> parents;
        List<BayesianNode> children = new ArrayList<>();

        // In case of outside value input
        private Callable<Float> callback = null;

        // In case of action
        private EntityActions action = null;

        public BayesianNode(String name, Callable<Float> callback) {
            this.name = name;
            this.callback = callback;
        }

        public BayesianNode(String name, Map<BayesianNode, Float> parents, EntityActions action) {
            this(name, parents);
            this.action = action;
        }

        public BayesianNode(String name, Map<BayesianNode, Float> parents) {
            this.name = name;
            this.parents = parents;
            for (BayesianNode parent : parents.keySet()) {
                parent.addChild(this);
            }
        }

        public float evaluate(int iteration) {
            if (this.iteration == iteration) {
                return savedValue;
            }
            if (callback != null) {
                try {
                    float value = callback.call();
                    // System.out.println("Evaluated " + name + " to " + value);
                    this.iteration = iteration;
                    this.savedValue = value;
                    return value;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            float result = 0f;
            for (Map.Entry<BayesianNode, Float> entry : parents.entrySet()) {
                BayesianNode parent = entry.getKey();
                // If the weight is negative, then the parent is inverted
                float weight = entry.getValue();
                float parentValue = parent.evaluate(iteration);
                if (weight < 0) {
                    parentValue = 1 - parentValue;
                }
                // System.out.println("Evaluated " + parent.name + " to " + parentValue);
                result += parentValue * Math.abs(weight);
            }
            // System.out.println("Evaluated " + name + " to " + result);
            return result;
        }

        public void addChild(BayesianNode child) {
            children.add(child);
        }

        public EntityActions getAction() {
            return action;
        }
    }

    List<BayesianNode> nodes = new ArrayList<>();

    float healthRemaining;
    float range;
    float inGroup;
    boolean teammateOnMap;
    boolean teammateInLine;

    public BayesianNetwork() {
        this.nodes = new ArrayList<>();
        BayesianNode HR = new BayesianNode("Health Remaining", () -> {
            return healthRemaining;
        });
        BayesianNode Ra = new BayesianNode("Range", () -> {
            return range;
        });
        BayesianNode IG = new BayesianNode("In Group", () -> {
            return inGroup;
        });
        BayesianNode TI = new BayesianNode("Teammate in Line", () -> {
            return teammateInLine ? 1f : 0f;
        });
        BayesianNode TOM = new BayesianNode("Teammate on Map", () -> {
            return teammateOnMap ? 0f : -1f;
        });

        // Add nodes to the network
        BayesianNode AoF = new BayesianNode("Attack or Flee", Map.of(
                HR, 0.6f,
                Ra, 0.2f,
                IG, 0.2f));

        this.nodes.add(new BayesianNode("Group Up", Map.of(
                HR, -0.4f,
                IG, -0.2f,
                AoF, -0.4f,
                TOM, 1.0f), EntityActions.GROUP_UP));

        BayesianNode Att = new BayesianNode("Attack", Map.of(
                AoF, 1f), EntityActions.ATTACK);

        BayesianNode Flee = new BayesianNode("Flee", Map.of(
                AoF, -0.8f), EntityActions.FLEE);

        BayesianNode MA = new BayesianNode("Move Aside", Map.of(
                TI, 0.9f,
                Att, 0.1f), EntityActions.MOVE_ASIDE);

        this.nodes.addAll(List.of(HR, Ra, IG, TI, AoF, Att, Flee, MA));
    }

    public HashMap<EntityActions, Float> evaluate(float healthRemaining, float range, float inGroup, boolean teammateOnMap,
            boolean teammateInLine, int iteration) {
        // Set values for evaluation needs
        this.healthRemaining = healthRemaining;
        this.range = range;
        this.inGroup = inGroup;
        this.teammateOnMap = teammateOnMap;
        this.teammateInLine = teammateInLine;
        long startTime = System.nanoTime();
        // Sort nodes

        HashMap<EntityActions, Float> chances = new HashMap<>();
        float cumulativeChance = 0f;

        for (BayesianNode node : nodes) {
            if (node.getAction() != null) {
                float chance = node.evaluate(iteration);
                cumulativeChance += chance;
                chances.put(node.action, chance);
            }
        }
        // System.out.println(chances.size());

        for (Map.Entry<EntityActions, Float> entry : chances.entrySet()) {
            EntityActions action = entry.getKey();
            float chance = entry.getValue();
            // Normalize the chance
            chances.put(action, chance / cumulativeChance);
        }

        // Print out the chances
        for (Map.Entry<EntityActions, Float> entry : chances.entrySet()) {
            EntityActions action = entry.getKey();
            float chance = entry.getValue();
            // System.out.println("Action: " + action + ", Chance: " + chance);
        }

        // Print out the time taken for evaluation
        // System.out.print("Chosen action " + action + " time taken: ");
        // System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() -
        // startTime) / 1000.0 + "ms");
        // System.out.println("Chosen action " + action + " time taken: " +
        // (System.nanoTime() - startTime) / 1000000.0 + "ms");
        return chances;
    }

    public EntityActions pickAction(HashMap<EntityActions, Float> chances) {
        // Pick the action with the highest chance
        EntityActions action = null;
        float maxChance = 0f;
        for (Map.Entry<EntityActions, Float> entry : chances.entrySet()) {
            if (entry.getValue() > maxChance) {
                maxChance = entry.getValue();
                action = entry.getKey();
            }
        }
        return action;
    }
}
