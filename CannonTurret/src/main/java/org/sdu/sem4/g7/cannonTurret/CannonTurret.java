package org.sdu.sem4.g7.cannonTurret;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Turret;

import javafx.scene.canvas.GraphicsContext;

public class CannonTurret extends Turret {

    private static URI shootSoundFile;
    private static URI explosionSoundFile;

    public CannonTurret() {
        super();
        setOffset(new Vector2(0, 0));
        setMuzzle(new Vector2(0, -25));
        setAttackSpeed(400);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("CannonTurret.png").toURI(), 5);
            this.setzIndex(-10);

            // Load the sound files
            if (CannonTurret.getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                CannonTurret.setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
            }
        } catch (URISyntaxException e) {e.printStackTrace();}
    }

    @Override
    public boolean shoot(GameData gameData, WorldData mission) {
        // Check if the turret is ready to shoot
        if (tryShoot() == false) {
            return false;
        }
        // Play the shoot sound
        if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1.0f)) {
            System.out.println("Playing shoot sound");
        } else {
            gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
        }

        CannonBullet bullet = new CannonBullet();

        // Set WeaponDamage for the specific weapon
        bullet.setWeaponBonus(100);
        bullet.finalizeDamage();

        bullet.setPosition(this.getPosition());
        // bullet.getPosition().add(this.getOffset());
        Vector2 rotatedMuzzle = new Vector2(this.getMuzzle().getX(), this.getMuzzle().getY());
        rotatedMuzzle.rotate(this.getRotation());
        bullet.getPosition().add(rotatedMuzzle);
        
        bullet.setRotation(this.getRotation());

        // set the tank as the createdBy
        bullet.setCreatedBy(this.getTank());
        
        float rotationInRadians = bullet.getRotation() - 90;
        rotationInRadians = (float) Math.toRadians(rotationInRadians);


        bullet.setVelocity(Math.cos(rotationInRadians) * 8, Math.sin(rotationInRadians) * 8);

        mission.addEntity(bullet);
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
        CannonTurret.shootSoundFile = shootSoundFile;
    }
    public static void setExplosionSoundFile(URI explosionSoundFile) {
        CannonTurret.explosionSoundFile = explosionSoundFile;
    }
}
