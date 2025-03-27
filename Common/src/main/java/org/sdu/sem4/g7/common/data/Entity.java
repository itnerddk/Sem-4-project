package org.sdu.sem4.g7.common.data;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Base object for all visible objects in the game
 */
public class Entity implements Serializable {

    /**
     * Unique id for the entity
     */
    private final UUID ID = UUID.randomUUID();
    
    /**
     * Position of the entity
     */
    private Vector2 position = new Vector2(0, 0);

    /**
     * Z-index of the entity
     */
    private int zIndex = 0;

    /**
     * Rotation of the entity
     *
     * Value between 0 and 359
     */ 
    private float rotation = 0;

    /**
     * direction of travel (with velocity)
     */ 
    private Vector2 velocity = new Vector2(0, 0);

    /**
     * Image of the sprite
     */
    private ImageView sprite;

    /**
     * Health of the entity
     *
     * NOTE: An entity should die, whenever the health is <= 0
     */
    private int health;

    /**
     * Limit for health, to ensure that the player cannot stack health
     */
    private int maxHealth;

    /**
     * Defines if the entity is able to move
     */
    private boolean immoveable;

    /**
     * Defines if the entity should have an hit box
     */
    private boolean collision;

    /**
     * Weight of the entity, used when collision occurs
     */
    private int weight;
            

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

    public int getzIndex() {
        return zIndex;
    }
    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
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

    public void setSprite(URI uri, int size) {
        Image original = new Image(uri.toString());
        Image scaled = new Image(uri.toString(), original.getWidth() * size, original.getHeight() * size, false, false);

        this.sprite = new ImageView(scaled);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    /*
     * Checks if the entity is dead
     * 
     * @returns true if dead
     */
    public boolean isDead() {
        return health <= 0;
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

    public boolean isCollision() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Entity> getChildren() {
        return new ArrayList<Entity>();
    }

    public void subProcess() {
        return;
    }

    public void render(GraphicsContext gc) {
        sprite.setTranslateX(this.getPosition().getX() - (sprite.getImage().getWidth() / 2));
        sprite.setTranslateY(this.getPosition().getY() - (sprite.getImage().getHeight() / 2));
        sprite.setRotate(this.getRotation());
    }
}
