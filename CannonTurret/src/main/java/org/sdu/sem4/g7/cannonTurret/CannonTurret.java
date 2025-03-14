package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurret extends Turret {
    public CannonTurret() {
        super();
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
    public void shoot(GameData gameData, Mission mission) {
        CannonBullet bullet = new CannonBullet();
        bullet.setPosition(this.getPosition());
        mission.addEntity(bullet);
        bullet.setRotation(this.getRotation() - 90);
        float rotationInRadians = bullet.getRotation();
        rotationInRadians = (float) Math.toRadians(rotationInRadians);
        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);
    }
}
