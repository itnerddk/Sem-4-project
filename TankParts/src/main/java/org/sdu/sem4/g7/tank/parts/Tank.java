package org.sdu.sem4.g7.tank.parts;

import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IRigidbodyService;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public abstract class Tank extends Entity implements IRigidbodyService {
    /**
     * The current forwards, backwards velocity of the tank
     */
    private double speed;
    private double maxSpeed = 0.5;
    private double acceleration = 1.2;
    private double deceleration = .8;
    private double rotationSpeed = 3;

    private Turret turret;

    public Tank() {
        super();
        this.setCollision(true);
        try {
            System.out.println(this.getClass().getClassLoader().getResource("Tank.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("Tank.png").toURI(), CommonConfig.DEFAULT_SCALING);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public void processPosition(GameData gameData) {
        setSpeed(lerp(getSpeed(), 0, 0.1));
        getVelocity().lerp(0, 0, 0.1);

        double changeY = Math.sin(Math.toRadians(getRotation() - 90));
        double changeX = Math.cos(Math.toRadians(getRotation() - 90));

        
        // Set position
        // setPosition(getPosition().getX() + (changeX * getSpeed()), getPosition().getY() + (changeY * getSpeed()));
        // Set velocity
        getVelocity().add(changeX * getSpeed(), changeY * getSpeed());
        // Apply velocity
        getPosition().add(getVelocity());

        processTurret();
    }

    private void processTurret() {
        if (turret != null) {
            turret.setPosition(getPosition().getX(), getPosition().getY());
        }
    }

    public void accelerate(float weight) {
        this.setSpeed(
            lerp(
                this.getSpeed(),
                Math.min(this.getSpeed() + (this.getAcceleration() * weight), this.getMaxSpeed()),
                0.7
            )
        );
    }
    
    public void accelerate() {
        this.accelerate(1.0f);
    }

    public void decelerate(float weight) {
        this.setSpeed(
            lerp(
                this.getSpeed(),
                Math.max(this.getSpeed() - (this.getDeceleration() * weight), -(this.getMaxSpeed()/2)),
                0.5
            )
        );
    }

    public void decelerate() {
        this.decelerate(1.0f);
    }

    public void turnLeft(float weight) {
        float newRotation = (float)(this.getRotation() - this.getRotationSpeed() * weight);
        if (newRotation < 0) {
            newRotation = 360 + newRotation;
        }
        if (turret != null) {
            turret.setRotation((float)(turret.getRotation() - this.getRotationSpeed() * weight));
        }

        this.setRotation(newRotation);
        // Turn velocity
        getVelocity().rotate(-this.getRotationSpeed() * weight);
    }

    public void turnLeft() {
        this.turnLeft(1.0f);
    }

    public void turnRight(float weight) {
        float newRotation = (float)(this.getRotation() + this.getRotationSpeed() * weight);
        if (newRotation > 360) {
            newRotation = 0 + newRotation - 360;
        }
        if (turret != null) {
            turret.setRotation((float)(turret.getRotation() + this.getRotationSpeed() * weight));
        }

        this.setRotation(newRotation);
        // Turn velocity
        getVelocity().rotate(this.getRotationSpeed() * weight);
    }

    public void turnRight() {
        this.turnRight(1.0f);
    }

    public void shoot(GameData gameData, WorldData mission) {
        if (turret != null) {
            turret.shoot(gameData, mission);
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public double getDeceleration() {
        return deceleration;
    }

    public void setDeceleration(double deceleration) {
        this.deceleration = deceleration;
    }

    public double getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public Turret getTurret() {
        return turret;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
        turret.setTank(this);
    }

    public double lerp(double a, double b, double f) {
        return a * (1.0 - f) + (b * f);
    }

    private Hitbox hitbox;

    public Hitbox getHitbox() {
        if (this.hitbox != null) {
            this.hitbox.setPosition(new Vector2(getPosition()));
            this.hitbox.setRotation(getRotation());
            return this.hitbox;
        }
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        return new Hitbox(new Vector2(getPosition()), size, getRotation());
    }

    @Override
    public boolean onCollision(IRigidbodyService other, GameData gameData) {
        return false;
    }


    @Override
    public List<Entity> getChildren() {
        List<Entity> children = new ArrayList<>();
        if (getTurret() != null) {
            children.add(getTurret());
        }
        return children;
    }

    @Override
    public void subProcess() {
        processTurret();
    }

    public void drawHealthBar(GraphicsContext gc) {
        // Width of the health bar
        double barWidth = 50; 
        // Height of the health bar
        double barHeight = 5; 
        double healthPercentage = (double) getHealth() / getMaxHealth();

        // Calculate the position of the health bar
        double x = getPosition().getX() - barWidth / 2;
        double y = getPosition().getY() - getSprite().getImage().getHeight() / 2 - 15; // Position above the tank

        // Draw the background of the health bar
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, barWidth, barHeight);

        // Draw the health of the health bar
        gc.setFill(Color.RED);
        gc.fillRect(x, y, barWidth * healthPercentage, barHeight);

        // Draw the border
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, barWidth, barHeight);
    }
    
    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        drawHealthBar(gc);

        // this.getHitbox().render(gc);
    }

}
