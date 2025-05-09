package org.sdu.sem4.g7.tank.parts;

import java.net.URI;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;
import org.sdu.sem4.g7.common.services.IWeaponInstance;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public abstract class Turret extends Entity implements IWeaponInstance {
    private Tank tank;
    private Class<? extends Bullet> bulletClass;
    private Vector2 offset;
    private Vector2 muzzle;
    private int rotationSpeed;
    private int attackSpeed;
    private long lastShotTime;
    private int damageUpgradeBonus;

    private URI shootSoundFile;
    private URI explosionSoundFile;

    public Turret(Class<? extends Bullet> bulletClass, Vector2 offset, Vector2 muzzle, int attackSpeed) {
        super();
        this.bulletClass = bulletClass;
        this.offset = offset;
        this.muzzle = muzzle;
        this.rotationSpeed = 3;
        this.attackSpeed = attackSpeed;
        setCollision(false);
    }
    
    public Tank getTank() {
        return this.tank;
    }
    
    public void setTank(Tank tank) {
        if (tank.getEntityType() == EntityType.PLAYER) {
            damageUpgradeBonus = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getDamageBonus)
                .orElse(0);
        }
        this.tank = tank;
    }

    public void aimTowards(Vector2 target) {
        Vector2 direction = new Vector2(target);
        direction.subtract(this.getPosition());
        direction.normalize();
        double targetRotation = Math.toDegrees(Math.atan2(direction.getY(), direction.getX())) + 90;
        double currentRotation = this.getRotation();
        double rotationDiff = targetRotation - currentRotation;
        if (rotationDiff > 180) {
            rotationDiff -= 360;
        } else if (rotationDiff < -180) {
            rotationDiff += 360;
        }
        if (Math.abs(rotationDiff) > rotationSpeed) {
            if (rotationDiff > 0) {
                this.setRotation((float) (currentRotation + rotationSpeed));
            } else {
                this.setRotation((float) (currentRotation - rotationSpeed));
            }
        } else {
            this.setRotation((float) targetRotation);
        }
    }

    public Bullet getBullet() throws Exception {
        Bullet bullet = bulletClass.getDeclaredConstructor().newInstance();
        bullet.setWeaponBonus(damageUpgradeBonus);
        bullet.setCreatedBy(this.tank);

        bullet.setPosition(new Vector2(this.getPosition()));
        bullet.getPosition().add(this.getOffset());
        Vector2 rotatedMuzzle = new Vector2(this.getMuzzle().getX(), this.getMuzzle().getY());
        rotatedMuzzle.rotate(this.getRotation());
        bullet.getPosition().add(rotatedMuzzle);
        
        bullet.setRotation(this.getRotation() - 90);
        
        float rotationInRadians = bullet.getRotation();
        rotationInRadians = (float) Math.toRadians(rotationInRadians);


        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        return bullet;
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

    public boolean tryShoot() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShotTime >= attackSpeed) {
            lastShotTime = currentTime;
            return true;
        }
        return false;
    }

    abstract public boolean shoot(GameData gameData, WorldData world);

    // Self functions
    public URI getShootSoundFile() {
        return shootSoundFile;
    }
    public URI getExplosionSoundFile() {
        return explosionSoundFile;
    }
    public void setShootSoundFile(URI shootSoundFile) {
        this.shootSoundFile = shootSoundFile;
    }
    public void setExplosionSoundFile(URI explosionSoundFile) {
        this.explosionSoundFile = explosionSoundFile;
    }

    public abstract String getWeaponId();
}