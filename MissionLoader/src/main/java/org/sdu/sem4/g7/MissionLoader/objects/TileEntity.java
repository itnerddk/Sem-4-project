package org.sdu.sem4.g7.MissionLoader.objects;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IRigidbodyService;

import javafx.scene.canvas.GraphicsContext;

public class TileEntity extends Entity implements IRigidbodyService {

    private Hitbox hitbox;

    @Override
    public Hitbox getHitbox() {
        if (this.hitbox != null) {
            return this.hitbox;
        }
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        this.hitbox = new Hitbox(new Vector2(getPosition()), size, getRotation());
        return this.hitbox;
    }

    boolean superRendered = false;

    @Override
    public void render(GraphicsContext gc) {
        if (!superRendered) {
            super.render(gc);
            superRendered = true;
        }
        // super.render(gc);

        // Draw hitbox
        // if (isCollision()) {
        //     getHitbox().render(gc);
        // }
    }

    @Override
    public boolean onCollision(IRigidbodyService other) {
        return false;
    }
}
