package org.sdu.sem4.g7.cannonTurret;

import javafx.scene.image.Image;
import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import java.util.Objects;

public class CannonTurretShopInfo implements IWeaponShopInfo {

    @Override
    public String getWeaponId() {
        return "cannon_turret";
    }

    @Override
    public String getDisplayName() {
        return "Cannon";
    }

    @Override
    public int getPrice() {
        return 500;
    }

    @Override
    public Image getImage() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/CannonTurret.png")));
    }

}

