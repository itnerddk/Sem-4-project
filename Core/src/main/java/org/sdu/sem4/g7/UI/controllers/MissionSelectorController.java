package org.sdu.sem4.g7.UI.controllers;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.main.GameInstance;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

public class MissionSelectorController {

    @FXML
    private ScrollPane missionScroll;
    @FXML
    private StackPane contentWrapper;

    @FXML
    private ImageView backgroundImage;

    private GameData gameData;
    private WorldData worldData;
    private Stage stage;

    private Mission selectedMission = null;

    @FXML
    private GridPane missionGrid;

    public void init(GameData gameData) {
        backgroundImage.setImage(new Image(getClass().getResource("/images/MainMenuBackground.png").toExternalForm()));

        this.gameData = gameData;
        List<Mission> missions = gameData.getMissionLoaderService().getMissions();
        missions.sort(Comparator.comparingInt(Mission::getId));

        int cols = 3;
        int highestUnlockedId = 2;

        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            VBox tile = new VBox();
            tile.setAlignment(Pos.CENTER);
            tile.setSpacing(5);
            tile.getStyleClass().add("mission-tile");

            Label number = new Label(String.valueOf(mission.getId()));
            number.getStyleClass().add("mission-number");

            boolean isLocked = i > highestUnlockedId;

            if (isLocked) {
                tile.getStyleClass().add("locked");

                ImageView lockIcon = new ImageView();
                lockIcon.setImage(new Image(
                        Objects.requireNonNull(getClass().getResource("/images/lock.png")).toExternalForm()
                ));
                lockIcon.setFitWidth(30);
                lockIcon.setFitHeight(30);

                tile.getChildren().add(lockIcon); // 👈 Kun lås, intet tal
                tile.setDisable(true);
            } else {
                tile.getChildren().add(number);
                tile.setOnMouseClicked(e -> {
                    selectedMission = mission;
                    System.out.println("Selected: " + mission.getName());

                    if (selectedMission != null) {
                        ServiceLoader.load(IGamePluginService.class)
                                .stream()
                                .map(ServiceLoader.Provider::get)
                                .forEach(plugin -> plugin.start(gameData, worldData));

                        worldData = gameData.getMissionLoaderService().loadMission(selectedMission.getId());
                        GameInstance game = new GameInstance(gameData, worldData);
                        Stage gameStage = (Stage) missionGrid.getScene().getWindow();
                        gameStage.setScene(game.getScene());
                    }
                });
            }

            int col = i % cols;
            int row = i / cols;
            missionGrid.add(tile, col, row);
        }
    }



    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private void handleBack(ActionEvent event) {
        loadScene(event, "/view/MainMenu.fxml", "Main Menu");
    }

    private void loadScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
