package org.sdu.sem4.g7.currency;

import java.util.ServiceLoader;

import org.sdu.sem4.g7.common.services.ICurrencyService;
import org.sdu.sem4.g7.common.services.IPersistenceService;

public class CurrencyManager implements ICurrencyService {

    /**
     * Key used to save currency
     */
    private static final String persistenceKey = "currency";

    /**
     * If the game does not have a persistence service, this variable will be able to hold the currency
     */
    private int localCurrency = 0;

    private IPersistenceService getPersistenceService() {
        return ServiceLoader.load(IPersistenceService.class).findFirst().orElse(null);
    }

    @Override
    public int getCurrency() {
        localCurrency = getPersistenceService().getInt(persistenceKey);
        // return currency from memory
        return localCurrency;
    }

    @Override
    public void addCurrency(int amount) {
        // add currency to memory
        localCurrency += amount;
        // save currency to persistence
        getPersistenceService().setInt(persistenceKey, localCurrency);
    }

    @Override
    public void subtractCurrency(int amount) {
        // subtract currency from memory
        localCurrency -= amount;
        // save currency to persistence
        getPersistenceService().setInt(persistenceKey, localCurrency);
    }
}
