package org.sdu.sem4.g7.cannonTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Bullet;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurret extends Turret {

    public CannonTurret() {
        super(CannonBullet.class, new Vector2(0, 0), new Vector2(0, -25), 400);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("CannonTurret.png").toURI());
            this.setzIndex(-10);

            // Load the sound files
            if (getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
            }
        } catch (URISyntaxException e) {e.printStackTrace();}
    }

    @Override
    public boolean shoot(GameData gameData, WorldData mission) {
        // Check if the turret is ready to shoot
        if (tryShoot() == false) {
            return false;
        }
        Bullet bullet;
        try {
            bullet = getBullet();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error creating bullet");
            return false;
        }

        // Play the shoot sound
        if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1.0f)) {
            System.out.println("Playing shoot sound");
        } else {
            gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
        }

        mission.addEntity(bullet);
        return true;
    }

    @Override
    public String getWeaponId() {
        return "cannon_turret";
    }
}
