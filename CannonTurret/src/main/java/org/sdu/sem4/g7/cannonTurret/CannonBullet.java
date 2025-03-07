package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Bullet;

public class CannonBullet extends Bullet {
    public CannonBullet() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("CannonBullet.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("CannonBullet.png").toURI());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
