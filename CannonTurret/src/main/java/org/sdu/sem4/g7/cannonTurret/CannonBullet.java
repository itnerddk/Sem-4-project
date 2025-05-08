package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Bullet;

public class CannonBullet extends Bullet {

    public CannonBullet() {
        super(100);
        setMaxFlightTime(3);
        setHealth(1);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("CannonBullet.png").toURI(), 2);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
