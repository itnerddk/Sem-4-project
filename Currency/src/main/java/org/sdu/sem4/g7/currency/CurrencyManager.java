package org.sdu.sem4.g7.currency;

import org.sdu.sem4.g7.common.services.ICurrencyService;

public class CurrencyManager implements ICurrencyService {
    private int currency = 999999;

    @Override
    public int getCurrency() {
        return currency;
    }

    @Override
    public void addCurrency(int amount) {
        if (amount > 0) currency += amount;
    }

    @Override
    public void subtractCurrency(int amount) {
        if (amount > 0 && currency >= amount) currency -= amount;
    }
}
