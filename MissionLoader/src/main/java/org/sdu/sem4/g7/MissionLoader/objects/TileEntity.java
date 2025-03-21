package org.sdu.sem4.g7.MissionLoader.objects;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.ICollidableService;

import javafx.scene.canvas.GraphicsContext;

public class TileEntity extends Entity implements ICollidableService {

    @Override
    public Hitbox getHitbox() {
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        return new Hitbox(new Vector2(getPosition()), size, getRotation());
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);

        // Draw hitbox
        if (isCollision()) {
            getHitbox().render(gc);
        }
    }
}
