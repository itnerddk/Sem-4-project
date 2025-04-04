package org.sdu.sem4.g7.UI.controllers;

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
import org.sdu.sem4.g7.common.services.ServiceLocator;
import javafx.scene.control.Label;


import java.io.IOException;

import org.sdu.sem4.g7.MissionLoader.services.MissionLoaderService;
import org.sdu.sem4.g7.common.data.GameData;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    private Stage stage;
    private GameData gameData;

    @FXML
    private Parent mainMenuPane;

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
            GameData gameData = new GameData();
            // Set up the game data so it has a mission loader
            MissionLoaderService missionLoader = new MissionLoaderService(gameData, null);
            gameData.setMissionLoaderService(missionLoader);
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MissionSelector.fxml"));
            Parent missionSelectorPane = loader.load();
            
            MissionSelectorController controller = loader.getController();
            controller.init(gameData);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controller.setStage(stage);

            Scene missionSelectorScene = new Scene(missionSelectorPane);
            stage.setScene(missionSelectorScene);
        } catch (Exception e) {
            System.out.println("Error loading MissionSelector.fxml: ");
        }

   

    }

    @FXML
    private  void handleShop(ActionEvent event){

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

    public void setScene(Stage stage) {
        this.stage = stage;
    }


    public void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

}
