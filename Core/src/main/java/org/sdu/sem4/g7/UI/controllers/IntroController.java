package org.sdu.sem4.g7.UI.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;

import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ServiceLocator;

import javafx.animation.PauseTransition;
import javafx.util.Duration;



public class IntroController implements Initializable {

    @FXML
    private ImageView gifView;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button loadSaveButton;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Background image
        Image img = new Image(getClass().getResource("/images/intro_image.png").toExternalForm());
        backgroundImage.setImage(img);

        // Logo Intro GIF
        Image logoGif = new Image(getClass().getResource("/images/test.gif").toExternalForm());
        Image LogoPng = new Image(getClass().getResource("/images/test.png").toExternalForm());
        gifView.setImage(logoGif);
        // Replace after 3 seconds
        PauseTransition delay = new PauseTransition(Duration.seconds(1.5));
        delay.setOnFinished(event -> {
            gifView.setImage(LogoPng);
        });
        delay.play();

        // If there is no persistence service or no saves, disable the load save button
        ServiceLocator.getPersistenceService().ifPresentOrElse(
            persistenceService -> {
                Function<Integer, Boolean> ex = persistenceService::fileExists; // Done to shorten code
                if (ex.apply(1) || ex.apply(2) || ex.apply(3)) {
                    loadSaveButton.setDisable(false);
                } else {
                    loadSaveButton.setDisable(true);
                }
            },
            () -> {
                loadSaveButton.setDisable(true);
            }    
        );
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), rootPane);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            try {
                // Load MainMenu.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
                Parent mainMenuRoot = loader.load();

                // Skift scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(mainMenuRoot);
                stage.setScene(scene);
                stage.setTitle("Tank Wars - Main Menu");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        fadeOut.play();
    }

    public void handleCredits(ActionEvent actionEvent) {
        playSound();
    }

    public void handleQuitGame(ActionEvent actionEvent) {
        playSound();
        System.exit(0);
    }

    @FXML
    private AnchorPane savePane;

    private HashMap<Integer, HashMap<String, Object>> getSaves() {
        HashMap<Integer, HashMap<String, Object>> saves = new HashMap<>();
        ServiceLocator.getPersistenceService().ifPresent(
            persistenceService -> {
                for (int i = 1; i <= 3; i++) {
                    HashMap<String, Object> save = new HashMap<>();
                    saves.put(i, save);
                    save.put("name", "Save " + i);
                    // If the file id is not set continue instead of trying to put values in the map.
                    if (!persistenceService.fileExists(i)) continue;
                    persistenceService.setFileId(i);
                    save.put("valid", true);
                    ServiceLocator.getLevelService().ifPresent(levelService -> {save.put("level", levelService.getLevel());});
                    ServiceLocator.getCurrencyService().ifPresent(currencyService -> {save.put("currency", currencyService.getCurrency());});
                    if (persistenceService.intListExists("completedMissions")) {
                        save.put("completedMissions", persistenceService.getIntList("completedMissions").size());
                    } else {
                        save.put("completedMissions", 0);
                    }
                }
            }
        );
        return saves;
    }

    private void spawnSave(int saveId, HashMap<String, Object> save, EventHandler<? super MouseEvent> onClick) {
        StackPane saveButton;
        if (save.get("valid") == null) {
            // This means new save
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NewSaveButton.fxml"));
            try {
                saveButton = loader.load();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            // Find the save name label
            Label saveLabel = (Label) saveButton.lookup("#saveName");
            saveLabel.setText(String.format(saveLabel.getText(), String.valueOf(saveId)));
        } else {
            // This means not a new save
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SaveButton.fxml"));
            try {
                saveButton = loader.load();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            // Find the save name label
            Label saveLabel = (Label) saveButton.lookup("#saveName");
            saveLabel.setText(String.format(saveLabel.getText(), String.valueOf(saveId)));

            // Find the save level label
            Label saveLevelLabel = (Label) saveButton.lookup("#saveLevel");
            saveLevelLabel.setText(String.format(saveLevelLabel.getText(), String.valueOf(save.get("level"))));

            // Find the save currency label
            Label saveCurrencyLabel = (Label) saveButton.lookup("#saveCurrency");
            saveCurrencyLabel.setText(String.format(saveCurrencyLabel.getText(), String.valueOf(save.get("currency"))));

            // Find the save completed missions label
            Label saveCompletedMissionsLabel = (Label) saveButton.lookup("#saveCompletedMissions");
            saveCompletedMissionsLabel.setText(String.format(saveCompletedMissionsLabel.getText(), String.valueOf(save.get("completedMissions"))));

        }

        saveButton.setOnMouseClicked(onClick);

        ((VBox) savePane.lookup("#savesVbox")).getChildren().add(saveButton);
    }

    @FXML
    public void newSave(ActionEvent actionEvent) {
        playSound();
        ((Label) savePane.lookup("#title")).setText("New Save");
        // If the loader is present that means saving is a thing.
        HashMap<Integer, HashMap<String, Object>> saves = getSaves();
        if (saves.size() == 0) {
            // No saves
            handleStartGame(actionEvent);
            return;
        }

        for (Map.Entry<Integer, HashMap<String, Object>> entry : saves.entrySet()) {
            int saveId = entry.getKey();
            HashMap<String, Object> save = entry.getValue();
            spawnSave(saveId, save, e -> {
                playSound();
                // Load the save
                ServiceLocator.getPersistenceService().ifPresent(persistenceService -> {
                    if (persistenceService.fileExists(saveId)) persistenceService.deleteFileId(saveId);
                    persistenceService.setFileId(saveId);
                });
                handleStartGame(actionEvent);
            });
        }

        savePane.setVisible(true);

        return;
    }

    
    @FXML
    public void loadSave(ActionEvent actionEvent) {
        playSound();
        ((Label) savePane.lookup("#title")).setText("New Save");
        // Should be possible to load a save. They should be unable to get here if the init works correctly.
        HashMap<Integer, HashMap<String, Object>> saves = getSaves();

        for (Map.Entry<Integer, HashMap<String, Object>> entry : saves.entrySet()) {
            int saveId = entry.getKey();
            HashMap<String, Object> save = entry.getValue();
            if (save.get("valid") == null) continue;
            spawnSave(saveId, save, e -> {
                playSound();
                // Load the save
                ServiceLocator.getPersistenceService().ifPresent(persistenceService -> {
                    if (persistenceService.fileExists(saveId)) persistenceService.setFileId(saveId);
                });
                handleStartGame(actionEvent);
            });
        }

        savePane.setVisible(true);

        return;
    }

    @FXML
    public void handleCloseSaves(ActionEvent actionEvent) {
        playSound();
        VBox vbox = (VBox) savePane.lookup("#savesVbox");
        // Remove all children except the title
        for (int i = vbox.getChildren().size() - 1; i > 0; i--) {
            vbox.getChildren().remove(i);
        }
        
        savePane.setVisible(false);
    }

    /**
     * Helper function to play sound
     */
    private void playSound() {
        ServiceLocator.getAudioProcessingService().ifPresent(audioProcessingService -> {
            audioProcessingService.playSound(SoundType.BUTTON_CLICK, 1f);
        });
    }
}
