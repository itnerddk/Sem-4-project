package org.sdu.sem4.g7.UI.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.sdu.sem4.g7.common.services.IInventoryService;
import org.sdu.sem4.g7.common.services.IWeaponInstance;
import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public class WeaponSelectorController {

    @FXML
    private VBox weaponBox;

    public void init() {
        weaponBox.getChildren().clear();

        Optional<IInventoryService> inventoryOpt = ServiceLoader.load(IInventoryService.class).findFirst();
        if (inventoryOpt.isEmpty()) {
            System.out.println("Inventory not found.");
            return;
        }

        IInventoryService inventory = inventoryOpt.get();
        List<IWeaponInstance> weapons = inventory.getAllOwnedTurrets();
        IWeaponInstance selected = inventory.getCurrentTurret();

        for (IWeaponInstance weapon : weapons) {
            ImageView icon = getIconForWeapon(weapon.getWeaponId());
            icon.setFitWidth(64);
            icon.setFitHeight(64);
            icon.getStyleClass().add("weapon-icon");

            if (weapon.equals(selected)) {
                icon.getStyleClass().add("selected");
            }

            weaponBox.getChildren().add(icon);
        }
    }

    private ImageView getIconForWeapon(String weaponId) {
        return ServiceLoader.load(IWeaponShopInfo.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(info -> info.getWeaponId().equals(weaponId))
                .findFirst()
                .map(info -> new ImageView(info.getImage()))
                .orElse(new ImageView());
    }
}
