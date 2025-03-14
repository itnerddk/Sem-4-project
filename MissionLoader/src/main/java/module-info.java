import org.sdu.sem4.g7.common.services.IGamePluginService;

module module.name {
    requires Common;
    provides IGamePluginService with org.sdu.sem4.g7.MissionLoader.services.Loader;
}
