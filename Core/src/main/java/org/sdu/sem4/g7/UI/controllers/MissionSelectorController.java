package org.sdu.sem4.g7.UI.controllers;

import java.util.List;
import java.util.ServiceLoader;

import javafx.scene.control.Button;
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
    private StackPane missionSelectorPane;

    private GameData gameData;
    private WorldData worldData;
    private Stage stage;

    private Mission selectedMission = null;

    @FXML
    private GridPane missionGrid;

    public void init(GameData gameData) {
        this.gameData = gameData;
        List<Mission> missions = gameData.getMissionLoaderService().getMissions();

        int cols = 3;
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            Button levelButton = new Button(String.valueOf(mission.getId()));
            levelButton.getStyleClass().add("mission-button");

            int col = i % cols;
            int row = i / cols;
            missionGrid.add(levelButton, col, row);

            // handle click
            levelButton.setOnAction(e -> {
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
    }


    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
