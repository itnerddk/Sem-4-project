package org.sdu.sem4.g7.currency;

import org.sdu.sem4.g7.common.services.ICurrencyService;
import org.sdu.sem4.g7.common.services.IPersistenceService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class CurrencyManager implements ICurrencyService {

    private IPersistenceService persistenceService;

    /**
     * Key used to save currency
     */
    private static final String persistenceKey = "currency";

    /**
     * If the game does not have a persistence service, this variable will be able to hold the currency
     */
    private int localCurrency = 0;

    public CurrencyManager() {
        ServiceLocator.getPersistenceService().ifPresentOrElse(
            service -> this.persistenceService = service,
            () -> System.out.println("Currency manager does not have a persistenceService!")
        );
    }

    @Override
    public int getCurrency() {

        if (persistenceService != null) {
            // get currency from persistent storage
            return persistenceService.getInt(persistenceKey);
        } else {
            // get currency in memory
            return localCurrency;
        }
    }

    @Override
    public void addCurrency(int amount) {
        if (persistenceService != null) {
            // add currency to persistent storage
            if (amount > 0) persistenceService.setInt(persistenceKey, getCurrency() + amount);
        } else {
            // save currency in memory
            if (amount > 0) localCurrency += amount;
        }
    }

    @Override
    public void subtractCurrency(int amount) {
        if (persistenceService != null) {
            // subtract currency from persistent storage
            if (amount > 0 && getCurrency() >= amount) persistenceService.setInt(persistenceKey, getCurrency() - amount);
        } else {
            // subtract currency from memory
            if (amount > 0 && localCurrency >= amount) localCurrency -= amount;
        }
    }
}
