package org.sdu.sem4.g7.UI.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.common.services.IInventoryService;
import org.sdu.sem4.g7.common.services.ISettingPluginService;
import org.sdu.sem4.g7.common.services.ITurretProviderService;

import java.io.IOException;

import org.sdu.sem4.g7.common.aware.IGameDataAware;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.data.SettingGroup;
import org.sdu.sem4.g7.common.enums.SoundType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class MainMenuController implements Initializable {

    @FXML private Button upgradeTabBtn, shopTabBtn;
    @FXML private ImageView coinIcon, levelIcon;
    @FXML private Label coinDisplay, levelDisplay;
    @FXML private AnchorPane settingsPane, upgradePane, shopPane;
    @FXML private ImageView backgroundImage;
    @FXML private Parent mainMenuPane;
    private Stage stage;
    private GameData gameData = new GameData();

    // Health upgrade UI
    @FXML private ImageView healthIcon;
    @FXML private VBox healthUpgradeBox;
    @FXML private Button healthPriceButton;
    @FXML private Label healthUpgradeText;
    @FXML private Circle healthCircle1, healthCircle2, healthCircle3, healthCircle4, healthCircle5;

    // Shield UI
    @FXML private ImageView shieldIcon;
    @FXML private VBox shieldUpgradeBox;
    @FXML private Button shieldPriceButton;
    @FXML private Label shieldUpgradeText;
    @FXML private Circle shieldCircle1, shieldCircle2, shieldCircle3, shieldCircle4, shieldCircle5;

    // Speed UI
    @FXML private ImageView speedIcon;
    @FXML private VBox speedUpgradeBox;
    @FXML private Button speedPriceButton;
    @FXML private Label speedUpgradeText;
    @FXML private Circle speedCircle1, speedCircle2, speedCircle3, speedCircle4, speedCircle5;

    // Damage UI
    @FXML private ImageView damageIcon;
    @FXML private VBox damageUpgradeBox;
    @FXML private Button damagePriceButton;
    @FXML private Label damageUpgradeText;
    @FXML private Circle damageCircle1, damageCircle2, damageCircle3, damageCircle4, damageCircle5;

    // Armor UI
    @FXML private ImageView armorIcon;
    @FXML private VBox armorUpgradeBox;
    @FXML private Button armorPriceButton;
    @FXML private Label armorUpgradeText;
    @FXML private Circle armorCircle1, armorCircle2, armorCircle3, armorCircle4, armorCircle5;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.setImage(new Image(getClass().getResource("/images/MainMenuBackground.png").toExternalForm()));
        coinIcon.setImage(new Image(getClass().getResource("/images/dollar.png").toExternalForm()));
        levelIcon.setImage(new Image(getClass().getResource("/images/star.png").toExternalForm()));
        healthIcon.setImage(new Image(getClass().getResource("/images/heart.png").toExternalForm()));
        shieldIcon.setImage(new Image(getClass().getResource("/images/shield.png").toExternalForm()));
        speedIcon.setImage(new Image(getClass().getResource("/images/speed.png").toExternalForm()));
        damageIcon.setImage(new Image(getClass().getResource("/images/damage.png").toExternalForm()));
        armorIcon.setImage(new Image(getClass().getResource("/images/armor.png").toExternalForm()));
      
        gameData = new GameData();

        // Load gamedata awares/consumers
        ServiceLoader.load(IGameDataAware.class).forEach(aware -> {
            aware.initGameData(gameData);
        });

        setupShieldUpgrade();
        setupHealthUpgrade();
        setupSpeedUpgrade();
        setupDamageUpgrade();
        setupArmorUpgrade();

        setupSettingsPane();

        ServiceLocator.getCurrencyService().ifPresentOrElse(
                service -> coinDisplay.setText("Coins: " + service.getCurrency()),
                () -> coinDisplay.setVisible(false)
        );

        ServiceLocator.getLevelService().ifPresentOrElse(
                service -> levelDisplay.setText("Level: " + service.getLevel()),
                () -> levelDisplay.setVisible(false)
        );


        ServiceLocator.getPersistenceService().ifPresentOrElse(
            service -> gameData.setPersistenceService(service),
            () -> System.out.println("No persistence service found!")
        );

        ServiceLocator.getAudioProcessingService().ifPresentOrElse(
                service -> {
                    gameData.setAudioProcessingService(service);
                },
                () -> System.out.println("AudioProcessingService not found")

        );

        ServiceLoader.load(ITurretProviderService.class).forEach(service -> {
            ServiceLoader.load(IInventoryService.class).forEach(inventory -> {
                service.getTurrets().forEach(turret -> {
                    inventory.add(turret.get());
                    System.out.println("Loaded turret: " + turret.get().getClass().getName());
                });
            });
        });
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        ServiceLocator.getMissionLoaderService().ifPresentOrElse(
                service -> {
                    try {
                        // Set up the game data so it has a mission loader
                        gameData.setMissionLoaderService(service.getClass().getConstructor().newInstance());
                        System.out.println(gameData.getMissionLoaderService());
                        gameData.getMissionLoaderService().insert(gameData, null);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MissionSelector.fxml"));
                        Parent missionSelectorPane = loader.load();
                        
                        MissionSelectorController controller = loader.getController();
                        controller.init(gameData);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        controller.setStage(stage);
                        gameData.setPrimaryStage(stage);
            
                        Scene missionSelectorScene = new Scene(missionSelectorPane);
                        stage.setScene(missionSelectorScene);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                () -> System.out.println("MissionLoaderService not found")
        );
    }
   

    @FXML private void showUpgrades() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        upgradePane.setVisible(true);
        shopPane.setVisible(false);
    }

    @FXML private void showShop() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        upgradePane.setVisible(false);
        shopPane.setVisible(true);
    }

    @FXML private void handleBack(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        loadScene(event, "/view/Intro.fxml", "Main Menu");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setScene(Stage stage) {
        this.stage = stage;
    }


    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }


    @FXML private void handleSettings(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        settingsPane.setOpacity(0);
        settingsPane.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), settingsPane);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML private void handleCloseSettings(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        settingsPane.setVisible(false);
    }

    @FXML
    private void handleDifficulty(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);
    }

    // Health
    private void setupHealthUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getHealthLevel();
            updateHealthCircles(level);

            if (upgradeService.isHealthMaxed()) {
                healthPriceButton.setText("MAX");
                healthPriceButton.setDisable(true);
            } else {
                healthPriceButton.setText(upgradeService.getHealthUpgradePrice() + "$");
            }
        });
    }

    @FXML
    private void handleHealthUpgrade() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            if (upgradeService.isHealthMaxed()) {
                healthPriceButton.setText("MAX");
                healthPriceButton.setDisable(true);
                return;
            }

            if (upgradeService.upgradeHealth()) {
                int newLevel = upgradeService.getHealthLevel();
                updateHealthCircles(newLevel);

                if (upgradeService.isHealthMaxed()) {
                    healthPriceButton.setText("MAX");
                    healthPriceButton.setDisable(true);
                } else {
                    healthPriceButton.setText(upgradeService.getHealthUpgradePrice() + "$");
                }

                ServiceLocator.getCurrencyService().ifPresent(service ->
                        coinDisplay.setText("Coins: " + service.getCurrency())
                );
            } else {
                System.out.println("Not enough coins!");
            }
        });
    }

    private void updateHealthCircles(int level) {
        Circle[] circles = {healthCircle1, healthCircle2, healthCircle3, healthCircle4, healthCircle5};
        for (int i = 0; i < circles.length; i++) {
            circles[i].getStyleClass().removeAll("filled", "empty");
            circles[i].getStyleClass().add(i < level ? "filled" : "empty");
        }
    }

    // Shield
    private void setupShieldUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getShieldLevel();
            updateShieldCircles(level);

            if (upgradeService.isShieldMaxed()) {
                shieldPriceButton.setText("MAX");
                shieldPriceButton.setDisable(true);
            } else {
                shieldPriceButton.setText(upgradeService.getShieldUpgradePrice() + "$");
            }
        });
    }

    @FXML
    private void handleShieldUpgrade() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            if (upgradeService.isShieldMaxed()) {
                shieldPriceButton.setText("MAX");
                shieldCircle1.setDisable(true);
                return;
            }

            if (upgradeService.upgradeShield()) {
                int newLevel = upgradeService.getShieldLevel();
                updateShieldCircles(newLevel);

                if (upgradeService.isShieldMaxed()) {
                    shieldPriceButton.setText("MAX");
                    shieldPriceButton.setDisable(true);
                } else {
                    shieldPriceButton.setText(upgradeService.getShieldUpgradePrice() + "$");
                }

                ServiceLocator.getCurrencyService().ifPresent(service ->
                        coinDisplay.setText("Coins: " + service.getCurrency())
                );
            } else {
                System.out.println("Not enough coins!");
            }
        });
    }

    private void updateShieldCircles(int level) {
        Circle[] circles = {shieldCircle1, shieldCircle2, shieldCircle3, shieldCircle4, shieldCircle5};
        for (int i = 0; i < circles.length; i++) {
            circles[i].getStyleClass().removeAll("filled", "empty");
            circles[i].getStyleClass().add(i < level ? "filled" : "empty");
        }
    }


    // Speed
    public void handleSpeedUpgrade(ActionEvent actionEvent) {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            if (upgradeService.isSpeedMaxed()) {
                speedPriceButton.setText("MAX");
                speedCircle1.setDisable(true);
                return;
            }

            if (upgradeService.upgradeSpeed()) {
                int newLevel = upgradeService.getSpeedLevel();
                updateSpeedCircles(newLevel);

                if (upgradeService.isSpeedMaxed()) {
                    speedPriceButton.setText("MAX");
                    speedPriceButton.setDisable(true);
                } else {
                    speedPriceButton.setText(upgradeService.getSpeedUpgradePrice() + "$");
                }

                ServiceLocator.getCurrencyService().ifPresent(service ->
                        coinDisplay.setText("Coins: " + service.getCurrency())
                );
            } else {
                System.out.println("Not enough coins!");
            }
        });
    }

    private void setupSpeedUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getSpeedLevel();
            updateSpeedCircles(level);

            if (upgradeService.isSpeedMaxed()) {
                speedPriceButton.setText("MAX");
                speedPriceButton.setDisable(true);
            } else {
                speedPriceButton.setText(upgradeService.getSpeedUpgradePrice() + "$");
            }
        });
    }

    private void updateSpeedCircles(int level) {
        Circle[] circles = {speedCircle1, speedCircle2, speedCircle3, speedCircle4, speedCircle5};
        for (int i = 0; i < circles.length; i++) {
            circles[i].getStyleClass().removeAll("filled", "empty");
            circles[i].getStyleClass().add(i < level ? "filled" : "empty");
        }
    }


    private void setupSettingsPane() {
        // Create an empty list to store the to be loaded groups
        List<SettingGroup> settingGroups = new ArrayList<>();
        // Load them through the service loader
        for(ISettingPluginService settingPlugin: ServiceLoader.load(ISettingPluginService.class).stream().map(ServiceLoader.Provider::get).collect(Collectors.toList())) {
            settingPlugin.addSettings(settingGroups);
        }
        // Iterate through the loaded groups and add them to the settings pane
        for (SettingGroup settingGroup : settingGroups) {
            // Load SettingGroup.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SettingGroup.fxml"));
            TitledPane settingGroupPane = null;
            try {
                // Load the setting group FXML file
                settingGroupPane = loader.load();
                settingGroupPane.setText(settingGroup.getName());
                settingGroupPane.setExpanded(true); // Might as well keep it expanded for now as there are not enough settings

                // Set the on click to play a sound
                settingGroupPane.setOnMouseClicked(e -> {
                    gameData.playAudio(SoundType.BUTTON_CLICK);
                });

                // TODO: Description label
                // Label groupDescriptionLabel = (Label) settingGroupPane.lookup("#groupDescription");
                // groupDescriptionLabel.setText(settingGroup.getDescription());

                // Find the container for the groups
                VBox settingsVbox = (VBox) settingGroupPane.getContent().lookup("#settingsVbox");

                // Iterate through the settings and add them to the group
                for (Setting setting : settingGroup.getSettings()) {
                    spawnSetting(setting, settingsVbox);
                }

                // Add the setting group to the settings pane
                ((VBox)settingsPane.lookup("#settingsVbox")).getChildren().add(settingGroupPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void spawnSetting(Setting setting, VBox parent) {
        System.out.println("Setting: " + setting.getName() + " - " + setting.getDescription());
            
        // Load Setting.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Setting.fxml"));
        Pane settingPane = null;
        try {
            // Load the setting FXML file
            settingPane = loader.load();
            Label settingLabel = (Label) settingPane.lookup("#settingName");
            settingLabel.setText(setting.getName());
            Label settingDescription = (Label) settingPane.lookup("#settingDescription");
            settingDescription.setText(setting.getDescription());

            // Set the value of the setting
            HBox settingValue = (HBox) settingPane.lookup("#settingValue");

            // Set up the input based on the type of setting
            if (setting.getValueTypeClass() == Boolean.class) {
                // Create a toggle button
                Button toggleButton = new Button();
                toggleButton.setText((Boolean) setting.getValue() ? "ON" : "OFF");
                toggleButton.setOnAction(e -> {
                    boolean newValue = !((Boolean) setting.getValue());
                    setting.setValue(newValue);
                    toggleButton.setText(newValue ? "ON" : "OFF");
                    gameData.playAudio(SoundType.BUTTON_CLICK);
                    setting.apply(gameData);
                });
                settingValue.getChildren().add(toggleButton);
            } else if (setting.getValueTypeClass() == Integer.class) {
            } else if (setting.getValueTypeClass() == Float.class) {
                // Create slider with 0.1 step
                Slider slider = new Slider(0, 1, (Float) setting.getValue());
                slider.setBlockIncrement(0.1f);
                slider.setMajorTickUnit(1);
                slider.setMinorTickCount(9);
                slider.setSnapToTicks(true);
                slider.setShowTickMarks(true);
                // Create a label to show the value of the slider
                Label sliderValueLabel = new Label(String.valueOf((Float) setting.getValue()));
                sliderValueLabel.setText(String.valueOf((Float) setting.getValue()));
                // Add a listener to the slider to update the setting value
                slider.valueProperty().addListener((obs, oldValue, newValue) -> {
                    // Snap to 0.1
                    float snappedValue = Math.round(newValue.floatValue() * 10) / 10f;
                    slider.setValue(snappedValue);
                    // If change is above 0.1
                    if (Math.abs(snappedValue - (Float) setting.getValue()) >= 0.05f) {
                        sliderValueLabel.setText(String.valueOf(snappedValue));
                        setting.setValue(snappedValue);
                        setting.apply(gameData);
                        gameData.playAudio(SoundType.BUTTON_CLICK);
                    }
                });

                // Add the slider and label to the setting value HBox
                HBox.setHgrow(slider, javafx.scene.layout.Priority.ALWAYS);
                settingValue.getChildren().add(slider);
                settingValue.getChildren().add(sliderValueLabel);
            }
            System.out.println("Adding to parent");
            // Get #settingsVbox
            parent.getChildren().add(settingPane);
            // Apply the setting as it may have a saved value
            setting.apply(gameData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Damage
    public void handleDamageUpgrade(ActionEvent actionEvent) {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            gameData.playAudio(SoundType.BUTTON_CLICK);
            if (upgradeService.isDamageMaxed()) {
                damagePriceButton.setText("MAX");
                damageCircle1.setDisable(true);
                return;
            }

            if (upgradeService.upgradeDamage()) {
                int newLevel = upgradeService.getDamageLevel();
                updateDamageCircles(newLevel);

                if (upgradeService.isDamageMaxed()) {
                    damagePriceButton.setText("MAX");
                    damagePriceButton.setDisable(true);
                } else {
                    damagePriceButton.setText(upgradeService.getDamageUpgradePrice() + "$");
                }

                ServiceLocator.getCurrencyService().ifPresent(service ->
                        coinDisplay.setText("Coins: " + service.getCurrency())
                );
            } else {
                System.out.println("Not enough coins!");
            }
        });
    }

    private void setupDamageUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getDamageLevel();
            updateDamageCircles(level);

            if (upgradeService.isDamageMaxed()) {
                damagePriceButton.setText("MAX");
                damagePriceButton.setDisable(true);
            } else {
                damagePriceButton.setText(upgradeService.getDamageUpgradePrice() + "$");
            }
        });
    }

    private void updateDamageCircles(int level) {
        Circle[] circles = {damageCircle1, damageCircle2, damageCircle3, damageCircle4, damageCircle5};
        for (int i = 0; i < circles.length; i++) {
            circles[i].getStyleClass().removeAll("filled", "empty");
            circles[i].getStyleClass().add(i < level ? "filled" : "empty");
        }
    }

    // Armor
    public void handleArmorUpgrade(ActionEvent actionEvent) {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            gameData.playAudio(SoundType.BUTTON_CLICK);
            if (upgradeService.isArmorMaxed()) {
                armorPriceButton.setText("MAX");
                armorCircle1.setDisable(true);
                return;
            }

            if (upgradeService.upgradeArmor()) {
                int newLevel = upgradeService.getArmorLevel();
                updateArmorCircles(newLevel);

                if (upgradeService.isArmorMaxed()) {
                    armorPriceButton.setText("MAX");
                    armorPriceButton.setDisable(true);
                } else {
                    armorPriceButton.setText(upgradeService.getArmorUpgradePrice() + "$");
                }

                ServiceLocator.getCurrencyService().ifPresent(service ->
                        coinDisplay.setText("Coins: " + service.getCurrency())
                );
            } else {
                System.out.println("Not enough coins!");
            }
        });
    }

    private void setupArmorUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getArmorLevel();
            updateArmorCircles(level);

            if (upgradeService.isArmorMaxed()) {
                armorPriceButton.setText("MAX");
                armorPriceButton.setDisable(true);
            } else {
                armorPriceButton.setText(upgradeService.getArmorUpgradePrice() + "$");
            }
        });
    }

    private void updateArmorCircles(int level) {
        Circle[] circles = {armorCircle1, armorCircle2, armorCircle3, armorCircle4, armorCircle5};
        for (int i = 0; i < circles.length; i++) {
            circles[i].getStyleClass().removeAll("filled", "empty");
            circles[i].getStyleClass().add(i < level ? "filled" : "empty");
        }
    }

}
