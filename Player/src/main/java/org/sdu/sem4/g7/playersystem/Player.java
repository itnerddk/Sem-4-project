package org.sdu.sem4.g7.playersystem;
import org.sdu.sem4.g7.common.data.Tank;

public class Player extends Tank {
    public Player() {
        super();
        this.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        this.setRadius(8);
    }
}