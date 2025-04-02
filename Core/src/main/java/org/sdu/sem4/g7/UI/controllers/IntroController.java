package org.sdu.sem4.g7.UI.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sdu.sem4.g7.main.GameInstance;

import javafx.geometry.Rectangle2D;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.application.Platform;


public class IntroController implements Initializable {

    @FXML
    private ImageView backgroundImage;

    @FXML
    private ImageView blurredSnapshot;

    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource("/images/intro_image.png").toExternalForm());
        backgroundImage.setImage(img);

        // Delay snapshot to ensure layout is complete
        Platform.runLater(() -> {
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            params.setViewport(new Rectangle2D(210, 360, 380, 250));
            WritableImage snapshot = backgroundImage.snapshot(params, null);
            blurredSnapshot.setImage(snapshot);

        });
    }

    @FXML
    private void handleStartGame(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        GameInstance game = new GameInstance(stage);
        Scene gameScene = game.getScene();

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
