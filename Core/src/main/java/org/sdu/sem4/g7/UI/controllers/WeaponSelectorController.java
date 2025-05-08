package org.sdu.sem4.g7.UI.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.sdu.sem4.g7.common.services.IInventoryService;
import org.sdu.sem4.g7.common.services.IWeaponInstance;
import org.sdu.sem4.g7.common.services.IWeaponShopInfo;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;

public class WeaponSelectorController {

    @FXML
    private HBox weaponBox;

    private List<IWeaponInstance> weapons;

    public void init() {
        weaponBox.setAlignment(Pos.CENTER);
        Optional<IInventoryService> inventoryOpt = ServiceLoader.load(IInventoryService.class).findFirst();
        if (inventoryOpt.isEmpty()) {
            System.out.println("Inventory not found.");
            return;
        }

        IInventoryService inventory = inventoryOpt.get();
        weapons = inventory.getAllOwnedTurrets();
        IWeaponInstance selected = inventory.getCurrentTurret();

        renderIcons(selected);
    }

    private void renderIcons(IWeaponInstance selected) {
        weaponBox.getChildren().clear();

        int selectedIndex = weapons.indexOf(selected);
        int total = weapons.size();

        if (total == 0 || selectedIndex == -1) return;

        int prevIndex = (selectedIndex - 1 + total) % total;
        int nextIndex = (selectedIndex + 1) % total;

        weaponBox.getChildren().clear();
        weaponBox.getChildren().add(createWeaponBox(weapons.get(prevIndex), "side"));
        weaponBox.getChildren().add(createWeaponBox(weapons.get(selectedIndex), "center"));
        weaponBox.getChildren().add(createWeaponBox(weapons.get(nextIndex), "side"));

    }

    private StackPane createWeaponBox(IWeaponInstance weapon, String styleClass) {
        ImageView icon = getIconForWeapon(weapon.getWeaponId());
        icon.setFitWidth(64);
        icon.setFitHeight(64);

        String name = getWeaponDisplayName(weapon.getWeaponId());
        javafx.scene.control.Label label = new javafx.scene.control.Label(name);
        label.getStyleClass().add("weapon-label");

        int size = 70;

        VBox content = new VBox(icon, label);
        content.setAlignment(Pos.CENTER);
        content.setSpacing(-5);
        content.setMaxSize(size, size);
        content.setPrefSize(size, size);
        content.setMinSize(size, size);

        StackPane iconBox = new StackPane(content);
        iconBox.getStyleClass().addAll("weapon-icon-box", styleClass);
        iconBox.setPrefSize(size, size);
        iconBox.setMaxSize(size, size);

        return iconBox;
    }


    private ImageView getIconForWeapon(String weaponId) {
        return ServiceLoader.load(IWeaponShopInfo.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(info -> info.getWeaponId().equals(weaponId))
                .findFirst()
                .map(info -> new ImageView(info.getImage()))
                .orElse(new ImageView());
    }

    private String getWeaponDisplayName(String weaponId) {
        return ServiceLoader.load(IWeaponShopInfo.class).stream()
                .map(ServiceLoader.Provider::get)
                .filter(info -> info.getWeaponId().equals(weaponId))
                .map(IWeaponShopInfo::getDisplayName)
                .findFirst()
                .orElse("Unknown");
    }



}
