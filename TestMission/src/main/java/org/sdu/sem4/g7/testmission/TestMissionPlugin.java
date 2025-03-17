package org.sdu.sem4.g7.testmission;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.ServiceLoader;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

public class TestMissionPlugin implements IGamePluginService {

    private WorldData mission;

    @Override
    public void start(GameData gameData, WorldData world) {
        // Implement if needed
        this.mission = new TestMission();

        for (IEntityPluginService plugin : getPluginServices()) {
            plugin.start(gameData, this.mission);
        }

        //this.mission.load();

        //gameData.addMission(this.mission);
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
    private Collection<? extends IEntityPluginService> getPluginServices() {
        return ServiceLoader.load(IEntityPluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
