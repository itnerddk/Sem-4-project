package org.sdu.sem4.g7.UI.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class VictoryController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Background image
        Image img = new Image(getClass().getResource("/images/intro_image.png").toExternalForm());
        backgroundImage.setImage(img);
    }

    @FXML
    private void handleTryAgain(ActionEvent event) {

    }

    @FXML
    private void handleMainMenu(ActionEvent event) {

    }

    @FXML
    private void handleCredits(ActionEvent event) {

    }

    @FXML
    private void handleQuitGame(ActionEvent event) {

    }
    
}
