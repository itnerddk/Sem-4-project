package org.sdu.sem4.g7.main;

import org.sdu.sem4.g7.common.services.IUpgradeService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class UpgradePlaceholderService implements IUpgradeService {


    // Health upgrade
    private int healthLevel = 0;
    private final int[] prices = {2000, 3500, 5000, 7000, 10000};

    @Override
    public int getHealthLevel() {
        return healthLevel;
    }

    @Override
    public void setHealthLevel(int level) {
        this.healthLevel = level;
    }

    @Override
    public boolean isHealthMaxed() {
        return healthLevel >= prices.length;
    }

    @Override
    public int getHealthUpgradePrice() {
        return isHealthMaxed() ? -1 : prices[healthLevel];
    }

    @Override
    public boolean upgradeHealth() {
        if (isHealthMaxed()) return false;

        return ServiceLocator.getCurrencyService().map(currencyService -> {
            int price = getHealthUpgradePrice();
            if (currencyService.getCurrency() >= price) {
                currencyService.subtractCurrency(price);
                healthLevel++;
                return true;
            }
            return false;
        }).orElse(false);
    }


    // Armor upgrades
    private int armorLevel = 0;
    private final int[] armorPrices = {1500, 3000, 4500, 6000, 8000};

    @Override
    public int getArmorLevel() {
        return armorLevel;
    }

    @Override
    public void setArmorLevel(int level) {
        this.armorLevel = level;
    }

    @Override
    public boolean isArmorMaxed() {
        return armorLevel >= armorPrices.length;
    }

    @Override
    public int getArmorUpgradePrice() {
        return isArmorMaxed() ? -1 : armorPrices[armorLevel];
    }

    @Override
    public boolean upgradeArmor() {
        if (isArmorMaxed()) return false;

        return ServiceLocator.getCurrencyService().map(currencyService -> {
            int price = getArmorUpgradePrice();
            if (currencyService.getCurrency() >= price) {
                currencyService.subtractCurrency(price);
                armorLevel++;
                return true;
            }
            return false;
        }).orElse(false);
    }


    // Sped upgrades
    private int speedLevel = 0;
    private final int[] speedPrices = {3000, 4500, 7000, 10000, 15000};

    @Override
    public int getSpeedLevel() {
        return speedLevel;
    }

    @Override
    public void setSpeedLevel(int level) {
        this.speedLevel = level;
    }

    @Override
    public int getSpeedUpgradePrice() {
        return isSpeedMaxed() ? -1 : speedPrices[speedLevel];
    }

    @Override
    public boolean isSpeedMaxed() {
        return speedLevel >= speedPrices.length;
    }

    @Override
    public boolean upgradeSpeed() {
        if (isSpeedMaxed()) return false;

        return ServiceLocator.getCurrencyService().map(currencyService -> {
            int price = getSpeedUpgradePrice();
            if (currencyService.getCurrency() >= price) {
                currencyService.subtractCurrency(price);
                speedLevel++;
                return true;
            }
            return false;
        }).orElse(false);
    }
}
