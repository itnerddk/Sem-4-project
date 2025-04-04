package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.sdu.sem4.g7.main.GameInstance;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MissionSelectorController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Button mission2Button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load background image
        Image img = new Image(getClass().getResource("/images/intro_image.png").toExternalForm());
        backgroundImage.setImage(img);

        // Simulating to check if mission 1 is complete
        boolean mission1Completed = checkMission1Completed();

        // Unlock mission 2 if mission 1 was completed
        mission2Button.setDisable(!mission1Completed);
    }

    private boolean checkMission1Completed() {
        // TODO:
        return false;
    }

    @FXML
    private void handleMission1(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GameInstance game = new GameInstance(stage);
        Scene gameScene = game.getScene();

        stage.setScene(gameScene);
        stage.setTitle("Tank Wars - Mission 1");
        stage.show();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Menu");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
