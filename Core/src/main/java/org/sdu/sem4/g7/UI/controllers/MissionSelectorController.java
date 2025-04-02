package org.sdu.sem4.g7.UI.controllers;

import java.util.List;
import java.util.ServiceLoader;



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

public class MissionSelectorController {

    @FXML
    private StackPane missionSelectorPane;

    private GameData gameData;
    private WorldData worldData;
    private Stage stage;

    @FXML ListView<Mission> missionListView;

    @FXML
    private StackPane missionSelectorStackPane;

    public void init(GameData gameData) {
        this.gameData = gameData;
        List<Mission> missions = gameData.getMissionLoaderService().getMissions();
        missionListView.getItems().setAll(missions);
    }

    @FXML
    private void handleStartMission() {

        Mission selectedMission = missionListView.getSelectionModel().getSelectedItem();
        if (selectedMission != null) {
            System.out.println("Selected mission: " + selectedMission.getId());
            ServiceLoader.load(IGamePluginService.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .forEach(plugin -> plugin.start(gameData, worldData));
            
            worldData = gameData.getMissionLoaderService().loadMission(selectedMission.getId());
            System.out.println(selectedMission.getId());
            
            GameInstance game = new GameInstance(gameData, worldData);
            Stage gameStage = (Stage) missionListView.getScene().getWindow();
            gameStage.setScene(game.getScene());
        }



        

        
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }



    
}
