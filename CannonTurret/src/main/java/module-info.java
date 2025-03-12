import org.sdu.sem4.g7.cannonTurret.CannonTurret;
import org.sdu.sem4.g7.cannonTurret.CannonTurretProcessor;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;
import org.sdu.sem4.g7.tank.parts.Turret;

module CannonTurret {
    requires Common;
    requires TankParts;
    // provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.cannonturret.CannonTurretControlSystem;

    // provides IEntityProcessingService with CannonTurretProcessor;
    // provides IPreGamePluginService with CannonTurretPlugin;
    provides Turret with CannonTurret;
}