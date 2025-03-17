import org.sdu.sem4.g7.common.services.IGamePluginService;


module MissionLoader {
    requires Common;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    exports org.sdu.sem4.g7.MissionLoader.objects to com.fasterxml.jackson.databind;
    provides IGamePluginService with org.sdu.sem4.g7.MissionLoader.services.Loader;
}
