import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.machineGunTurret.MachineGunProcceser;
import org.sdu.sem4.g7.machineGunTurret.MachineGunTurret;
import org.sdu.sem4.g7.tank.parts.Turret;

module MachineGunTurret {
    requires transitive Common;
    requires transitive TankParts;

    exports org.sdu.sem4.g7.machineGunTurret to TankParts;
    provides IEntityProcessingService with MachineGunProcceser;
    provides Turret with MachineGunTurret;


    provides org.sdu.sem4.g7.common.services.IWeaponShopInfo
            with org.sdu.sem4.g7.machineGunTurret.MachineGunShopInfo;
}
