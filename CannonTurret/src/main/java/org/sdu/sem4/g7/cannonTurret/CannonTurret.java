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
        bullet.setPosition(this.getPosition());
        // bullet.getPosition().add(this.getOffset());
        bullet.setRotation(this.getRotation());

        // set the tank as the createdBy
        bullet.setCreatedBy(this.getTank());
        
        float rotationInRadians = bullet.getRotation() - 90;
        rotationInRadians = (float) Math.toRadians(rotationInRadians);

        // bullet.setPosition(bullet.getPosition().getX() + (float) Math.cos(rotationInRadians) * 40,
        //         bullet.getPosition().getY() + (float) Math.sin(rotationInRadians) * 40);

        // bullet.getPosition().add(new Vector2(-4, -4));

        Vector2 size = new Vector2(getTank().getSprite().getImage().getWidth(), getTank().getSprite().getImage().getHeight());
        bullet.getPosition().add(new Vector2(size.getX() / 2, size.getY() / 2));

        Vector2 bulletSize = new Vector2(bullet.getSprite().getImage().getWidth(), bullet.getSprite().getImage().getHeight());
        bullet.getPosition().subtract(new Vector2(bulletSize.getX() / 2, bulletSize.getY() / 2));

        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        mission.addEntity(bullet);
    }
}
