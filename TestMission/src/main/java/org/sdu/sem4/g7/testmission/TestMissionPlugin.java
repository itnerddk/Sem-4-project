package org.sdu.sem4.g7.testmission;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.ServiceLoader;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

public class TestMissionPlugin extends Mission implements IGamePluginService {

    private Mission mission;

    @Override
    public void start(GameData gameData, Mission world) {
        // Implement if needed
        this.mission = new TestMission();
        // Entity entity = new Entity();
        // entity.setPosition(100, 100);
        // this.mission.addEntity(entity);

        for (IEntityPluginService plugin : getPluginServices()) {
            plugin.start(gameData, this.mission);
        }

        gameData.addMission(this.mission);
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
    private Collection<? extends IEntityPluginService> getPluginServices() {
        return ServiceLoader.load(IEntityPluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
