package org.sdu.sem4.g7.tank.parts;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.Vector2;

public class Turret extends Entity {
    private Tank tank;
    private Bullet bullet;
    private Vector2 offset;
    private Vector2 muzzle;
    private int attackSpeed;

    public Turret() {
    }

    public Tank getTank() {
        return this.tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public Bullet getBullet() {
        return this.bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public Vector2 getOffset() {
        return this.offset;
    }

    public void setOffset(Vector2 offset) {
        this.offset = offset;
    }

    public Vector2 getMuzzle() {
        return this.muzzle;
    }

    public void setMuzzle(Vector2 muzzle) {
        this.muzzle = muzzle;
    }

    public int getAttackSpeed() {
        return this.attackSpeed;
    }

    public void setAttackSpeed(int attackSpeed) {
        this.attackSpeed = attackSpeed;
    }
}