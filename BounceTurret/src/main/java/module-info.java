
module BounceTurret {
    requires transitive Common;
    requires transitive TankParts;

    // requires javafx.graphics;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.bounceTurret.BounceTurretProcessor;
    provides org.sdu.sem4.g7.tank.parts.Turret with org.sdu.sem4.g7.bounceTurret.BounceTurret;

    provides org.sdu.sem4.g7.common.services.IWeaponShopInfo
            with org.sdu.sem4.g7.bounceTurret.BounceTurretShopInfo;
}
