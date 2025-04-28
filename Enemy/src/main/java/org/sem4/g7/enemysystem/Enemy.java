package org.sem4.g7.enemysystem;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.ITurretProviderService;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

import javafx.scene.canvas.GraphicsContext;

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
        ServiceLoader<ITurretProviderService> turretLoader = ServiceLoader.load(ITurretProviderService.class);
        for (ITurretProviderService turretProvider : turretLoader) {
            for (Provider<? extends Entity> turret : turretProvider.getTurrets()) {
                this.setTurret((Turret)turret.get());
                break; // Only set the first turret found
            }
        }
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

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);

        // Draw rotation
        gc.save();

        gc.translate(this.getPosition().getX(), this.getPosition().getY());

        gc.fillText(Double.toString(Math.round(this.getRotation()*10)/10.0), 0, 50);

        gc.fillText(Vector2.round(new Vector2(getPosition()).multiply(10)).divide(10).toString(), 0, 72);

        gc.restore();
    }
}
