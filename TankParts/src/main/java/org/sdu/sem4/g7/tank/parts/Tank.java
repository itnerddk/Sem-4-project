package org.sdu.sem4.g7.tank.parts;

import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.ICollidableService;

import javafx.geometry.Bounds;

public abstract class Tank extends Entity implements ICollidableService {
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

        if (turret != null) {
            turret.setPosition(getPosition().getX(), getPosition().getY());
            turret.setRotation(getRotation());
        }
    }

    public void accelerate() {
        this.setSpeed(
            lerp(
                this.getSpeed(),
                Math.min(this.getSpeed() + (this.getAcceleration()), this.getMaxSpeed()),
                0.7
            )
        );
    }

    public void decelerate() {
        this.setSpeed(
            lerp(
                this.getSpeed(),
                Math.max(this.getSpeed() - (this.getDeceleration()), -(this.getMaxSpeed()/2)),
                0.5
            )
        );
    }

    public void turnLeft() {
        this.setRotation((float)(this.getRotation() - this.getRotationSpeed()));
        // Turn velocity
        getVelocity().rotate(-this.getRotationSpeed());
    }

    public void turnRight() {
        this.setRotation((float)(this.getRotation() + this.getRotationSpeed()));
        // Turn velocity
        getVelocity().rotate(this.getRotationSpeed());
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

    public Bounds getBounds() {
        return getSprite().getBoundsInParent();
    }


    @Override
    public List<Entity> getChildren() {
        List<Entity> children = new ArrayList<>();
        if (getTurret() != null) {
            children.add(getTurret());
        }
        return children;
    }
}
