module Enemy {
    requires transitive TankParts;
    requires transitive Common;
    requires Player;
    requires javafx.graphics;
    exports org.sem4.g7.enemysystem;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sem4.g7.enemysystem.EnemyControlSystem;
    provides org.sdu.sem4.g7.common.services.IEntityPluginService with org.sem4.g7.enemysystem.EnemyPlugin;
    uses org.sdu.sem4.g7.common.services.ITurretProviderService;
}