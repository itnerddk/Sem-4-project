package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;

public interface ICurrencyService {
    int getCurrency(GameData gamedata);

    void addCurrency(GameData gamedata, int amount);

    void subtractCurrency(GameData gamedata, int amount);
}
