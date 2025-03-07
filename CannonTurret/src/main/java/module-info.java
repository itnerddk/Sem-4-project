import org.sdu.sem4.g7.cannonTurret.CannonTurretPlugin;
import org.sdu.sem4.g7.cannonTurret.CannonTurretProcessor;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

module CannonTurret {
    requires Common;
    requires TankParts;
    // provides org.sdu.sem4.g7.common.services.IEntityProcessingService with org.sdu.sem4.g7.cannonturret.CannonTurretControlSystem;

    provides IEntityProcessingService with CannonTurretProcessor;
    provides IPreGamePluginService with CannonTurretPlugin;
}