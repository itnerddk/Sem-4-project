package org.sdu.sem4.g7.machineGunTurret;

import java.net.URISyntaxException;
import org.sdu.sem4.g7.tank.parts.Bullet;

public class MachineGunBullet extends Bullet {
    public MachineGunBullet() {
        super();
        setMaxFlightTime(1.1 + Math.random() * 0.3);
        setHealth(1);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("MachineGunBullet.png").toURI(), 2);
        } catch (URISyntaxException e) {e.printStackTrace();}
    }
}
