package org.sdu.sem4.g7.common.data;

public class Tank extends Entity {
    private double speed;
    private double maxSpeed = 5;
    private double acceleration = 0.3;
    private double deacceleration = 0.1;
    private float rotationSpeed = 2;

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

    public double getDeacceleration() {
        return deacceleration;
    }

    public void setDeacceleration(double deacceleration) {
        this.deacceleration = deacceleration;
    }

    public float getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
}
