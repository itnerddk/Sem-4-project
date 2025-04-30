package org.sdu.sem4.g7.common.tests;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.WorldData;

public class FetchTest {

    public static WorldData worldData;

    public static class EntityA extends Entity {
        public EntityA() {
            super();
        }
    }
    public static class EntityB extends Entity {
        public EntityB() {
            super();
        }
    }

    @BeforeEach
    public static void setUp() {
        worldData = new WorldData();
        // Create a world with 1000 entities of different classes
        for (int i = 0; i < 1000; i++) {
            EntityA entityA = new EntityA();
            entityA.setHealth((int)Math.random() * 100);
            worldData.addEntity(entityA);

            EntityB entityB = new EntityB();
            entityB.setHealth((int)Math.random() * 100);
            worldData.addEntity(entityB);
        }
    }

    @Test
    public void testClassFetch() {
        // Test fetching entities by class
        long start = System.nanoTime();
        worldData.getEntities(EntityA.class);
        long end = System.nanoTime();
        long duration = end - start;
        System.out.println("\tTime to fetch entities of class A: " + duration + " ns");
        assertTrue(duration < 1000000, "Fetching entities of class A should be less than 1ms");

    }
}
