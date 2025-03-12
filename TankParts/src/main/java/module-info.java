module TankParts {
    requires Common;
    exports org.sdu.sem4.g7.tank.parts;
    exports org.sdu.sem4.g7.tank;
    uses org.sdu.sem4.g7.tank.parts.Turret;
    provides org.sdu.sem4.g7.common.services.IGamePluginService with org.sdu.sem4.g7.tank.TurretLoader;
}
