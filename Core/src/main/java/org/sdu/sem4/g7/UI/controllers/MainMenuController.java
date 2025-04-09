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

import org.sdu.sem4.g7.common.services.ISettingPluginService;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import javafx.scene.control.Label;

import java.io.IOException;

import org.sdu.sem4.g7.MissionLoader.services.MissionLoaderService;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.data.SettingGroup;

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
    private GameData gameData;


    // Health upgrade UI
    @FXML private ImageView healthIcon;
    @FXML private VBox healthUpgradeBox;
    @FXML private Button healthPriceButton;
    @FXML private Label healthUpgradeText;
    @FXML private Circle healthCircle1, healthCircle2, healthCircle3, healthCircle4, healthCircle5;

    // Armor UI
    @FXML private ImageView armorIcon;
    @FXML private VBox armorUpgradeBox;
    @FXML private Button armorPriceButton;
    @FXML private Label armorUpgradeText;
    @FXML private Circle armorCircle1, armorCircle2, armorCircle3, armorCircle4, armorCircle5;

    // Speed UI
    @FXML private ImageView speedIcon;
    @FXML private VBox speedUpgradeBox;
    @FXML private Button speedPriceButton;
    @FXML private Label speedUpgradeText;
    @FXML private Circle speedCircle1, speedCircle2, speedCircle3, speedCircle4, speedCircle5;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backgroundImage.setImage(new Image(getClass().getResource("/images/MainMenuBackground.png").toExternalForm()));
        coinIcon.setImage(new Image(getClass().getResource("/images/dollar.png").toExternalForm()));
        levelIcon.setImage(new Image(getClass().getResource("/images/star.png").toExternalForm()));
        healthIcon.setImage(new Image(getClass().getResource("/images/heart.png").toExternalForm()));
        armorIcon.setImage(new Image(getClass().getResource("/images/armor.png").toExternalForm()));
        speedIcon.setImage(new Image(getClass().getResource("/images/speed.png").toExternalForm()));
        gameData = new GameData();

        setupArmorUpgrade();
        setupHealthUpgrade();
        setupSpeedUpgrade();

        setupSettingsPane();

        ServiceLocator.getCurrencyService().ifPresentOrElse(
                service -> coinDisplay.setText("Coins: " + service.getCurrency()),
                () -> coinDisplay.setVisible(false)
        );

        ServiceLocator.getLevelService().ifPresentOrElse(
                service -> levelDisplay.setText("Level: " + service.getLevel()),
                () -> levelDisplay.setVisible(false)
        );
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        try {
            // Set up the game data so it has a mission loader
            MissionLoaderService missionLoader = new MissionLoaderService(gameData, null);
            gameData.setMissionLoaderService(missionLoader);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MissionSelector.fxml"));
            Parent missionSelectorPane = loader.load();
            
            MissionSelectorController controller = loader.getController();
            controller.init(gameData);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controller.setStage(stage);

            Scene missionSelectorScene = new Scene(missionSelectorPane);
            stage.setScene(missionSelectorScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   

    @FXML private void showUpgrades() {
        upgradePane.setVisible(true);
        shopPane.setVisible(false);
    }

    @FXML private void showShop() {
        upgradePane.setVisible(false);
        shopPane.setVisible(true);
    }

    @FXML private void handleBack(ActionEvent event) {
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
        settingsPane.setOpacity(0);
        settingsPane.setVisible(true);
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.3), settingsPane);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    @FXML private void handleCloseSettings(ActionEvent event) {
        settingsPane.setVisible(false);
    }

    @FXML
    private void handleDifficulty(ActionEvent event) {
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

    //Armor
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

    @FXML
    private void handleArmorUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
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

    private void updateArmorCircles(int level) {
        Circle[] circles = {armorCircle1, armorCircle2, armorCircle3, armorCircle4, armorCircle5};
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
                    setting.apply(gameData);
                });
                settingValue.getChildren().add(toggleButton);
            } else if (setting.getValueTypeClass() == Integer.class) {
            } else if (setting.getValueTypeClass() == Float.class) {
                // Create slider with 0.1 step
                Slider slider = new Slider(0, 1, (Float) setting.getValue());
                slider.setBlockIncrement(0.1);
                slider.setMajorTickUnit(1);
                slider.setMinorTickCount(9);
                slider.setSnapToTicks(true);
                slider.setShowTickMarks(true);
                // Create a label to show the value of the slider
                Label sliderValueLabel = new Label(String.valueOf((Float) setting.getValue()));
                sliderValueLabel.setText(String.valueOf((Float) setting.getValue()));
                // Add a listener to the slider to update the setting value
                slider.valueProperty().addListener((obs, oldValue, newValue) -> {
                    setting.setValue(newValue.floatValue());
                    sliderValueLabel.setText(String.valueOf(Math.round(newValue.floatValue() * 10.0f) / 10.0f));
                    setting.apply(gameData);
                });
                // Add the slider and label to the setting value HBox
                HBox.setHgrow(slider, javafx.scene.layout.Priority.ALWAYS);
                settingValue.getChildren().add(slider);
                settingValue.getChildren().add(sliderValueLabel);
            }
            System.out.println("Adding to parent");
            // Get #settingsVbox
            parent.getChildren().add(settingPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
