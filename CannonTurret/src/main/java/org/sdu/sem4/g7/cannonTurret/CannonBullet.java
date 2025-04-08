package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Bullet;

public class CannonBullet extends Bullet {

    public CannonBullet() {
        super(10); // TODO: Find a better way to set bullet damage #HACKFIX
        setCollision(true);
        setHealth(1);
        try {
            // System.out.println(this.getClass().getClassLoader().getResource("CannonBullet.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("CannonBullet.png").toURI(), 2);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
