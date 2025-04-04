package org.sdu.sem4.g7.tank.parts;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IRigidbodyService;
import org.sdu.sem4.g7.common.enums.EntityType;

/*
 * Shootable and renderable bullet
 */
public abstract class Bullet extends Entity implements IRigidbodyService {
    
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


    private Hitbox hitbox;

    public Hitbox getHitbox() {
        if (this.hitbox != null) {
            this.hitbox.setPosition(new Vector2(getPosition()));
            return this.hitbox;
        }
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        return new Hitbox(new Vector2(getPosition()), size, getRotation());
    }

    @Override
    public boolean onCollision(IRigidbodyService other, GameData gameData) {
        // If the bullet hits another bullet
        if (other instanceof Bullet) {
            // Return true (This makes the current collision ignore it)
            return true;
        } else {
            // If the bullet hits an entity it means we know the behavior and can cast it
            if (other instanceof Entity) {
                // If the entity that created the bullet is the same as the entity that the bullet hit
                if (this.createdBy != null && this.createdBy.equals(other)) {
                    // Return true (This makes the current collision ignore it)
                    return true;
                }
                // Cast the other entity to an entity
                Entity otherEntity = (Entity) other;
                // Reduce the health of the entity by the damage of the bullet
                otherEntity.setHealth(otherEntity.getHealth() - this.damage);
                // Set the health of the bullet to 0 to make it disapear next tick
                this.setHealth(0);
                gameData.playAudio(SoundType.HIT);
                // Return true signifying that the collision was handled
                return true;
            }
        }
        // Return false if the collision was not handled meaning it runs default collision handling
        return false;
    }

}