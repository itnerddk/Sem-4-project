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
    uses org.sdu.sem4.g7.common.services.IPreGamePluginService;
    uses org.sdu.sem4.g7.common.services.IGamePluginService;
    uses org.sdu.sem4.g7.common.services.IEntityProcessingService;
    uses org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
    uses org.sdu.sem4.g7.common.services.ICurrencyService;
    uses org.sdu.sem4.g7.common.services.IMissionLoaderService;
    uses org.sdu.sem4.g7.common.services.ISettingPluginService;
    uses org.sdu.sem4.g7.common.services.IInventoryService;
    uses org.sdu.sem4.g7.common.services.ITurretProviderService;

    // Exports
    exports org.sdu.sem4.g7.main;
    exports org.sdu.sem4.g7.UI.controllers to javafx.fxml;
}


