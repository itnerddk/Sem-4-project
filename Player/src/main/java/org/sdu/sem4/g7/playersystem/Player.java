package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Player extends Tank {
    public Player() {
        super();
        this.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0.45, 0, 0, 0));
        this.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
        this.setzIndex(-5);

        // Set entity type
        setEntityType(EntityType.PLAYER);

        // Set health and max health for player
        this.setHealth(100);
        this.setMaxHealth(100);

        // Test code to load turret
        try {
            this.setTurret(TurretLoader.getTurrets().get(0).get());
        } catch (Exception e) {
            System.out.println("Error loading turret: " + e.getMessage());
        }
    }

    @Override
    public void setTurret(Turret turret) {
        super.setTurret(turret);
        turret.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0.45, 0, 0, 0));
        turret.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
    }

    // Take damage for a player
    public void takeDamage(int damage) {
        int newHealth = Math.max(0, this.getHealth() - damage); // Ensure health doesn't go below 0
        this.setHealth(newHealth);
        System.err.println("Player health: " + this.getHealth());
    }
}