package org.sdu.sem4.g7.machineGunTurret;

import java.net.URI;
import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.tank.parts.Turret;

public class MachineGunTurret extends Turret {

    private static URI shootSoundFile;
    private static URI explosionSoundFile;

    public MachineGunTurret() {
        super();
        setAttackSpeed(50); // 20 shots per second
        setOffset(new Vector2(0, 0));
        setMuzzle(new Vector2(0, -25));

        try {
            this.setSprite(this.getClass().getClassLoader().getResource("MachineGunTurret.png").toURI(), 5);
            this.setzIndex(-10);

            // Load the sound files
            if (MachineGunTurret.getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                MachineGunTurret.setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
            }
        } catch (URISyntaxException e) {e.printStackTrace();}
    }

    boolean bulletShot = false; // Since the machinegun is fast the audio will only play every other time

    @Override
    public boolean shoot(GameData gameData, WorldData world) {
        // Check if the turret is ready to shoot
        if (tryShoot() == false) {
            return false;
        }
        if (this.bulletShot) {
            // Play the shoot sound
            if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1f)) {
                System.out.println("Playing shoot sound");
            } else {
                gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
            }
        }

        MachineGunBullet bullet = new MachineGunBullet();

        // Set WeaponDamage for the specific weapon
        bullet.setWeaponBonus(2);
        bullet.finalizeDamage();

        bullet.setPosition(this.getPosition());
        Vector2 rotatedMuzzle = new Vector2(this.getMuzzle().getX(), this.getMuzzle().getY());
        rotatedMuzzle.rotate(this.getRotation());
        bullet.getPosition().add(rotatedMuzzle);
        
        bullet.setRotation(this.getRotation());

        // set the tank as the createdBy
        bullet.setCreatedBy(this.getTank());

        float rotationInRadians = bullet.getRotation() - 90;
        rotationInRadians = (float) Math.toRadians(rotationInRadians);

        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        world.addEntity(bullet);
        this.bulletShot = !this.bulletShot;
        return true;
    }

    // Self functions
    public static URI getShootSoundFile() {
        return shootSoundFile;
    }
    public static URI getExplosionSoundFile() {
        return explosionSoundFile;
    }
    public static void setShootSoundFile(URI shootSoundFile) {
        MachineGunTurret.shootSoundFile = shootSoundFile;
    }
    public static void setExplosionSoundFile(URI explosionSoundFile) {
        MachineGunTurret.explosionSoundFile = explosionSoundFile;
    }
    
}
