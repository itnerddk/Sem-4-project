package org.sdu.sem4.g7.UI.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.services.*;
import java.io.IOException;

import org.sdu.sem4.g7.common.aware.IGameDataAware;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Setting;
import org.sdu.sem4.g7.common.data.SettingGroup;
import org.sdu.sem4.g7.common.enums.DifficultyEnum;
import org.sdu.sem4.g7.common.enums.SoundType;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.function.Supplier;

public class MainMenuController implements Initializable {

    private Stage stage;
    private GameData gameData = new GameData();

    @FXML private Parent mainMenuPane;
    @FXML private AnchorPane settingsPane, upgradePane, shopPane;
    @FXML private ImageView backgroundImage;
    @FXML private Button upgradeTabBtn, shopTabBtn;

    // Currency
    @FXML private ImageView coinIcon, levelIcon;
    @FXML private Label coinDisplay, levelDisplay;

    // Upgrade
    @FXML private AnchorPane UpgradeTapsPane;
    @FXML private Text upgradeTankText;

    // Shop
    @FXML private AnchorPane shopTapsPane;
    @FXML private ScrollPane shopScrollPane;

    // Health upgrade UI
    @FXML private ImageView healthIcon;
    @FXML private VBox healthUpgradeBox;
    @FXML private Button healthPriceButton;
    @FXML private Text healthUpgradeText;
    @FXML private ProgressBar healthProgress;
    @FXML private Text healthUpgradeLevelText;

    // Shield UI
    @FXML private ImageView shieldIcon;
    @FXML private VBox shieldUpgradeBox;
    @FXML private Button shieldPriceButton;
    @FXML private Text shieldUpgradeText;
    @FXML private ProgressBar shieldProgress;
    @FXML private Text shieldUpgradeLevelText;

    // Speed UI
    @FXML private ImageView speedIcon;
    @FXML private VBox speedUpgradeBox;
    @FXML private Button speedPriceButton;
    @FXML private Text speedUpgradeText;
    @FXML private ProgressBar speedProgress;
    @FXML private Text speedUpgradeLevelText;

    // Damage UI
    @FXML private ImageView damageIcon;
    @FXML private VBox damageUpgradeBox;
    @FXML private Button damagePriceButton;
    @FXML private Text damageUpgradeText;
    @FXML private ProgressBar damageProgress;
    @FXML private Text damageUpgradeLevelText;


    // Armor UI
    @FXML private ImageView armorIcon;
    @FXML private VBox armorUpgradeBox;
    @FXML private Button armorPriceButton;
    @FXML private Text armorUpgradeText;
    @FXML private ProgressBar armorProgress;
    @FXML private Text armorUpgradeLevelText;

    // Difficulty
    @FXML private AnchorPane difficultyPane;
    @FXML private Button difficultyEasyButton;
    @FXML private Button difficultyNormalButton;
    @FXML private Button difficultyHardButton;

    // Shop
    @FXML private GridPane weaponGrid;


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
        Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/LuckiestGuy-Regular.ttf"), 20);
        System.out.println("Loaded font: " + font.getName());

        // make non visible if module is not found
        if (ServiceLocator.getUpgradeService().isEmpty()) {
            UpgradeTapsPane.setVisible(false);
            upgradeTankText.setVisible(false);
            return;
        }

        setupShieldUpgrade();
        setupHealthUpgrade();
        setupSpeedUpgrade();
        setupDamageUpgrade();
        setupArmorUpgrade();

        ServiceLocator.getUpgradeService().ifPresent(service -> {
            updateHealthProgress(service.getHealthLevel());
            updateArmorProgress(service.getArmorLevel());
            updateDamageProgress(service.getDamageLevel());
            updateSpeedProgress(service.getSpeedLevel());
            updateShieldProgress(service.getShieldLevel());

        });

        setupSettingsPane();
        setupShopWeapons();

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
        shopScrollPane.setVisible(false);
    }

    @FXML private void showShop() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        upgradePane.setVisible(false);
        shopPane.setVisible(true);
        shopScrollPane.setVisible(true);
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


    // Difficulty
    @FXML
    private void handleDifficulty(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);

        // set all buttons to enabled
        difficultyEasyButton.setDisable(false);
        difficultyNormalButton.setDisable(false);
        difficultyHardButton.setDisable(false);

        // load difficulty service
        ServiceLocator.getDifficultyService().ifPresentOrElse(
            difficultyService -> {
                // disabled the button for the difficultylevel already set
                switch (difficultyService.getDifficulty()) {
                    case EASY:
                        difficultyEasyButton.setDisable(true);
                        break;

                    case NORMAL:
                        difficultyNormalButton.setDisable(true);
                        break;

                    case HARD:
                        difficultyHardButton.setDisable(true);
                        break;

                    default:
                        difficultyNormalButton.setDisable(true);
                        break;
                }
            },
            () -> {
                System.out.println("Could not find a DifficultyService!");
                difficultyNormalButton.setDisable(true); // default to normal selected
            }
        );

        difficultyPane.setVisible(true);
    }

    @FXML
    private void handleCloseDifficulty(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);

        difficultyPane.setVisible(false);
    }

    @FXML
    private void handleDifficultyEasy(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);

        ServiceLocator.getDifficultyService().ifPresent(
            difficultyService -> {
                difficultyService.setDifficulty(DifficultyEnum.EASY);
            }
        );

        difficultyEasyButton.setDisable(true);
        difficultyNormalButton.setDisable(false);
        difficultyHardButton.setDisable(false);
    }

    @FXML
    private void handleDifficultyNormal(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);

        ServiceLocator.getDifficultyService().ifPresent(
            difficultyService -> {
                difficultyService.setDifficulty(DifficultyEnum.NORMAL);
            }
        );

        difficultyEasyButton.setDisable(false);
        difficultyNormalButton.setDisable(true);
        difficultyHardButton.setDisable(false);
    }

    @FXML
    private void handleDifficultyHard(ActionEvent event) {
        gameData.playAudio(SoundType.BUTTON_CLICK);

        ServiceLocator.getDifficultyService().ifPresent(
            difficultyService -> {
                difficultyService.setDifficulty(DifficultyEnum.HARD);
            }
        );

        difficultyEasyButton.setDisable(false);
        difficultyNormalButton.setDisable(false);
        difficultyHardButton.setDisable(true);
    }

    // Health
    private void setupHealthUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getHealthLevel();
            updateHealthProgress(level);

            if (upgradeService.isHealthMaxed()) {
                healthPriceButton.setText("MAX");
                healthPriceButton.setDisable(true);
            } else {
                healthPriceButton.setText(upgradeService.getHealthUpgradePrice() + "$");
            }
        });
    }

    private void updateHealthProgress(int level) {
        double progress = Math.min(1.0, level / 5.0);
        healthProgress.setProgress(progress);

        healthUpgradeLevelText.setText(level + "/5");
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
                updateHealthProgress(newLevel);

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

    // Shield
    private void setupShieldUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getShieldLevel();
            updateShieldProgress(level);

            if (upgradeService.isShieldMaxed()) {
                shieldPriceButton.setText("MAX");
                shieldPriceButton.setDisable(true);
            } else {
                shieldPriceButton.setText(upgradeService.getShieldUpgradePrice() + "$");
            }
        });
    }

    private void updateShieldProgress(int level) {
        double progress = Math.min(1.0, level / 5.0);
        shieldProgress.setProgress(progress);

        shieldUpgradeLevelText.setText(level + "/5");
    }

    @FXML
    private void handleShieldUpgrade() {
        gameData.playAudio(SoundType.BUTTON_CLICK);
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            if (upgradeService.isShieldMaxed()) {
                shieldPriceButton.setText("MAX");
                shieldPriceButton.setDisable(true);
                return;
            }

            if (upgradeService.upgradeShield()) {
                int newLevel = upgradeService.getShieldLevel();
                updateShieldProgress(newLevel);

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

    // Speed
    public void handleSpeedUpgrade(ActionEvent actionEvent) {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            if (upgradeService.isSpeedMaxed()) {
                speedPriceButton.setText("MAX");
                speedPriceButton.setDisable(true);
                return;
            }

            if (upgradeService.upgradeSpeed()) {
                int newLevel = upgradeService.getSpeedLevel();
                updateSpeedProgress(newLevel);

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
            updateSpeedProgress(level);

            if (upgradeService.isSpeedMaxed()) {
                speedPriceButton.setText("MAX");
                speedPriceButton.setDisable(true);
            } else {
                speedPriceButton.setText(upgradeService.getSpeedUpgradePrice() + "$");
            }
        });
    }

    private void updateSpeedProgress(int level) {
        double progress = Math.min(1.0, level / 5.0);
        speedProgress.setProgress(progress);

        speedUpgradeLevelText.setText(level + "/5");
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
                damagePriceButton.setDisable(true);
                return;
            }

            if (upgradeService.upgradeDamage()) {
                int newLevel = upgradeService.getDamageLevel();
                updateDamageProgress(newLevel);

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

    private void updateDamageProgress(int level) {
        double progress = Math.min(1.0, level / 5.0);
        damageProgress.setProgress(progress);

        damageUpgradeLevelText.setText(level + "/5");
    }

    private void setupDamageUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getDamageLevel();
            updateDamageProgress(level);

            if (upgradeService.isDamageMaxed()) {
                damagePriceButton.setText("MAX");
                damagePriceButton.setDisable(true);
            } else {
                damagePriceButton.setText(upgradeService.getDamageUpgradePrice() + "$");
            }
        });
    }

    // Armor
    public void handleArmorUpgrade(ActionEvent actionEvent) {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            gameData.playAudio(SoundType.BUTTON_CLICK);
            if (upgradeService.isArmorMaxed()) {
                armorPriceButton.setText("MAX");
                armorPriceButton.setDisable(true);
                return;
            }

            if (upgradeService.upgradeArmor()) {
                int newLevel = upgradeService.getArmorLevel();
                updateArmorProgress(newLevel);

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

    private void updateArmorProgress(int level) {
        double progress = Math.min(1.0, level / 5.0);
        armorProgress.setProgress(progress);

        armorUpgradeLevelText.setText(level + "/5");
    }

    private void setupArmorUpgrade() {
        ServiceLocator.getUpgradeService().ifPresent(upgradeService -> {
            int level = upgradeService.getArmorLevel();
            updateArmorProgress(level);

            if (upgradeService.isArmorMaxed()) {
                armorPriceButton.setText("MAX");
                armorPriceButton.setDisable(true);
            } else {
                armorPriceButton.setText(upgradeService.getArmorUpgradePrice() + "$");
            }
        });
    }

    // Shop
    private void setupShopWeapons() {
        ServiceLocator.getBoughtWeaponsService().ifPresent(boughtService -> {
            ServiceLoader<IWeaponShopInfo> loader = ServiceLoader.load(IWeaponShopInfo.class);
            List<IWeaponShopInfo> weapons = loader.stream()
                    .map(ServiceLoader.Provider::get)
                    .sorted(Comparator.comparingInt(IWeaponShopInfo::getPrice))
                    .collect(Collectors.toList());

            weaponGrid.getChildren().clear();

            int col = 0;
            int row = 0;
            int maxCols = 4;

            for (IWeaponShopInfo weapon : weapons) {
                VBox weaponBox = createWeaponBox(weapon, boughtService);
                weaponGrid.add(weaponBox, col, row);
                col++;
                if (col >= maxCols) {
                    col = 0;
                    row++;
                }
            }
        });
    }

    private VBox createWeaponBox(IWeaponShopInfo weapon, IBoughtWeaponsService boughtService) {
        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);
        box.setPrefSize(125, 200);
        box.getStyleClass().add("weapon-box");

        ImageView icon = new ImageView(weapon.getImage());
        icon.setFitHeight(48);
        icon.setFitWidth(48);
        icon.setPreserveRatio(true);

        Label name = new Label(weapon.getDisplayName());
        name.getStyleClass().add("weapon-name");
        VBox.setMargin(name, new Insets(25, 0, 4, 0));

        Button buyButton = new Button();
        buyButton.getStyleClass().add("shop-price-button");
        VBox.setMargin(buyButton, new Insets(15, 0, 0, 0));

        boolean alreadyBought = boughtService.isWeaponBought(weapon.getWeaponId()) || weapon.getPrice() == 0;

        if (alreadyBought) {
            buyButton.setText("Owned");
            buyButton.setDisable(true);

            ServiceLocator.getInventoryService().ifPresent(inv -> {
                weaponToEntity(weapon).ifPresent(inv::add);
            });

        } else {
            buyButton.setText(weapon.getPrice() + "$");

            buyButton.setOnAction(e -> {
                ServiceLocator.getCurrencyService().ifPresent(currency -> {
                    if (currency.getCurrency() >= weapon.getPrice()) {
                        currency.subtractCurrency(weapon.getPrice());
                        boughtService.buyWeapon(weapon.getWeaponId());

                        ServiceLocator.getInventoryService().ifPresent(inv -> {
                            weaponToEntity(weapon).ifPresent(inv::add);
                            System.out.println("Added to inventory: " + weapon.getWeaponId());
                        });

                        buyButton.setText("Owned");
                        buyButton.setDisable(true);
                        coinDisplay.setText("Coins: " + currency.getCurrency());
                    }
                });
            });
        }

        box.getChildren().addAll(icon, name, buyButton);
        return box;
    }

    private Optional<Entity> weaponToEntity(IWeaponShopInfo weapon) {
        return ServiceLoader.load(ITurretProviderService.class).stream()
                .map(ServiceLoader.Provider::get)
                .flatMap(provider -> provider.getTurrets().stream())
                .map(Supplier::get)
                .map(entity -> (IWeaponInstance) entity)
                .filter(entity -> entity.getWeaponId().equals(weapon.getWeaponId()))
                .map(entity -> (Entity) entity)
                .findFirst();
    }

}
