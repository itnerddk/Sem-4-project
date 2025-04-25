package org.sdu.sem4.g7.MissionLoader.objects;

import java.io.File;

import org.sdu.sem4.g7.MissionLoader.config.Config;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IRigidbodyService;

import javafx.scene.canvas.GraphicsContext;

public class TileEntity extends Entity implements IRigidbodyService {

    private Hitbox hitbox;
    private boolean invulnerable;
    private int replacedByOnDeath;

    public TileEntity() {
        super();
        setImmoveable(true);
    }

    public TileEntity(TileObject tileObject) {
        super();
        setImmoveable(true);
        setSprite(new File(Config.tilesDir, tileObject.getImage()).toURI(), Config.tileSize);
        setzIndex(tileObject.getZ());
        setCollision(tileObject.isCollision());
        setHealth(tileObject.getHealth());
        setInvulnerable(tileObject.isInvulnerable());
        replacedByOnDeath = tileObject.getReplacedByOnDeath();
    }

    @Override
    public Hitbox getHitbox() {
        if (this.hitbox != null) {
            return this.hitbox;
        }
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        this.hitbox = new Hitbox(new Vector2(getPosition()), size, getRotation());
        return this.hitbox;
    }

    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public void setHealth(int health) {
        if (invulnerable) {
            return;
        }
        super.setHealth(health);
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
    public boolean onCollision(IRigidbodyService other, GameData gameData) {
        return false;
    }
    @Override
    public boolean isDead() {
        return false;
    }

    public int getReplacedByOnDeath() {
        return replacedByOnDeath;
    }
}
