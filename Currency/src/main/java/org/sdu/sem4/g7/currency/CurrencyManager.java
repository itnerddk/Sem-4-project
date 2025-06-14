package org.sdu.sem4.g7.currency;

import org.sdu.sem4.g7.common.services.ICurrencyService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class CurrencyManager implements ICurrencyService {
    /**
     * Key used to save currency
     */
    private static final String persistenceKey = "currency";

    /**
     * If the game does not have a persistence service, this variable will be able to hold the currency
     */
    private int localCurrency = 0;

    @Override
    public int getCurrency() {
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                // load currency from persistence
                if (persistenceService.intExists(persistenceKey)) {
                    int loadedCurrency = persistenceService.getInt(persistenceKey);
                    localCurrency = loadedCurrency;
                } else {
                    localCurrency = 0;
                }
            }
        );
        return localCurrency;
    }

    @Override
    public void addCurrency(int amount) {
        // add currency to memory
        localCurrency += amount;
        // save currency to persistence
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setInt(persistenceKey, localCurrency);
            }
        );
    }

    @Override
    public void subtractCurrency(int amount) {
        // subtract currency from memory
        localCurrency -= amount;
        // save currency to persistence
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                persistenceService.setInt(persistenceKey, localCurrency);
            }
        );
    }
}
