package org.sdu.sem4.g7.UI.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.sdu.sem4.g7.common.services.ServiceLocator;
import org.sdu.sem4.g7.main.GameInstance;
import javafx.scene.control.Label;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label coinDisplay;

    @FXML
    private Label levelDisplay;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource("/images/intro_image.png").toExternalForm());
        backgroundImage.setImage(img);

        // Show Coins from Currency using ServiceLocator -> if module is not present remove currency display
        ServiceLocator.getCurrencyService().ifPresentOrElse(service -> {
            coinDisplay.setText("Coins: " + service.getCurrency());
        }, () -> {
            coinDisplay.setVisible(false);
        });

        // Show Level from Level using Service Locator -> if module is not present remove level display
        ServiceLocator.getLevelService().ifPresentOrElse(service -> {
            levelDisplay.setText("Level: " + service.getLevel());
        }, () -> {
            levelDisplay.setVisible(false);
        });
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
            try {
                // Load MainMenu.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MissionSelector.fxml"));
                Parent mainMenuRoot = loader.load();

                // Skift scene
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(mainMenuRoot);
                stage.setScene(scene);
                stage.setTitle("Tank Wars - Mission Selector");
                stage.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

    @FXML private void handleShop(ActionEvent event) {
    }

    @FXML private void handleSave(ActionEvent event) {
    }

    @FXML private void handleLoadSave(ActionEvent event) {
    }

    @FXML private void handleDifficulty(ActionEvent event) {
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Intro.fxml"));
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
