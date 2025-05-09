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
    private Vector2 target = null;
    private EntityActions currentAction = EntityActions.IDLE;

    /**
     * This action is only what it's doing at the moment, doesn't have any effect on
     * anything as of now.
     */
    public EntityActions logicalAction;

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
            this.setTurret((Turret)turretProvider.getTurrets().get((int) (Math.random() * turretProvider.getTurrets().size())).get());
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

    public void setTarget(Vector2 target) {
        this.target = target;
    }

    public Vector2 getTarget() {
        return target;
    }

    public void setCurrentAction(EntityActions currentAction) {
        this.currentAction = currentAction;
    }

    public EntityActions getCurrentAction() {
        return currentAction;
    }

    @Override
    public void processPosition(GameData gameData) {
        // Point radius is for checking if close enough to the point
        float pointRadius = 0.5f;
        super.processPosition(gameData);
        if (this.path == null || this.path.isEmpty()) {
            this.currentAction = EntityActions.IDLE;
            return;
        }
        if (new Vector2(this.getPosition()).distance(new Vector2(this.path.get(0))
                .multiply(CommonConfig.getTileSize())) < (pointRadius * CommonConfig.getTileSize())) {
            // System.out.println("Removing point");
            this.path.remove(0);
            if (this.path.isEmpty())
                this.currentAction = EntityActions.IDLE;

            // This is where we move

        } else {
            this.currentAction = EntityActions.MOVING;

            // System.out.println(Vector2.round(this.getPosition()) + " -> " + new
            // Vector2(this.path.get(0)).multiply(CommonConfig.getTileSize()));

            double targetRotation = Math.round(new Vector2(this.path.get(0))
                    .subtract(new Vector2(this.getPosition()).divide(CommonConfig.getTileSize())).rotation());
            // System.out.println(Math.round(this.getRotation()) + " -> " + targetRotation);
            // turnLeft and turnRight within 5 degrees
            double angleDiff = Math.round(this.getRotation()) - targetRotation;
            if (angleDiff > 180.0) {
                angleDiff -= 2.0 * 180.0;
            } else if (angleDiff < -180.0) {
                angleDiff += 2.0 * 180.0;
            }

            // Above 5 degrees, turn with 1.0f weight
            // Below 5 degrees, turn with variable weight
            float turnWeight = 1.0f;
            if (Math.abs(angleDiff) < 25) {
                turnWeight = (float) Math.abs((float) angleDiff / 12.5f);
            }

            if (angleDiff > 0) {
                this.turnLeft(turnWeight);
            } else if (angleDiff < 0) {
                this.turnRight(turnWeight);
            }

            // Move forward
            if (path.size() > 1) {
                // Based on angle accelerate more but if the angle is too big, don't accelerate
                // If the angle is less than 10 degrees accelerate 1.0f
                float acceleration = 1.0f;
                if (Math.abs(angleDiff) > 10) {
                    acceleration = (float) Math.abs((float) angleDiff / 45.0f);
                }
                if (Math.abs(angleDiff) > 45) {
                    acceleration = 0.0f;
                }
                this.accelerate(acceleration);
                // System.out.println("Accelerating: " + acceleration);
            } else if (Math.abs(angleDiff) < 10) {
                double distance = new Vector2(this.path.get(0))
                        .subtract(new Vector2(this.getPosition()).divide(CommonConfig.getTileSize())).length();
                // Emergency brake if dangerously close and still moving
                if (distance < 1 && this.getSpeed() > 0.1f) {
                    this.decelerate(1.0f); // SLAM THE BRAKES
                    return; // stop here, don't apply other movement
                }

                // Based on distance to the next point accelerate less
                // If the distance is less than 1 tile, accelerate less
                float v = (float) this.getSpeed() * CommonConfig.getTileSize(); // speed in pixels/s
                float maxDecel = (float) this.getDeceleration() * CommonConfig.getTileSize(); // decel in pixels/s^2
                float d = (float) distance * CommonConfig.getTileSize(); // distance in pixels

                // Estimate brake distance (no /2 since your `getDeceleration()` might already
                // account for it)
                float brakeDist = (v * v) / (2 * maxDecel);

                
                
                if (d <= brakeDist + 0.1f) { // +0.1f as buffer
                    // Compute how deep into the braking zone we are
                    float t = 1.0f - (d / brakeDist);
                    float weight = (float) Math.pow(Math.max(0, Math.min(1, t)), 2);

                    // Only brake if we're still moving forward
                    if (this.getSpeed() > 0.01f) {
                        this.decelerate(weight);
                    } else {
                        // Optional: apply brakes *lightly* to prevent jitter/reverse
                        this.decelerate(0.0f);
                    }
                } else {
                    // Accelerate if we're not turning sharply
                    this.accelerate(1.0f);
                }

            }
        }

    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);

        if (CommonConfig.isDEBUG()) {
            this.getHitbox().render(gc);
            // Draw rotation
            gc.save();

            gc.translate(this.getPosition().getX(), this.getPosition().getY());

            gc.fillText(Double.toString(Math.round(this.getRotation() * 10) / 10.0), 0, 50);

            gc.fillText(Vector2.round(new Vector2(getPosition()).multiply(10)).divide(10).toString(), 0, 72);

            // Format to fill 8 characters
            if (this.getCurrentAction() != null && this.logicalAction != null) {
                gc.fillText(String.format("%s\n->%s", this.getCurrentAction().toString(), this.logicalAction.toString()), 0,
                        100);
            }

            gc.restore();

            // Draw target
            if (this.target != null) {
                gc.save();
                gc.setFill(javafx.scene.paint.Color.RED);
                gc.fillOval(this.target.getX(), this.target.getY(), 10, 10);
                gc.restore();
            }
        }
    }
}
