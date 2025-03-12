package org.sdu.sem4.g7.testmission;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Mission;


public class TestMission extends Mission {


    public TestMission() {
    }

    @Override
    public void load() {

        Entity floor = new Entity();
        floor.setPosition(0, 0);
        floor.setzIndex(-1);
        floor.setCollision(false);
        floor.setImmoveable(true);
        floor.setHealth(1);
        try {
            System.out.println(TestMission.class.getClassLoader().getResource("TestLevel.png"));
            floor.setSprite(TestMission.class.getClassLoader().getResource("TestLevel.png").toURI());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.addEntity(floor);

        try {
            Entity e = this.getEntityTypes().get("Players").get(0).getDeclaredConstructor().newInstance();
            e.setPosition(300, 300);
            e.setHealth(100);
            this.addEntity(e);
            this.addEntity(e.getChildren());
        } catch (Exception e) { // Exception instead of specific exceptions as there are 6-7 specific exceptions that can be thrown
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}