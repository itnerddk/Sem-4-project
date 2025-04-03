package org.sdu.sem4.g7.common.services;

public interface ICurrencyService {
    int getCurrency();
    void addCurrency(int amount);

    void subtractCurrency(int amount);
}
