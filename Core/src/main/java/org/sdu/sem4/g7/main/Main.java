package org.sdu.sem4.g7.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sdu.sem4.g7.common.services.ServiceLocator;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ServiceLocator.loadServices();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IntroMedia.fxml"));
        Parent root = loader.load();
        Scene IntroMediaScene = new Scene(root);
        primaryStage.setScene(IntroMediaScene);
        primaryStage.setTitle("Tank Wars");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
