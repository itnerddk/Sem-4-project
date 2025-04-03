package org.sdu.sem4.g7.common.services;

import java.util.Optional;
import java.util.ServiceLoader;

public class ServiceLocator {

    private static ICurrencyService currencyService;

    public static void loadServices() {
        ServiceLoader<ICurrencyService> currencyLoader = ServiceLoader.load(ICurrencyService.class);
        currencyService = currencyLoader.findFirst().orElse(null);
    }

    public static Optional<ICurrencyService> getCurrencyService() {
        return Optional.ofNullable(currencyService);
    }
}
