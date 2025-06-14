import org.sdu.sem4.g7.common.aware.IMapAware;
import org.sdu.sem4.g7.common.aware.IWorldAware;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IMissionLoaderService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;


module MissionLoader {
    requires transitive Common;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    uses IEntityPluginService;
    uses IMapAware;
    uses IWorldAware;
    exports org.sdu.sem4.g7.MissionLoader.objects to com.fasterxml.jackson.databind;
    exports org.sdu.sem4.g7.MissionLoader.services;
    provides IPreGamePluginService with org.sdu.sem4.g7.MissionLoader.services.Loader;
    provides IMissionLoaderService with org.sdu.sem4.g7.MissionLoader.services.MissionLoaderService;
    provides IEntityProcessingService with org.sdu.sem4.g7.MissionLoader.services.TileService;
}
