package org.sdu.sem4.g7.currency;

public class CurrencyManager {
    private int currency;


// Start with 0 currency
    public CurrencyManager() {
        this.currency = 0;
    }

    public int getCurrency() {
        return currency;
    }

    public void addCurrency(int amount) {
        if (amount > 0) {
            currency += amount;
            System.out.println("Added " + amount + " currency. Total: " + currency);
        }
    }

    public void subtractCurrency(int amount) {
        if (amount > 0 && currency >= amount) {
            currency -= amount;
            System.out.println("Subtracted " + amount + " currency. Total: " + currency);
        } else {
            System.out.println("Not enough currency to subtract " + amount);
        }
    }
}