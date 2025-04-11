package org.sdu.sem4.g7.upgrades;

import org.sdu.sem4.g7.common.services.IUpgradeService;
import org.sdu.sem4.g7.common.services.IUpgradeStatsService;

public class UpgradeManager implements IUpgradeService, IUpgradeStatsService {

    public UpgradeManager() {
        if (instance == null) instance = this;
    }

    private static UpgradeManager instance;
    public static UpgradeManager getInstance() {
        if (instance == null) {
            instance = new UpgradeManager();
        }
        return instance;
    }

    // Upgrade objekter
    private final HealthUpgrade health = new HealthUpgrade();
    private final ShieldUpgrade shield = new ShieldUpgrade();
    private final SpeedUpgrade speed = new SpeedUpgrade();
    private final DamageUpgrade damage = new DamageUpgrade();

    // Health
    @Override public int getHealthUpgradePrice() { return health.getNextPrice(); }
    @Override public boolean upgradeHealth() { return health.upgrade(); }
    @Override public boolean isHealthMaxed() { return health.isMaxed(); }
    @Override public int getHealthLevel() { return health.getLevel(); }
    @Override public void setHealthLevel(int level) { health.setLevel(level); }

    // Shield
    @Override public int getShieldUpgradePrice() { return shield.getNextPrice(); }
    @Override public boolean upgradeShield() { return shield.upgrade(); }
    @Override public boolean isShieldMaxed() { return shield.isMaxed(); }
    @Override public int getShieldLevel() { return shield.getLevel(); }
    @Override public void setShieldLevel(int level) { shield.setLevel(level); }

    // Speed
    @Override public int getSpeedUpgradePrice() { return speed.getNextPrice(); }
    @Override public boolean upgradeSpeed() { return speed.upgrade(); }
    @Override public boolean isSpeedMaxed() { return speed.isMaxed(); }
    @Override public int getSpeedLevel() { return speed.getLevel(); }
    @Override public void setSpeedLevel(int level) { speed.setLevel(level); }

    @Override public int getDamageUpgradePrice() { return damage.getNextPrice(); }
    @Override public boolean upgradeDamage() { return damage.upgrade(); }
    @Override public boolean isDamageMaxed() { return damage.isMaxed(); }
    @Override public int getDamageLevel() { return damage.getLevel();}
    @Override public void setDamageLevel(int level) { damage.setLevel(level); }


    @Override
    public int getHealthBonus() {
        return health.getLevel() * 10;
    }

    @Override
    public float getSpeedMultiplier() {
        return speed.getLevel() * 0.05f;
    }

    @Override
    public int getShieldBonus() {
        return shield.getLevel() * 5;
    }

    @Override
    public int getDamageBonus() {
        return damage.getLevel();
    }
}

