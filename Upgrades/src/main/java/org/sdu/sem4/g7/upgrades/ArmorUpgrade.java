package org.sdu.sem4.g7.upgrades;

import org.sdu.sem4.g7.common.services.ServiceLocator;

public class ArmorUpgrade {
    private int level = 0;
    private final int[] prices = {2000, 3500, 6000, 8500, 12000};

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public boolean isMaxed() { return level >= prices.length; }
    public int getNextPrice() { return isMaxed() ? -1 : prices[level]; }

    public ArmorUpgrade() {
        ServiceLocator.getPersistenceService().ifPresent(
            service -> {
                // load level from persistence
                if (service.intExists(this.getClass().getName())) {
                    int loadedLevel = service.getInt(this.getClass().getName());
                    this.level = loadedLevel;
                }
            }
        );
    }

    public boolean upgrade() {
        if (isMaxed()) return false;

        return ServiceLocator.getCurrencyService().map(cs -> {
            int price = getNextPrice();
            if (cs.getCurrency() >= price) {
                cs.subtractCurrency(price);
                level++;

                // update persistence
                ServiceLocator.getPersistenceService().ifPresent(
                        persistenceService -> {
                            persistenceService.setInt(this.getClass().getName(), level);
                        }
                );

                return true;
            }
            return false;
        }).orElse(false);
    }
}