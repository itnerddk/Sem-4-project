package org.sdu.sem4.g7.UI.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.sdu.sem4.g7.MissionLoader.services.MissionLoaderService;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.main.GameInstance;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    private Stage stage;

    @FXML
    private StackPane mainMenuPane;

    @FXML
    private ImageView backgroundImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image(getClass().getResource("/images/image.png").toExternalForm());

        // Set the background image to fit the pane, makes the background dynamic
        backgroundImage.fitWidthProperty().bind(mainMenuPane.widthProperty());
        backgroundImage.fitHeightProperty().bind(mainMenuPane.heightProperty());
        backgroundImage.setImage(img);
    }

    @FXML
    private void handleStartGame(ActionEvent event) {

        // Load the MissionSelector.fxml file
        showMissionSelector();

        // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // GameInstance game = new GameInstance(stage);
        // Scene gameScene = game.getScene();

        // stage.setScene(gameScene);
        // stage.setTitle("Tank Wars");
        // stage.show();
    }

    public void handleSettings(ActionEvent actionEvent) {

    }

    public void handleCredits(ActionEvent actionEvent) {

    }

    public void handleQuitGame(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void setScene(Stage stage) {
        this.stage = stage;
    }

    private void showMissionSelector(){
        try {
            GameData gameData = new GameData();
            // Set up the game data so it has a mission loader
            MissionLoaderService missionLoader = new MissionLoaderService(gameData, null);
            gameData.setMissionLoaderService(missionLoader);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MissionSelector.fxml"));
            Pane missionSelectorPane = loader.load();
            
            MissionSelectorController controller = loader.getController();
            controller.init(gameData);
            controller.setStage(stage);

            Scene missionSelectorScene = new Scene(missionSelectorPane);
            stage.setScene(missionSelectorScene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
