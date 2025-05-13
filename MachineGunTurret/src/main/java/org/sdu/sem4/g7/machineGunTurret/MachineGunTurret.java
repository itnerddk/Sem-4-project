package org.sdu.sem4.g7.machineGunTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.tank.parts.Bullet;
import org.sdu.sem4.g7.tank.parts.Turret;

public class MachineGunTurret extends Turret {
    public MachineGunTurret() {
        super(MachineGunBullet.class, new Vector2(0, 0), new Vector2(0, -25), 50);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("MachineGunTurret.png").toURI());
            this.setzIndex(-10);

            // Load the sound files
            if (getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
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
        Bullet bullet;
        try {
            double thisRotation = this.getRotation();
            this.setRotation((float) (thisRotation + (float)(Math.random() * 15 - 7.5)));
            bullet = getBullet();
            this.setRotation((float) thisRotation);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating bullet");
            return false;
        }
        if (this.bulletShot) {
            // Play the shoot sound
            if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1f, false)) {
                System.out.println("Playing shoot sound");
            } else {
                gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
            }
        }

        world.addEntity(bullet);
        this.bulletShot = !this.bulletShot;
        return true;
    }

    @Override
    public String getWeaponId() {
        return "machine_gun";
    }
}
