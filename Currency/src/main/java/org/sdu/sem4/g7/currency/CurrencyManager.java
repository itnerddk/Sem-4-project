package org.sdu.sem4.g7.currency;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.services.ICurrencyService;

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
    public int getCurrency(GameData gameData) {

        if (gameData.getPersistenceService() != null) {
            // get currency from persistent storage
            return gameData.getPersistenceService().getInt(persistenceKey);
        } else {
            // get currency in memory
            return localCurrency;
        }
    }

    @Override
    public void addCurrency(GameData gameData, int amount) {
        if (gameData.getPersistenceService() != null) {
            // add currency to persistent storage
            if (amount > 0) gameData.getPersistenceService().setInt(persistenceKey, getCurrency(gameData) + amount);
        } else {
            // save currency in memory
            if (amount > 0) localCurrency += amount;
        }
    }

    @Override
    public void subtractCurrency(GameData gameData, int amount) {
        if (gameData.getPersistenceService() != null) {
            // subtract currency from persistent storage
            if (amount > 0 && getCurrency(gameData) >= amount) gameData.getPersistenceService().setInt(persistenceKey, getCurrency(gameData) - amount);
        } else {
            // subtract currency from memory
            if (amount > 0 && localCurrency >= amount) localCurrency -= amount;
        }
    }
}
