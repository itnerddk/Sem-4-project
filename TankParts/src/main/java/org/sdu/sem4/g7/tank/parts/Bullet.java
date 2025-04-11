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
    private int upgradeBonus = 0;
    private int damage;

    private Entity createdBy;

    public Bullet() {
        super();
        setEntityType(EntityType.BULLET);
    }

    public void finalizeDamage() {
        int upgradeBonus = ServiceLocator.getUpgradeStatsService()
                .map(IUpgradeStatsService::getDamageBonus)
                .orElse(0);
        this.damage = baseDamage + upgradeBonus + weaponBonus;
    }

    public void setWeaponBonus(int weaponBonus) {
        this.weaponBonus = weaponBonus;
    }

    public void setUpgradeBonus(int upgradeBonus) {
        this.upgradeBonus = upgradeBonus;
    }

    public int getDamage() {
        return damage;
    }

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public Entity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Entity createdBy) {
        this.createdBy = createdBy;
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

            try {
                Method methodGetShield = otherEntity.getClass().getMethod("getShield");
                Method methodSetShield = otherEntity.getClass().getMethod("setShield", int.class);

                int shield = (int) methodGetShield.invoke(otherEntity);

                if (shield > 0) {
                    int damageToShield = Math.min(damage, shield);
                    int remainingDamage = damage - damageToShield;

                    methodSetShield.invoke(otherEntity, shield - damageToShield);
                    otherEntity.setHealth(otherEntity.getHealth() - remainingDamage);
                } else {
                    otherEntity.setHealth(otherEntity.getHealth() - this.damage);
                }
            } catch (Exception e) {
                otherEntity.setHealth(otherEntity.getHealth() - this.damage);
            }


            this.setHealth(0);
            gameData.playAudio(SoundType.HIT);
            return true;
        }


        return false;
    }
}
