module ShotgunTurret {
    
    requires transitive Common;
    requires transitive TankParts;
    requires javafx.graphics;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.shotgunTurret.ShotgunProcessor;
    exports org.sdu.sem4.g7.shotgunTurret to TankParts;
    provides org.sdu.sem4.g7.tank.parts.Turret with org.sdu.sem4.g7.shotgunTurret.ShotgunTurret;
    provides org.sdu.sem4.g7.common.services.IWeaponShopInfo
            with org.sdu.sem4.g7.shotgunTurret.ShotgunTurretShopInfo;

}
