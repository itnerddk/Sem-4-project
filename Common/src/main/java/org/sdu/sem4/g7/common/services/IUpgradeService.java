package org.sdu.sem4.g7.common.services;

public interface IUpgradeService {

    int getHealthUpgradePrice();
    boolean upgradeHealth();
    boolean isHealthMaxed();
    int getHealthLevel();
    void setHealthLevel(int level);



    int getArmorUpgradePrice();
    boolean upgradeArmor();
    boolean isArmorMaxed();
    int getArmorLevel();
    void setArmorLevel(int level);

    int getSpeedUpgradePrice();
    boolean upgradeSpeed();
    boolean isSpeedMaxed();
    int getSpeedLevel();
    void setSpeedLevel(int level);
}