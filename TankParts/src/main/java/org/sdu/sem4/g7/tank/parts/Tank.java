package org.sdu.sem4.g7.tank.parts;

import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.ICollidableService;

import javafx.scene.shape.Shape;

public abstract class Tank extends Entity implements ICollidableService {
    /**
     * The current forwards, backwards velocity of the tank
     */
    private double speed;
    private double maxSpeed = 7.5;
    private double acceleration = 1;
    private double deceleration = .5;
    private float rotationSpeed = 2;

    private Turret turret;

    private Shape shape;

    public void processPosition(GameData gameData) {
        setSpeed(lerp(getSpeed(), 0, 0.1));

        double changeY = Math.sin(Math.toRadians(getRotation() - 90));
        double changeX = Math.cos(Math.toRadians(getRotation() - 90));

        
        // Set position
        setPosition(getPosition().getX() + (changeX * getSpeed()), getPosition().getY() + (changeY * getSpeed()));
        if (turret != null) {
            turret.setPosition(getPosition().getX(), getPosition().getY());
            turret.setRotation(getRotation());
        }
    }

    public void shoot(GameData gameData, Mission mission) {
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

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
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

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
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
