package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sdu.sem4.g7.main.GameInstance;

public class MainMenuController {

    @FXML
    private void handleStartGame(ActionEvent event) {
        GameInstance game = new GameInstance();
        Scene gameScene = game.getScene();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        stage.setTitle("Tank Wars");
        stage.show();
    }
}
