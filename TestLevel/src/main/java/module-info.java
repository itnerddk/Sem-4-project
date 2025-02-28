import org.sdu.sem4.g7.common.services.IGamePluginService;

module TestLevel {
    requires Common;
    provides IGamePluginService with org.sdu.sem4.g7.testlevel.TestLevelPlugin;
}
