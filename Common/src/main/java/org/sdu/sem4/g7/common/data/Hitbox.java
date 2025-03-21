package org.sdu.sem4.g7.common.data;

import javafx.geometry.Bounds;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Hitbox {
    Vector2 position;
    Vector2 size;
    double rotation;

    public Hitbox(Vector2 position, Vector2 size, double rotation) {
        this.position = position;
        this.size = size;
        this.rotation = rotation;
    }

    public void render(GraphicsContext gc) {
        gc.save();
        gc.translate(position.getX(), position.getY());
        gc.rotate(rotation);
        Rectangle rect = createRotatedRectangle();
        gc.strokeRect(-size.getX() / 2, -size.getY() / 2, size.getX(), size.getY());
        gc.setStroke(javafx.scene.paint.Color.BLUE);
        gc.strokeRect(-rect.getWidth() / 2, -rect.getHeight() / 2, rect.getWidth(), rect.getHeight());
        gc.restore();

        // Draw radius
        // gc.save();
        // gc.setStroke(javafx.scene.paint.Color.RED);
        // gc.translate(position.getX(), position.getY());
        // gc.strokeOval(-radius(), -radius(), radius() * 2, radius() * 2);
        // gc.restore();
    }

    public Vector2 collides(Hitbox other) {

        if (position.distance(other.position) < radius() + other.radius()) {
            return checkCollision(other);
        }
        return null;
    }

    public double radius() {
        return Math.max(size.getX(), size.getY());
    }

    public Vector2 checkCollision(Hitbox other) {
        Rectangle rect1 = this.createRotatedRectangle();
        Rectangle rect2 = other.createRotatedRectangle();

        // Use JavaFX's Shape.intersect() to check if they overlap
        Shape intersection = Shape.intersect(rect1, rect2);
        Bounds bounds = intersection.getBoundsInLocal();
        if (!bounds.isEmpty()) {
            return new Vector2(bounds.getWidth(), bounds.getHeight());
        }
        return null;
    }

    private Rectangle createRotatedRectangle() {
        double w = size.getX();
        double h = size.getY();

        Rectangle rect = new Rectangle(position.getX() - w/2, position.getY() - h/2, w, h);
        rect.setRotate(rotation); // Apply rotation
        rect.setFill(Color.TRANSPARENT); // Invisible, just for collision
        return rect;
    }
}
