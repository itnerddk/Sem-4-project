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


    public Vector2 add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }
    /**
     * Add the x and y values of another vector to this vector
     * @param vector vector to add
     */
    public Vector2 add(Vector2 vector) {
        this.x += vector.getX();
        this.y += vector.getY();
        return this;
    }

    public Vector2 subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Subtract the x and y values of another vector from this vector
     * @param vector vector to subtract
     */
    public Vector2 subtract(Vector2 vector) {
        this.x -= vector.getX();
        this.y -= vector.getY();
        return this;
    }

    /**
     * Multiply the x and y values of this vector with another vector
     * @param vector vector to multiply with
     */
    public Vector2 multiply(Vector2 vector) {
        this.x *= vector.getX();
        this.y *= vector.getY();
        return this;
    }

    /**
     * Multiply the x and y values of this vector with a scalar
     * @param scalar scalar to multiply with
     */
    public Vector2 multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    /**
     * Divide the x and y values of this vector with another vector
     * @param vector vector to divide with
     */
    public Vector2 divide(Vector2 vector) {
        this.x /= vector.getX();
        this.y /= vector.getY();
        return this;
    }

    /**
     * Divide the x and y values of this vector with a scalar
     * @param scalar scalar to divide with
     */
    public Vector2 divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    public Vector2 divideInt(double scalar) {
        this.divide(scalar);
        Vector2 newThis = Vector2.round(this);
        this.x = newThis.getX();
        this.y = newThis.getY();
        return this;
    }

    public Vector2 normalize() {
        double length = Math.sqrt(x * x + y * y);
        if (length != 0) {
            x /= length;
            y /= length;
        }
        return this;
    }

    public double dot(Vector2 vector) {
        return x * vector.getX() + y * vector.getY();
    }

    public double distance(Vector2 vector) {
        double dx = x - vector.getX();
        double dy = y - vector.getY();
        return Math.sqrt(dx * dx + dy * dy);
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

    public Vector2 lerp(Vector2 vector, double alpha) {
        this.x = (1 - alpha) * this.x + alpha * vector.getX();
        this.y = (1 - alpha) * this.y + alpha * vector.getY();
        return this;
    }

    public Vector2 lerp(double x, double y, double alpha) {
        this.x = (1 - alpha) * this.x + alpha * x;
        this.y = (1 - alpha) * this.y + alpha * y;
        return this;
    }

    public Vector2 rotate(double degrees) {
        double angle = Math.toRadians(degrees);
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = this.x * cos - this.y * sin;
        double y = this.x * sin + this.y * cos;
        this.x = x;
        this.y = y;
        return this;
    }

    public double rotation() {
        double angle = Math.atan2(x, -y);
        if (angle < 0) {
            angle += 2 * Math.PI;
        }
        return Math.toDegrees(angle);
    }

    public static Vector2 round(Vector2 vector) {
        return new Vector2(Math.round(vector.getX()), Math.round(vector.getY()));
    }



    @Override
    public String toString() {
        return "Vector2 {x=" + x + ", y=" + y + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vector2 vector2 = (Vector2) obj;
        return Double.compare(vector2.x, x) == 0 && Double.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return 31 * Double.hashCode(x) + Double.hashCode(y);
    }
}
