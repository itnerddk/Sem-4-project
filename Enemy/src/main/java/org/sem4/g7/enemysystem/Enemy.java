package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Tank {
    public Enemy() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("EnemyTank.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("EnemyTank.png").toURI());
            this.setzIndex(-3);
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
            System.out.println("Turret exists");
            children.add(getTurret());
        }
        return children;
    }

}
