import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IGamePluginService;


module MissionLoader {
    requires transitive Common;
    requires javafx.graphics;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    uses IEntityPluginService;
    exports org.sdu.sem4.g7.MissionLoader.objects to com.fasterxml.jackson.databind;
    exports org.sdu.sem4.g7.MissionLoader.services;
    provides IGamePluginService with org.sdu.sem4.g7.MissionLoader.services.Loader;
}
