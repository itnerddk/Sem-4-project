package org.sdu.sem4.g7.bounceTurret;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.tank.parts.Turret;

public class BounceTurret extends Turret {
    public BounceTurret() {
        super();
        setOffset(new Vector2(0, 0));
        setMuzzle(new Vector2(0, -25));
        setAttackSpeed(400);
        try {
            this.setSprite(this.getClass().getClassLoader().getResource("BounceTurret.png").toURI(), 5);
            this.setzIndex(-10);

            // Load the sound files
            if (getShootSoundFile() == null) {
                System.out.println(this.getClass().getResource("/shoot.wav"));
                // setShootSoundFile(this.getClass().getResource("/shoot.wav").toURI());
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
        // if (gameData.playAudio(SoundType.SHOOT, getShootSoundFile().toString(), 1.0f)) {
        //     System.out.println("Playing shoot sound");
        // } else {
        //     gameData.addAudio(SoundType.SHOOT, getShootSoundFile());
        // }

        BounceBullet bullet = new BounceBullet();

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

    @Override
    public String getWeaponId() {
        return "bounce_turret";
    }
}
