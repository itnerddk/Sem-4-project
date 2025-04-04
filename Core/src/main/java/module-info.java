module Core {
    requires Common;  
    requires javafx.graphics;
    requires TankParts;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.controls;
    requires MissionLoader;
    requires javafx.base;


    opens org.sdu.sem4.g7.main to javafx.graphics;
    opens org.sdu.sem4.g7.UI.controllers to javafx.fxml;

    uses org.sdu.sem4.g7.common.services.IPreGamePluginService;
    uses org.sdu.sem4.g7.common.services.IGamePluginService;
    uses org.sdu.sem4.g7.common.services.IEntityProcessingService;
    uses org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
    uses org.sdu.sem4.g7.tank.parts.Tank;
    uses org.sdu.sem4.g7.common.services.ICurrencyService;
    uses org.sdu.sem4.g7.common.services.IMissionLoaderService;

    exports org.sdu.sem4.g7.main;
    exports org.sdu.sem4.g7.UI.controllers to javafx.fxml;

}


