package org.sdu.sem4.g7.common.services;

import java.util.Optional;
import java.util.ServiceLoader;

public class ServiceLocator {

    private static ICurrencyService currencyService;
    private static ILevelService levelService;
    private static IUpgradeService upgradeService;
    private static IMissionLoaderService missionLoaderService;
    private static IPersistenceService persistenceLoaderService;
    private static IUpgradeStatsService upgradeStatsService;
    private static IAudioProcessingService audioProcessingService;
    private static ILogicService logicService;
    private static IRayCastingService rayCastingService;

    public static void loadServices() {
        ServiceLoader<ICurrencyService> currencyLoader = ServiceLoader.load(ICurrencyService.class);
        currencyService = currencyLoader.findFirst().orElse(null);

        ServiceLoader<ILevelService> levelLoader = ServiceLoader.load(ILevelService.class);
        levelService = levelLoader.findFirst().orElse(null);

        ServiceLoader<IUpgradeService> upgradeLoader = ServiceLoader.load(IUpgradeService.class);
        upgradeService = upgradeLoader.findFirst().orElse(null);

        ServiceLoader<IMissionLoaderService> missionLoader = ServiceLoader.load(IMissionLoaderService.class);
        missionLoaderService = missionLoader.findFirst().orElse(null);

        ServiceLoader<IPersistenceService> persistenceLoader = ServiceLoader.load(IPersistenceService.class);
        persistenceLoaderService = persistenceLoader.findFirst().orElse(null);

        ServiceLoader<IUpgradeStatsService> statsLoader = ServiceLoader.load(IUpgradeStatsService.class);
        upgradeStatsService = statsLoader.findFirst().orElse(null);

        ServiceLoader<IAudioProcessingService> audioLoader = ServiceLoader.load(IAudioProcessingService.class);
        audioProcessingService = audioLoader.findFirst().orElse(null);

        ServiceLoader<ILogicService> logicLoader = ServiceLoader.load(ILogicService.class);
        logicService = logicLoader.findFirst().orElse(null);

        ServiceLoader<IRayCastingService> rayCastingLoader = ServiceLoader.load(IRayCastingService.class);
        rayCastingService = rayCastingLoader.findFirst().orElse(null);
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

    public static Optional<IPersistenceService> getPersistenceService() {
        return Optional.ofNullable(persistenceLoaderService);
    }

    public static Optional<IUpgradeStatsService> getUpgradeStatsService() {
        return Optional.ofNullable(upgradeStatsService);
    }

    public static Optional<IAudioProcessingService> getAudioProcessingService () {
        return Optional.ofNullable(audioProcessingService);
    }

    public static Optional<ILogicService> getLogicService () {
        return Optional.ofNullable(logicService);
    }

    public static Optional<IRayCastingService> getRayCastingService () {
        return Optional.ofNullable(rayCastingService);
    }
}
