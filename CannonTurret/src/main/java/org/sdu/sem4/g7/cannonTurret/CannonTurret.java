package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurret extends Turret {
    public CannonTurret() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("Sprite.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("Sprite.png").toURI());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void shoot() {
        
    }
}
