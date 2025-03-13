package org.sdu.sem4.g7.playersystem;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;

public class Player extends Tank {
    public Player() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("Player.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("Player.png").toURI());
            this.setzIndex(-5);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // Test code to load turret
        this.setTurret(TurretLoader.getTurrets().get(0));
    }

    @Override
    public List<Entity> getChildren() {
        List<Entity> children = new ArrayList<>();
        if (getTurret() != null) {
            children.add(getTurret());
        }
        return children;
    }
}