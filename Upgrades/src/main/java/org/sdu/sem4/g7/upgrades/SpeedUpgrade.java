package org.sdu.sem4.g7.upgrades;

import org.sdu.sem4.g7.common.services.ServiceLocator;

public class SpeedUpgrade {
    private int level = 0;
    private final int[] prices = {3000, 4500, 7000, 10000, 15000};

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public boolean isMaxed() { return level >= prices.length; }
    public int getNextPrice() { return isMaxed() ? -1 : prices[level]; }

    public boolean upgrade() {
        if (isMaxed()) return false;

        return ServiceLocator.getCurrencyService().map(cs -> {
            int price = getNextPrice();
            if (cs.getCurrency() >= price) {
                cs.subtractCurrency(price);
                level++;
                return true;
            }
            return false;
        }).orElse(false);
    }
}