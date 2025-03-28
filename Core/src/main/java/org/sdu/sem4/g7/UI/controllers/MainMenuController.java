package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.sdu.sem4.g7.main.GameInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource("/images/image.png").toExternalForm());
        backgroundImage.setImage(img);
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        GameInstance game = new GameInstance();
        Scene gameScene = game.getScene();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
        stage.setTitle("Tank Wars");
        stage.show();
    }

    public void handleSettings(ActionEvent actionEvent) {

    }

    public void handleCredits(ActionEvent actionEvent) {

    }

    public void handleQuitGame(ActionEvent actionEvent) {
        System.exit(0);
    }
}
