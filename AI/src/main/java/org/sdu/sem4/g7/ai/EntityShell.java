package org.sdu.sem4.g7.ai;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Vector2;

public class EntityShell {
    
    private final Vector2 location;
    private final float rotation;

    public EntityShell(Entity entity) {
        this.location = new Vector2(entity.getPosition());
        this.rotation = entity.getRotation();
    }

    public Vector2 getPosition() {
        return location;
    }
    public void setPosition(Vector2 position) {
        this.location.set(position);
    }
    public float getRotation() {
        return rotation;
    }

}
