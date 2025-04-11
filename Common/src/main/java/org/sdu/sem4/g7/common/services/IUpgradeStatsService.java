package org.sdu.sem4.g7.common.services;

public interface IUpgradeStatsService {
    int getHealthBonus();       // +10 HP per lvl
    float getSpeedMultiplier(); // + 0.05 speed per level
    int getShieldBonus();        // +5 shield per level
    int getDamageBonus();       // +5 damage per level
}
