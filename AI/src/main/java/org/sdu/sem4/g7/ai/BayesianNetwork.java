package org.sdu.sem4.g7.ai;

import java.util.*;
import java.util.concurrent.Callable;

import org.sdu.sem4.g7.common.enums.EntityActions;

public class BayesianNetwork {
    // Node System
    class BayesianNode {
        String name;
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

        public float evaluate() {
            if (callback != null) {
                try {
                    float value = callback.call();
                    System.out.println("Evaluated " + name + " to " + value);
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
                float parentValue = parent.evaluate();
                if (weight < 0) {
                    parentValue = 1 - parentValue;
                }
                // System.out.println("Evaluated " + parent.name + " to " + parentValue);
                result += parentValue * Math.abs(weight);
            }
            System.out.println("Evaluated " + name + " to " + result);
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
    boolean inGroup;
    boolean teammateInLine;

    public BayesianNetwork() {
        this.nodes = new ArrayList<>(
            List.of(
                new BayesianNode("Health Remaining", () -> {return healthRemaining;}),
                new BayesianNode("Range", () -> {return range;}),
                new BayesianNode("In Group", () -> {return inGroup ? 1f : 0f;}),
                new BayesianNode("Teammate in Line", () -> {return teammateInLine ? 1f : 0f;})
            )
        );
        // Add nodes to the network
        this.nodes.add(new BayesianNode("Attack or Flee", Map.of(
            this.nodes.get(0), 0.4f,
            this.nodes.get(1), 0.4f,
            this.nodes.get(2), 0.2f
        )));

        this.nodes.add(new BayesianNode("Group Up", Map.of(
            this.nodes.get(0), 0.5f,
            this.nodes.get(2), -0.5f
        ), EntityActions.GROUP_UP));

        this.nodes.add(new BayesianNode("Attack", Map.of(
            this.nodes.get(4), 1f
        ), EntityActions.ATTACK));

        this.nodes.add(new BayesianNode("Flee", Map.of(
            this.nodes.get(4), -1f
        ), EntityActions.FLEE));

        this.nodes.add(new BayesianNode("Move Aside", Map.of(
            this.nodes.get(3), 0.9f,
            this.nodes.get(6), 0.1f
        ), EntityActions.MOVE_ASIDE));
    }

    public HashMap<EntityActions, Float> evaluate(float healthRemaining, float range, boolean inGroup, boolean teammateInLine) {
        // Set values for evaluation needs
        this.healthRemaining = healthRemaining;
        this.range = range;
        this.inGroup = inGroup;
        this.teammateInLine = teammateInLine;
        long startTime = System.nanoTime();
        // Sort nodes

        HashMap<EntityActions, Float> chances = new HashMap<>();
        float cumulativeChance = 0f;

        for(BayesianNode node : nodes){
            if(node.getAction() != null){
                float chance = node.evaluate();
                cumulativeChance += chance;
                chances.put(node.action, chance);
            }
        }
        System.out.println(chances.size());


        for (Map.Entry<EntityActions, Float> entry : chances.entrySet()) {
            EntityActions action = entry.getKey();
            float chance = entry.getValue();
            // Normalize the chance
            chances.put(action, chance / cumulativeChance);
        }

        // Print out the time taken for evaluation
        // System.out.print("Chosen action " + action + " time taken: ");
        // System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTime) / 1000.0 + "ms");
        // System.out.println("Chosen action " + action + " time taken: " + (System.nanoTime() - startTime) / 1000000.0 + "ms");
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
