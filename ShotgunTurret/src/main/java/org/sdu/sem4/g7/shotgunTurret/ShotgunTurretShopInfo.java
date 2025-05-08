package org.sdu.sem4.g7.shotgunTurret;

import java.util.Objects;

import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import javafx.scene.image.Image;

public class ShotgunTurretShopInfo implements IWeaponShopInfo {

    @Override
    public String getWeaponId() {
        return "shotgun_turret";
    }

    @Override
    public String getDisplayName() {
        return "Shotgun Turret";
    }

    @Override
    public int getPrice() {
        return 3000;
    }

    @Override
    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ShotgunTurret.png")));
    }

}
