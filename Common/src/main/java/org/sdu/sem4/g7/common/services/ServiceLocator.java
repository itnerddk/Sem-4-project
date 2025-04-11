package org.sdu.sem4.g7.common.services;

import java.util.Optional;
import java.util.ServiceLoader;

public class ServiceLocator {

    private static ICurrencyService currencyService;
    private static ILevelService levelService;
    private static IUpgradeService upgradeService;
    private static IMissionLoaderService missionLoaderService;
    private static IUpgradeStatsService upgradeStatsService;

    public static void loadServices() {
        ServiceLoader<ICurrencyService> currencyLoader = ServiceLoader.load(ICurrencyService.class);
        currencyService = currencyLoader.findFirst().orElse(null);

        ServiceLoader<ILevelService> levelLoader = ServiceLoader.load(ILevelService.class);
        levelService = levelLoader.findFirst().orElse(null);

        ServiceLoader<IUpgradeService> upgradeLoader = ServiceLoader.load(IUpgradeService.class);
        upgradeService = upgradeLoader.findFirst().orElse(null);

        ServiceLoader<IMissionLoaderService> missionLoader = ServiceLoader.load(IMissionLoaderService.class);
        missionLoaderService = missionLoader.findFirst().orElse(null);

        ServiceLoader<IUpgradeStatsService> statsLoader = ServiceLoader.load(IUpgradeStatsService.class);
        upgradeStatsService = statsLoader.findFirst().orElse(null);
    }

    public static Optional<ICurrencyService> getCurrencyService() {
        return Optional.ofNullable(currencyService);
    }

    public static Optional<ILevelService> getLevelService() {
        return Optional.ofNullable(levelService);
    }

    public static Optional<IUpgradeService> getUpgradeService() {
        return Optional.ofNullable(upgradeService);
    }

    public static Optional<IMissionLoaderService> getMissionLoaderService() {
        return Optional.ofNullable(missionLoaderService);
    }

    public static Optional<IUpgradeStatsService> getUpgradeStatsService() {
        return Optional.ofNullable(upgradeStatsService);
    }
}
