import org.sdu.sem4.g7.common.aware.*;
import org.sdu.sem4.g7.common.services.*;

module Core {
    // Core
    requires Common;  
    // Javafx
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.base;

    // Opening to
    opens org.sdu.sem4.g7.main to javafx.graphics;
    opens org.sdu.sem4.g7.UI.controllers to javafx.fxml;

    // Services
    uses IPreGamePluginService;
    uses IGamePluginService;
    uses IEntityProcessingService;
    uses IPostEntityProcessingService;
    uses ICurrencyService;
    uses IMissionLoaderService;
    uses ISettingPluginService;
    uses IInventoryService;
    uses ITurretProviderService;
    uses IWeaponShopInfo;

    // Aware
    uses IGameDataAware;
    uses IMapAware;


    // Exports
    exports org.sdu.sem4.g7.main;
    exports org.sdu.sem4.g7.UI.controllers to javafx.fxml;
}


