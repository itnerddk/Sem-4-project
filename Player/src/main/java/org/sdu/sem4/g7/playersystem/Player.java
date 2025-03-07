package org.sdu.sem4.g7.playersystem;
import java.net.URISyntaxException;

import org.sdu.sem4.g7.tank.parts.Tank;

public class Player extends Tank {
    public Player() {
        super();
        try {
            System.out.println(this.getClass().getClassLoader().getResource("Player.png"));
            this.setSprite(this.getClass().getClassLoader().getResource("Player.png").toURI());
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}