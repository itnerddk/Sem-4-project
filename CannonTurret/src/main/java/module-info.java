import org.sdu.sem4.g7.cannonTurret.CannonTurret;
import org.sdu.sem4.g7.cannonTurret.CannonTurretProcessor;
import org.sdu.sem4.g7.tank.parts.Turret;

module CannonTurret {
    requires transitive Common;
    requires transitive TankParts;
    requires javafx.graphics;
    provides org.sdu.sem4.g7.common.services.IEntityProcessingService with CannonTurretProcessor;
    exports org.sdu.sem4.g7.cannonTurret to TankParts;
    // provides IEntityProcessingService with CannonTurretProcessor;
    // provides IPreGamePluginService with CannonTurretPlugin;
    provides Turret with CannonTurret;


    provides org.sdu.sem4.g7.common.services.IWeaponShopInfo
            with org.sdu.sem4.g7.cannonTurret.CannonTurretShopInfo;
}