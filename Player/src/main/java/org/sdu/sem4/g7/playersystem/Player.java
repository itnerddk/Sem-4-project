package org.sdu.sem4.g7.playersystem;
import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;

public class Player extends Tank {
    public Player() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("Player.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("Player.png").toURI(), 5);
            this.setzIndex(-5);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Set type
        setEntityType(EntityType.PLAYER);

        // Set health and max health for player
        this.setHealth(100);
        this.setMaxHealth(100);

        // Test code to load turret
        this.setTurret(TurretLoader.getTurrets().get(0).get());
    }

    // Take damage for a player
    public void takeDamage(int damage) {
        int newHealth = Math.max(0, this.getHealth() - damage); // Ensure health doesn't go below 0
        this.setHealth(newHealth);
        System.err.println("Player health: " + this.getHealth());
    }
}