package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Turret;

import javafx.scene.canvas.GraphicsContext;

public class CannonTurret extends Turret {
    public CannonTurret() {
        super();
        setOffset(new Vector2(0, 0));
        setMuzzle(new Vector2(0, -25));
        try {
            System.out.println(this.getClass().getClassLoader().getResource("CannonTurret.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("CannonTurret.png").toURI(), 5);
            this.setzIndex(-10);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void shoot(GameData gameData, WorldData mission) {
        CannonBullet bullet = new CannonBullet();

        // Set WeaponDamage for the specific weapon
        bullet.setWeaponBonus(10);
        bullet.finalizeDamage();

        bullet.setPosition(this.getPosition());
        // bullet.getPosition().add(this.getOffset());
        Vector2 rotatedMuzzle = new Vector2(this.getMuzzle().getX(), this.getMuzzle().getY());
        rotatedMuzzle.rotate(this.getRotation());
        bullet.getPosition().add(rotatedMuzzle);
        
        bullet.setRotation(this.getRotation());

        // set the tank as the createdBy
        bullet.setCreatedBy(this.getTank());
        
        float rotationInRadians = bullet.getRotation() - 90;
        rotationInRadians = (float) Math.toRadians(rotationInRadians);


        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        mission.addEntity(bullet);
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
        gc.save();
        gc.translate(this.getPosition().getX(), this.getPosition().getY());
        gc.rotate(this.getRotation());
        gc.strokeOval(this.getMuzzle().getX(), this.getMuzzle().getY(), 5, 5);
        gc.restore();
    }
}
