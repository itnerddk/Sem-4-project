package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.main.GameInstance;

public class PauseMenuController implements Initializable {
    private GameInstance gameInstance;
    private GameData gameData;
    private int missionId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setGameInstance(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    @FXML
    private void handleResumeGame(ActionEvent event) {
        ServiceLocator.getAudioProcessingService().ifPresent(
            service -> {
                service.playSound(SoundType.BUTTON_CLICK, 1.0f);
            }
        );
        gameInstance.resumeGame();
    }

    @FXML
    private void handleRestartGame(ActionEvent event) {
        ServiceLocator.getAudioProcessingService().ifPresent(
            service -> {
                service.playSound(SoundType.BUTTON_CLICK, 1.0f);
            }
        );
        WorldData worldData = gameData.getMissionLoaderService().loadMission(missionId);
        GameInstance game = new GameInstance(gameData, worldData, missionId);
        Stage gameStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        gameStage.setScene(game.getScene());
    }

    @FXML
    private void handleMainMenu(ActionEvent event) {
        try {
            ServiceLocator.getAudioProcessingService().ifPresent(
                service -> {
                    service.playSound(SoundType.BUTTON_CLICK, 1.0f);
                }
            );
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
