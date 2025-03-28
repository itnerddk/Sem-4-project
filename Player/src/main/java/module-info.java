
module Player {
    exports org.sdu.sem4.g7.playersystem;
    requires transitive Common;
    requires transitive TankParts;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.playersystem.PlayerControlSystem;
    provides org.sdu.sem4.g7.common.services.IEntityPluginService with org.sdu.sem4.g7.playersystem.PlayerPlugin;
}