import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.machineGunTurret.MachineGunProcceser;
import org.sdu.sem4.g7.machineGunTurret.MachineGunTurret;
import org.sdu.sem4.g7.tank.parts.Turret;

module MachineGunTurret {
    requires transitive Common;
    requires transitive TankParts;

    provides IEntityProcessingService with MachineGunProcceser;
    provides Turret with MachineGunTurret;
}
