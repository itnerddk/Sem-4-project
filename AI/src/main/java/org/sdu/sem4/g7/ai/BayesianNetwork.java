package org.sdu.sem4.g7.ai;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.sdu.sem4.g7.common.data.Entity;
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
                    return callback.call();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            float result = 1f;
            for (Map.Entry<BayesianNode, Float> entry : parents.entrySet()) {
                BayesianNode parent = entry.getKey();
                float weight = entry.getValue();
                result *= parent.evaluate() * weight;
            }
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

        nodes = new ArrayList<>(
            List.of(
                new BayesianNode("Health Remaining", () -> healthRemaining),
                new BayesianNode("Range", () -> range),
                new BayesianNode("In Group", () -> inGroup ? 1f : 0f),
                new BayesianNode("Teammate in Line", () -> teammateInLine ? 1f : 0f),
                new BayesianNode("Attack or Flee", Map.of()),
                new BayesianNode("Attack", Map.of()),
                new BayesianNode("Flee", Map.of()),
                new BayesianNode("Group Up", Map.of()),
                new BayesianNode("Move Aside", Map.of())   
            )
        );
    }

    public EntityActions evaluate(float healthRemaining, float range, boolean inGroup, boolean teammateInLine) {
        // Set values for evaluation needs
        this.healthRemaining = healthRemaining;
        this.range = range;
        this.inGroup = inGroup;
        this.teammateInLine = teammateInLine;
        long startTime = System.nanoTime();
        // Sort nodes
        List<BayesianNode> outputNodes = new ArrayList<>();
        for(BayesianNode node : nodes){
            if(node.getAction() != null){
                outputNodes.add(node);
            }
        }

        float cumulativeChance = 0f;
        Map<BayesianNode, Float> chances = new HashMap<>();
        
        for (BayesianNode node : outputNodes) {
            float chance = node.evaluate();
            cumulativeChance += chance;
            chances.put(node, chance);
        }

        float randomValue = (float) Math.random() * cumulativeChance;
        float cumulativeValue = 0f;

        EntityActions action = null;

        for (Map.Entry<BayesianNode, Float> entry : chances.entrySet()) {
            BayesianNode node = entry.getKey();
            float chance = entry.getValue();
            cumulativeValue += chance;
            if (cumulativeValue >= randomValue) {
                action = node.getAction();
                break;
            }
        }

        // Print out the time taken for evaluation
        // System.out.print("Chosen action " + action + " time taken: ");
        // System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - startTime) / 1000.0 + "ms");

        return action;
    }
}
