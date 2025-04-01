package org.sdu.sem4.g7.main;

import org.sdu.sem4.g7.UI.controllers.MainMenuController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainMenu.fxml"));
        StackPane pane = loader.load();
       
        MainMenuController controller = loader.getController();
        
        controller.setScene(primaryStage);

        Scene scene = new Scene(pane, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Wars");
        primaryStage.show();
    }
}
