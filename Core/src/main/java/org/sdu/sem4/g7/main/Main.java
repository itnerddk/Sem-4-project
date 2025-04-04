package org.sdu.sem4.g7.main;

import java.util.ServiceLoader;

import org.sdu.sem4.g7.UI.controllers.MainMenuController;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServiceLocator.loadServices();
        GameData gameData = new GameData();

        // Load pregame plugins
        ServiceLoader.load(IPreGamePluginService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(plugin -> plugin.start(gameData, null));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Intro.fxml"));
        Parent root = loader.load();


        // Inject Main Menu with GameData
        FXMLLoader mainMenuLoader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        mainMenuLoader.load();
        MainMenuController controller = mainMenuLoader.getController();
        controller.setGameData(gameData);

        Scene IntroMediaScene = new Scene(root);

        primaryStage.setScene(IntroMediaScene);
        primaryStage.setTitle("Tank Wars");
        primaryStage.show();
    }
}