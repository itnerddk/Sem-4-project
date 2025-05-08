package org.sdu.sem4.g7.bounceTurret;

import java.util.Objects;

import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import javafx.scene.image.Image;

public class BounceTurretShopInfo implements IWeaponShopInfo {

    @Override
    public String getWeaponId() {
        return "bounce_turret";
    }

    @Override
    public String getDisplayName() {
        return "Bounce Gun";
    }

    @Override
    public int getPrice() {
        return 7500;
    }

    @Override
    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/BounceTurret.png")));
    }
}
