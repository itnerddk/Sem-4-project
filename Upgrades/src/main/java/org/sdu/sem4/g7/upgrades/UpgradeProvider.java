package org.sdu.sem4.g7.upgrades;

import org.sdu.sem4.g7.common.services.IUpgradeService;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;

public class UpgradeProvider implements IUpgradeService, IUpgradeStatsService {
    private final UpgradeManager instance = UpgradeManager.getInstance();

    @Override public int getHealthUpgradePrice() { return instance.getHealthUpgradePrice(); }
    @Override public boolean upgradeHealth() { return instance.upgradeHealth(); }
    @Override public boolean isHealthMaxed() { return instance.isHealthMaxed(); }
    @Override public int getHealthLevel() { return instance.getHealthLevel(); }
    @Override public void setHealthLevel(int level) { instance.setHealthLevel(level); }

    @Override public int getShieldUpgradePrice() { return instance.getShieldUpgradePrice(); }
    @Override public boolean upgradeShield() { return instance.upgradeShield(); }
    @Override public boolean isShieldMaxed() { return instance.isShieldMaxed(); }
    @Override public int getShieldLevel() { return instance.getShieldLevel(); }
    @Override public void setShieldLevel(int level) { instance.setShieldLevel(level); }

    @Override public int getSpeedUpgradePrice() { return instance.getSpeedUpgradePrice(); }
    @Override public boolean upgradeSpeed() { return instance.upgradeSpeed(); }
    @Override public boolean isSpeedMaxed() { return instance.isSpeedMaxed(); }
    @Override public int getSpeedLevel() { return instance.getSpeedLevel(); }
    @Override public void setSpeedLevel(int level) { instance.setSpeedLevel(level); }

    @Override public int getDamageUpgradePrice() { return instance.getDamageUpgradePrice(); }
    @Override public boolean upgradeDamage() { return instance.upgradeDamage(); }
    @Override public boolean isDamageMaxed() { return instance.isDamageMaxed(); }
    @Override public int getDamageLevel() { return instance.getDamageLevel(); }
    @Override public void setDamageLevel(int level) { instance.setDamageLevel(level); }

    @Override public int getArmorUpgradePrice() { return instance.getArmorUpgradePrice(); }
    @Override public boolean upgradeArmor() { return instance.upgradeArmor(); }
    @Override public boolean isArmorMaxed() { return instance.isArmorMaxed(); }
    @Override public int getArmorLevel() { return instance.getArmorLevel(); }
    @Override public void setArmorLevel(int level) { instance.setArmorLevel(level); }

    @Override public int getHealthBonus() { return instance.getHealthBonus(); }
    @Override public float getSpeedMultiplier() { return instance.getSpeedMultiplier(); }
    @Override public int getShieldBonus() { return instance.getShieldBonus(); }
    @Override public int getDamageBonus() { return instance.getDamageBonus(); }
    @Override public int getArmorBonus() { return instance.getArmorBonus(); }

}
