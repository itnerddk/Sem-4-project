import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

module TestMission {
    requires Common;
    uses IEntityPluginService;
    provides IGamePluginService with org.sdu.sem4.g7.testmission.TestMissionPlugin;
}
