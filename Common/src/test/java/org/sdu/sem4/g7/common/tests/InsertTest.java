package org.sdu.sem4.g7.common.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.WorldData;

public class InsertTest {

    public static WorldData worldData;

    @BeforeAll
    public static void setUp() {
        worldData = new WorldData();
    }

    @Test
    public void testAddEntityTime() {
        // Single entity time
        long single = 0;
        long average = 0;

        // Test adding entity time
        for (int i = 0; i < 1000; i++) {
            Entity entity = new Entity();
            entity.setHealth((int)Math.random() * 100);

            long start = System.nanoTime();
            worldData.addEntity(entity);
            long end = System.nanoTime();
            long duration = end - start;
            if (single == 0) {
                single = duration * 2;
            } else {
                assertTrue(duration < single * 2, "Entity time should be less than 2x single time");
                average += duration;
            }
        }
        average = average / 10000;
        System.out.println("\tAverage time to add entity: " + average + " ns" + " (should be less than " + single*2 + " ns)");
    }
}