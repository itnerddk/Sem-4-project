package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;

public interface IRigidbodyService {    
    public Hitbox getHitbox();
    /**
     * Called when this entity is colliding with another entity
     * @param other The other entity that this entity is colliding with
     * @return false if the collision should be ignored
     */
    public boolean onCollision(IRigidbodyService other, GameData gameData);
}
