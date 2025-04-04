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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.sdu.sem4.g7.main.GameInstance;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.PauseTransition;
import javafx.util.Duration;



public class IntroController implements Initializable {

    @FXML
    private ImageView gifView;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private AnchorPane rootPane;

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
    }

    public void handleQuitGame(ActionEvent actionEvent) {
        System.exit(0);
    }


    public void loadSave(ActionEvent actionEvent) {
    }
}
