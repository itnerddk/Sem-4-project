package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurret extends Turret {
    public CannonTurret() {
        super();
        setOffset(new Vector2(60, 60));
        try {
            System.out.println(this.getClass().getClassLoader().getResource("CannonTurret.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("CannonTurret.png").toURI());
            this.setzIndex(-10);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void shoot(GameData gameData, WorldData mission) {
        CannonBullet bullet = new CannonBullet();
        bullet.setPosition(this.getPosition());
        bullet.getPosition().add(this.getOffset());
        bullet.setRotation(this.getRotation());
        
        float rotationInRadians = bullet.getRotation() - 90;
        rotationInRadians = (float) Math.toRadians(rotationInRadians);

        bullet.setPosition(bullet.getPosition().getX() + (float) Math.cos(rotationInRadians) * 40,
                bullet.getPosition().getY() + (float) Math.sin(rotationInRadians) * 40);

        bullet.getPosition().add(new Vector2(-4, -4));

        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        mission.addEntity(bullet);
    }
}
