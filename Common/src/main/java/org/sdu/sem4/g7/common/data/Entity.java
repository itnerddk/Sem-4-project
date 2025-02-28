package org.sdu.sem4.g7.common.data;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.UUID;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Entity implements Serializable {

    private final UUID ID = UUID.randomUUID();
    
    private Vector2 position = new Vector2(0, 0);
    private float rotation = 0;
    private Vector2 velocity = new Vector2(0, 0);
    private ImageView sprite;
    private int health;
    private int maxHealth;
    private boolean immoveable;
    private boolean collission;
    private int weight;

    
    private float radius;
            

    public String getID() {
        return ID.toString();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(double x, double y) {
        this.position = new Vector2(x, y);
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(double x, double y) {
        this.velocity = new Vector2(x, y);
    }
    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public void addVelocity(double x, double y) {
        this.velocity.add(new Vector2(x, y));
    }
    public void addVelocity(Vector2 velocity) {
        this.velocity.add(velocity);
    }

    public ImageView getSprite() {
        return sprite;
    }

    public void setSprite(URI uri) {
        Image original = new Image(uri.toString());
        Image scaled = new Image(uri.toString(), original.getWidth() * 8, original.getHeight() * 8, false, false);

        this.sprite = new ImageView(scaled);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public boolean isImmoveable() {
        return immoveable;
    }

    public void setImmoveable(boolean immoveable) {
        this.immoveable = immoveable;
    }

    public boolean isCollission() {
        return collission;
    }

    public void setCollission(boolean collission) {
        this.collission = collission;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
