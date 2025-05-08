package org.sdu.sem4.g7.tank.parts;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Hitbox;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.IRigidbodyService;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import java.lang.reflect.Method;

public abstract class Bullet extends Entity implements IRigidbodyService {

    private int baseDamage = 0;
    private int weaponBonus = 0;

    private double fligthTime = 0;
    private double maxFlightTime = 1; // in seconds

    private Entity createdBy;

    public Bullet(int baseDamage) {
        super();
        this.baseDamage = baseDamage;
        setEntityType(EntityType.BULLET);
        setCollision(true);
    }

    public void setWeaponBonus(int weaponBonus) {
        this.weaponBonus = weaponBonus;
    }

    public int getDamage() {
        return baseDamage + weaponBonus;
    }

    public void increaseFlightTime(double delta) {
        this.fligthTime += delta;
        if (this.fligthTime > maxFlightTime) {
            this.setHealth(0);
        }
    }

    public void setMaxFlightTime(double maxFlightTime) {
        this.maxFlightTime = maxFlightTime;
    }

    public Entity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Entity createdBy) {
        this.createdBy = createdBy;
    }

    private int getArmorReduction(Entity entity) {
        if (entity.getEntityType() != EntityType.PLAYER) return 0;

        return ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getArmorBonus)
                .orElse(0);
    }

    private Hitbox hitbox;

    public Hitbox getHitbox() {
        if (this.hitbox != null) {
            this.hitbox.setPosition(new Vector2(getPosition()));
            return this.hitbox;
        }
        Vector2 size = new Vector2(getSprite().getImage().getWidth(), getSprite().getImage().getHeight());
        return new Hitbox(new Vector2(getPosition()), size, getRotation());
    }

    @Override
    public boolean onCollision(IRigidbodyService other, GameData gameData) {
        if (other instanceof Bullet) return true;

        if (other instanceof Entity) {
            if (this.createdBy != null && this.createdBy.equals(other)) return true;

            Entity otherEntity = (Entity) other;

            int effectiveDamage = Math.max(0, getDamage() - getArmorReduction(otherEntity));

            try {
                Method methodGetShield = otherEntity.getClass().getMethod("getShield");
                Method methodSetShield = otherEntity.getClass().getMethod("setShield", int.class);

                int shield = (int) methodGetShield.invoke(otherEntity);

                if (shield > 0) {
                    System.out.println(otherEntity.getHealth());
                    int damageToShield = Math.min(effectiveDamage, shield);
                    int remainingDamage = effectiveDamage - damageToShield;

                    methodSetShield.invoke(otherEntity, shield - damageToShield);
                    otherEntity.setHealth(otherEntity.getHealth() - remainingDamage);
                } else {
                    otherEntity.setHealth(otherEntity.getHealth() - effectiveDamage);
                }

            } catch (Exception e) {

                otherEntity.setHealth(otherEntity.getHealth() - effectiveDamage);
            }

            System.out.println("Damage was reduced by " + getArmorReduction(otherEntity) + " from armor");

            this.setHealth(this.getHealth() - 1);
            gameData.playAudio(SoundType.HIT);
            return true;
        }

        return false;
    }

}
