module Common {
    requires javafx.base;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.annotation;
    exports org.sdu.sem4.g7.common.services;
    exports org.sdu.sem4.g7.common.data;
    exports org.sdu.sem4.g7.common.enums;

    uses org.sdu.sem4.g7.common.services.ICurrencyService;
    uses org.sdu.sem4.g7.common.services.ILevelService;
    uses org.sdu.sem4.g7.common.services.IUpgradeService;
    uses org.sdu.sem4.g7.common.services.IMissionLoaderService;
    uses org.sdu.sem4.g7.common.services.IPersistenceService;
    uses org.sdu.sem4.g7.common.services.IUpgradeStatsService;
    uses org.sdu.sem4.g7.common.services.IAudioProcessingService;
    uses org.sdu.sem4.g7.common.services.IDifficultyService;
    uses org.sdu.sem4.g7.common.services.IBoughtWeaponsService;
    uses org.sdu.sem4.g7.common.services.IInventoryService;
}