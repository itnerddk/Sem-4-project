package org.sdu.sem4.g7.playersystem;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Player extends Tank {
    public Player() {
        super();

        this.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0.45, 0, 0, 0));
        this.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
        this.setzIndex(-5);
        setEntityType(EntityType.PLAYER);

        // Shield
        int baseShield = 0;
        int bonusShield = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getShieldBonus)
                .orElse(0);
        int totalShield = baseShield + bonusShield;
        this.setShield(totalShield);

        // Armor
        int baseArmor = 0;
        int bonusArmor = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getArmorBonus)
                .orElse(0);
        int totalArmor = baseArmor + bonusArmor;
        this.setArmor(totalArmor);

        // Health
        int baseHealth = 1000;
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

        // Debug text for upgrades
        System.out.println("Player spawned with health: " + totalHealth);
        System.out.println("Player spawned with speed: " + getMaxSpeed());
        System.out.println("Player spawned with acceleration: " + getAcceleration());
        System.out.println("Player spawned with deceleration: " + getDeceleration());
        System.out.println("Player spawned with shield: " + totalShield);
        System.out.println("Player spawned with armor: " + getArmor());
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

    // Override Draw Health for Player to draw shield
    @Override
    public void drawHealthBar(GraphicsContext gc) {
        double barWidth = 50;
        double barHeight = 5;

        int hp = this.getHealth();
        int shield = this.getShield();
        int total = hp + shield;

        // Fill ratio
        double fillRatio = (double) total / getMaxHealth();
        double filledWidth = Math.min(fillRatio, 1.0) * barWidth;

        double hpRatio = total > 0 ? (double) hp / total : 0;
        double shieldRatio = total > 0 ? (double) shield / total : 0;

        // Placement
        double x = getPosition().getX() - barWidth / 2;
        double y = getPosition().getY() - getSprite().getImage().getHeight() / 2 - 15;

        // Background
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, barWidth, barHeight);

        // Health (red)
        gc.setFill(Color.RED);
        gc.fillRect(x, y, filledWidth * hpRatio, barHeight);

        // Shield (blue)
        gc.setFill(Color.YELLOW);
        gc.fillRect(x + filledWidth * hpRatio, y, filledWidth * shieldRatio, barHeight);

        // Edge
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, barWidth, barHeight);
    }


}
