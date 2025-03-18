module Core {
    requires Common;  
    requires javafx.graphics;    
    opens org.sdu.sem4.g7.main to javafx.graphics;
    uses org.sdu.sem4.g7.common.services.IPreGamePluginService;
    uses org.sdu.sem4.g7.common.services.IGamePluginService;
    uses org.sdu.sem4.g7.common.services.IEntityProcessingService;
    uses org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
    uses org.sdu.sem4.g7.tank.parts.Tank;
}


