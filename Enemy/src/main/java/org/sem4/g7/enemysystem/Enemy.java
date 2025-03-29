package org.sem4.g7.enemysystem;

import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.tank.TurretLoader;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Enemy extends Tank {

    private double lastShotTime = 0.0; // Initialize last shot time to 0

    public Enemy() {
        super();
        this.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0, 0, 0, 0));
        this.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
        this.setzIndex(-3);

        // Set entity type
        setEntityType(EntityType.ENEMY);

        // Test code to load turret
        this.setTurret(TurretLoader.getTurrets().get(0).get());
    }

    @Override
    public void setTurret(Turret turret) {
        super.setTurret(turret);
        turret.getSprite().setEffect(new javafx.scene.effect.ColorAdjust(0.0, 0, 0, 0));
        turret.getSprite().setCacheHint(javafx.scene.CacheHint.SPEED);
    }

    // Getters and setters for last shot time
    public double getLastShotTime() {
        return lastShotTime;
    }

    public void setLastShotTime(double lastShotTime) {
        this.lastShotTime = lastShotTime;
    }
}
