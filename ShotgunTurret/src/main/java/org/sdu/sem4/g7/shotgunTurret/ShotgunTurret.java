package org.sdu.sem4.g7.shotgunTurret;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.tank.parts.Bullet;
import org.sdu.sem4.g7.tank.parts.Turret;

public class ShotgunTurret extends Turret {

    public ShotgunTurret() {
        super(ShotgunBullet.class, new Vector2(0, 0), new Vector2(0, -25), 400);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("ShotgunTurret.png").toURI(), 5);
            this.setzIndex(-10);

            // Load the sound files
            if (getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shoot(GameData gameData, WorldData world) {
        // Check if the turret is ready to shoot
        if (tryShoot() == false) {
            return false;
        }
        // Randomize the rotation of the turret
        double thisRotation = this.getRotation();
        for (int i = 0; i < 6; i++) {
            this.setRotation((float) (thisRotation + (float) (Math.random() * 30 - 15)));
            Bullet bullet;
            try {
                bullet = getBullet();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error creating bullet");
                return false;
            }
    
            world.addEntity(bullet);
        }

        // Play the shoot sound
        if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1.0f)) {
            System.out.println("Playing shoot sound");
        } else {
            gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
        }
        this.setRotation((float) thisRotation);
        return true;
    }

    @Override
    public String getWeaponId() {
        return "shotgun_turret";
    }
    
}
