package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Player extends Tank {
    public Player() {
        super();

        this.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0.45, 0, 0, 0));
        this.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
        this.setzIndex(-5);
        setEntityType(EntityType.PLAYER);

        // Health
        int baseHealth = 100;
        int bonusHealth = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getHealthBonus)
                .orElse(0);
        int totalHealth = baseHealth + bonusHealth;
        this.setMaxHealth(totalHealth);
        this.setHealth(totalHealth);

        // Speed
        float speedMultiplier = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getSpeedMultiplier)
                .orElse(0f);

        // Base values
        double baseMaxSpeed = 0.5;
        double baseAcceleration = 1.2;
        double baseDeceleration = 0.8;

        // Apply speed multiplier based on upgrades
        this.setMaxSpeed(Math.min(baseMaxSpeed + speedMultiplier, 0.75));
        this.setAcceleration(baseAcceleration + speedMultiplier * (baseAcceleration / baseMaxSpeed));
        this.setDeceleration(baseDeceleration + speedMultiplier * (baseDeceleration / baseMaxSpeed));

        // Debug text
        System.out.println("Player spawned with max health: " + totalHealth);
        System.out.println("Player spawned with max speed: " + getMaxSpeed());
        System.out.println("Player spawned with acceleration: " + getAcceleration());
        System.out.println("Player spawned with deceleration: " + getDeceleration());

        // Load default turret
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

    public void takeDamage(int damage) {
        int newHealth = Math.max(0, this.getHealth() - damage);
        this.setHealth(newHealth);
        System.err.println("Player took " + damage + " damage. Remaining HP: " + this.getHealth());
    }
}
