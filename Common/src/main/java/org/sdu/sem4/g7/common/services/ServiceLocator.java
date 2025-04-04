package org.sdu.sem4.g7.common.services;

import java.util.Optional;
import java.util.ServiceLoader;

public class ServiceLocator {

    private static ICurrencyService currencyService;
    private static ILevelService levelService;

    public static void loadServices() {

        // CurrencyServiceLoad
        ServiceLoader<ICurrencyService> currencyLoader = ServiceLoader.load(ICurrencyService.class);
        currencyService = currencyLoader.findFirst().orElse(null);

        // LevelServiceLoad
        ServiceLoader<ILevelService> levelLoader = ServiceLoader.load(ILevelService.class);
        levelService = levelLoader.findFirst().orElse(null);
    }

    public static Optional<ICurrencyService> getCurrencyService() {
        return Optional.ofNullable(currencyService);
    }

    public static Optional<ILevelService> getLevelService() {
        return Optional.ofNullable(levelService);
    }

}
