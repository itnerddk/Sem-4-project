package org.sdu.sem4.g7.shotgunTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Bullet;

public class ShotgunBullet extends Bullet {

    public ShotgunBullet() {
        super(20);
        setMaxFlightTime(0.5 + (float) Math.random() * 0.4f);
        setHealth(1);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("ShotgunBullet.png").toURI(), 2);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}
