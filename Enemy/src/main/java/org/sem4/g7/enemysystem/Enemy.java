package org.sem4.g7.enemysystem;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.EntityActions;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.ITurretProviderService;
import org.sdu.sem4.g7.tank.parts.Tank;
import org.sdu.sem4.g7.tank.parts.Turret;

import javafx.scene.canvas.GraphicsContext;

public class Enemy extends Tank {

    private double lastShotTime = 0.0; // Initialize last shot time to 0

    private ArrayList<Vector2> path = new ArrayList<>();
    private EntityActions currentAction = EntityActions.IDLE;

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

    public void setPath(ArrayList<Vector2> path) {
        this.path = path;
    }
    public ArrayList<Vector2> getPath() {
        return path;
    }


    @Override
    public void processPosition(GameData gameData) {
        super.processPosition(gameData);
        if (this.path == null || this.path.isEmpty()) {
            this.currentAction = EntityActions.IDLE;
            return;
        }
        if (new Vector2(this.getPosition()).distance(new Vector2(this.path.getFirst()).multiply(CommonConfig.getTileSize())) < (1 * CommonConfig.getTileSize())) {
            System.out.println("Removing point");
            this.path.remove(0);
            if (this.path.isEmpty()) this.currentAction = EntityActions.IDLE;
            
            // This is where we move
            
        } else {
            this.currentAction = EntityActions.MOVING;

            System.out.println(Vector2.round(this.getPosition()) + " -> " + new Vector2(this.path.getFirst()).multiply(CommonConfig.getTileSize()));

            double targetRotation = Math.round(new Vector2(this.path.getFirst()).subtract(new Vector2(this.getPosition()).divideInt(CommonConfig.getTileSize())).rotation());
            System.out.println(Math.round(this.getRotation()) + " -> " + targetRotation);
            // turnLeft and turnRight within 5 degrees
            double angleDiff = Math.round(this.getRotation()) - targetRotation;
            if (angleDiff > 180) {
                angleDiff -= 2 * 180;
            } else if (angleDiff < -180) {
                angleDiff += 2 * 180;
            }

            if (angleDiff > 5) {
                this.turnLeft();
            } else if (angleDiff < -5) {
                this.turnRight();
            }

            // Move forward
            if (Math.abs(Math.round(this.getRotation()) - targetRotation) < 10) {
                this.accelerate();
            }
        }

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
