package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;

public interface ICurrencyService {
    int getCurrency();

    void addCurrency(int amount);

    void subtractCurrency(int amount);
}
