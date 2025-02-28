module Core {
    requires Common;
    requires CommonBullet;    
    requires javafx.graphics;    
    opens org.sdu.sem4.g7.main to javafx.graphics;
    uses org.sdu.sem4.g7.common.services.IGamePluginService;
    uses org.sdu.sem4.g7.common.services.IEntityProcessingService;
    uses org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
}


