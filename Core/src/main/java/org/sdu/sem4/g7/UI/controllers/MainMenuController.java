package org.sdu.sem4.g7.UI.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource("/images/mainmenu_image.png").toExternalForm());
        backgroundImage.setImage(img);
    }
}
