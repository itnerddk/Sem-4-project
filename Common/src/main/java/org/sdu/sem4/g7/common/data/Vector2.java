package org.sdu.sem4.g7.common.data;

/**
 * A class to represent a 2D vector with integer values
 */
public class Vector2 {
    private double x;
    private double y;

    /**
     * 
     * Default constructor
     * Sets x and y to 0
     */
    public Vector2() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Complete constructor
     * @param x the length on the x axis
     * @param y the length on the y axis
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Set the x and y values
     * @param x
     * @param y
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Copy constructor
     * @param vector the vector to copy
     */
    public Vector2(Vector2 vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    /**
     * Set the x and y values based on other vector
     * @param vector
     */
    public void set(Vector2 vector) {
        this.x = vector.getX();
        this.y = vector.getY();
    }

    /**
     * Add the x and y values of another vector to this vector
     * @param vector vector to add
     */
    public void add(Vector2 vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }

    /**
     * Subtract the x and y values of another vector from this vector
     * @param vector vector to subtract
     */
    public void subtract(Vector2 vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
    }

    /**
     * Multiply the x and y values of this vector with another vector
     * @param vector vector to multiply with
     */
    public void multiply(Vector2 vector) {
        this.x *= vector.getX();
        this.y *= vector.getY();
    }

    /**
     * Divide the x and y values of this vector with another vector
     * @param vector vector to divide with
     */
    public void divide(Vector2 vector) {
        this.x /= vector.getX();
        this.y /= vector.getY();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
