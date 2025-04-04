package org.sdu.sem4.g7.UI.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.net.URL;

public class IntroMediaController {

    @FXML
    private MediaView mediaView;

    public void initialize() {
        URL mediaUrl = getClass().getResource("/media/Intro.mp4");
        Media media = new Media(mediaUrl.toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaView.setMediaPlayer(mediaPlayer);


        mediaPlayer.setOnEndOfMedia(() -> switchToIntro((Stage) mediaView.getScene().getWindow()));
    }

    private void switchToIntro(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/Intro.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Tank Wars");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
