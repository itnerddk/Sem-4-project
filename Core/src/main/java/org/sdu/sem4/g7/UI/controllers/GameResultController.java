package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class GameResultController implements Initializable {

    @FXML private Text resultText;
    @FXML private Text scoreText;
    @FXML private Text coinsText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init kaldes bagefter med data
    }

    public void init(boolean isWin, int score, int target, int coins) {
        resultText.setText(isWin ? "YOU WIN!" : "YOU LOSE!");
        scoreText.setText("Your Score: " + score);
        coinsText.setText("+" + coins + " Coins");

        // Play sound
        ServiceLocator.getAudioProcessingService().ifPresent(
            service -> {
                if (isWin) {
                    service.playSound(SoundType.GAME_WON, 1);
                } else {
                    service.playSound(SoundType.GAME_LOST, 1);
                }
            }
        );
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
