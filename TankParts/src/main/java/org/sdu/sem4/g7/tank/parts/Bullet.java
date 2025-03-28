package org.sdu.sem4.g7.tank.parts;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.enums.EntityType;

/*
 * Shootable and renderable bullet
 */
public abstract class Bullet extends Entity {
    
    /*
     * Defines how many health an entity loses upon collision
     */
    private int damage;

    /*
     * Defines the entity that have created the entity
     * 
     * Usefull, to ensure the bullet does not collide with the entity that have "created" it
     */
    private Entity createdBy;

    public Bullet() {
        super();

        // Set entity type
        setEntityType(EntityType.BULLET);
    }

    public Bullet(int damage) {
        super();
        this.damage = damage;

        // Set entity type
        setEntityType(EntityType.BULLET);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Entity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Entity createdBy) {
        this.createdBy = createdBy;
    }

}