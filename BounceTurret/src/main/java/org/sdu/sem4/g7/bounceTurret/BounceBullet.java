package org.sdu.sem4.g7.bounceTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.services.IRigidbodyService;
import org.sdu.sem4.g7.tank.parts.Bullet;

public class BounceBullet extends Bullet {
    IRigidbodyService lastHit = null;
    BounceBullet() {
        super();
        setMaxFlightTime(3);
        setHealth(10);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("BounceBullet.png").toURI(), 2);
        } catch (URISyntaxException e) {e.printStackTrace();}
    }

    @Override
    public boolean onCollision(IRigidbodyService other, GameData gameData) {
        if (other == lastHit) {
            return true;
        }
        lastHit = other;
        int oldHealth = this.getHealth();
        super.onCollision(other, gameData);
        int newHealth = this.getHealth();
        if (oldHealth > newHealth) {
            // Some collision happened so make bounce
            Hitbox hitbox = other.getHitbox();
            float wallAngle = (getBounceAngle(hitbox) + 180) % 360;
            float thisAngle = this.getRotation();
            float angleDiff = thisAngle - wallAngle;
            float angle = thisAngle - (angleDiff * 2);
            angle += 180;
            angle = (angle + 360) % 360;
            // float angle = wallAngle;
            this.setRotation(angle);
            angle = (float) Math.toRadians(angle);
            this.setVelocity((float) Math.cos(angle) * 8, (float) Math.sin(angle) * 8);
            this.setPosition(this.getPosition().getX() + this.getVelocity().getX(), this.getPosition().getY() + this.getVelocity().getY());
        }
        return true;
    }

    private float getBounceAngle(Hitbox hitbox) {
        double angle = Math.toRadians(hitbox.getRotation());
        double bestDistance = Double.MAX_VALUE;
        double bestAngle = 0;
        for (int i = 0; i < 4; i++) {
            // get vertices of the hitbox
            float x = (float) (hitbox.getPosition().getX() + hitbox.getSize().getX() * Math.cos(angle));
            float y = (float) (hitbox.getPosition().getY() + hitbox.getSize().getY() * Math.sin(angle));
            double distance = Math.sqrt(Math.pow(x - this.getPosition().getX(), 2) + Math.pow(y - this.getPosition().getY(), 2));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestAngle = angle;
            }
            angle += Math.PI / 2;
        }
        return (float) Math.toDegrees(bestAngle);
    }
}
