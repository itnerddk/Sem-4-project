module TankParts {
    requires transitive Common;
    requires transitive javafx.graphics;
    exports org.sdu.sem4.g7.tank.parts;
    exports org.sdu.sem4.g7.tank;
    uses org.sdu.sem4.g7.tank.parts.Turret;
    provides org.sdu.sem4.g7.common.services.IPreGamePluginService with org.sdu.sem4.g7.tank.TurretLoader;
    provides org.sdu.sem4.g7.common.services.ITurretProviderService with org.sdu.sem4.g7.tank.TurretLoader;
}
