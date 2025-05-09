import org.sdu.sem4.g7.common.services.*;
import org.sdu.sem4.g7.common.aware.*;

module Common {
    requires javafx.base;
    requires transitive javafx.graphics;
    requires com.fasterxml.jackson.annotation;
    exports org.sdu.sem4.g7.common.services;
    exports org.sdu.sem4.g7.common.aware;
    exports org.sdu.sem4.g7.common.data;
    exports org.sdu.sem4.g7.common.enums;
    exports org.sdu.sem4.g7.common.Config;

    uses ICurrencyService;
    uses ILevelService;
    uses IUpgradeService;
    uses IMissionLoaderService;
    uses IPersistenceService;
    uses IUpgradeStatsService;
    uses IAudioProcessingService;
    uses ILogicService;
    uses IRayCastingService;
    uses IDifficultyService;
    uses IBoughtWeaponsService;
    uses IInventoryService;

    // Aware
    uses IMapAware;
    uses IWorldAware;

}