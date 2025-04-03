package org.sdu.sem4.g7.main;

import java.util.ServiceLoader;

import org.sdu.sem4.g7.UI.controllers.MainMenuController;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.services.IMissionLoaderService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        GameData gameData = new GameData();

        // Load pregame plugins
        ServiceLoader.load(IPreGamePluginService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(plugin -> plugin.start(gameData, null));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        StackPane pane = loader.load();

        MainMenuController controller = loader.getController();
        controller.setGameData(gameData);

        controller.setScene(primaryStage);

        Scene scene = new Scene(pane, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Wars");
        primaryStage.show();
    }
}
