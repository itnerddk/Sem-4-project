package org.sdu.sem4.g7.machineGunTurret;

import javafx.scene.image.Image;
import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import java.util.Objects;

public class MachineGunShopInfo implements IWeaponShopInfo {

    @Override
    public String getWeaponId() {
        return "machine_gun";
    }

    @Override
    public String getDisplayName() {
        return "Machine Gun";
    }

    @Override
    public int getPrice() {
        return 750;
    }

    @Override
    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/MachineGunTurret.png")));
    }
}
