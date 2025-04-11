package org.sdu.sem4.g7.currency;

import java.util.Optional;

import org.sdu.sem4.g7.common.services.ICurrencyService;
import org.sdu.sem4.g7.common.services.IPersistenceService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class CurrencyManager implements ICurrencyService {

    private static final Optional<IPersistenceService> persistenceService = ServiceLocator.getPersistenceService();

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
        if (persistenceService.isPresent()) {
            // get currency from persistent storage
            return persistenceService.get().getInt(persistenceKey);
        } else {
            // get currency in memory
            return localCurrency;
        }
    }

    @Override
    public void addCurrency(int amount) {
        if (persistenceService.isPresent()) {
            // add currency to persistent storage
            if (amount > 0) persistenceService.get().setInt(persistenceKey, getCurrency() + amount);
        } else {
            // save currency in memory
            if (amount > 0) localCurrency += amount;
        }
    }

    @Override
    public void subtractCurrency(int amount) {
        if (persistenceService.isPresent()) {
            // subtract currency from persistent storage
            if (amount > 0 && getCurrency() >= amount) persistenceService.get().setInt(persistenceKey, getCurrency() - amount);
        } else {
            // subtract currency from memory
            if (amount > 0 && localCurrency >= amount) localCurrency -= amount;
        }
    }
}
