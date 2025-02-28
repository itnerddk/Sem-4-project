package org.sdu.sem4.g7.testlevel;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.ServiceLoader;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

public class TestLevelPlugin extends Level implements IGamePluginService {

    private Level level;

    @Override
    public void start(GameData gameData, Level world) {
        // Implement if needed
        this.level = new TestLevel();
        // Entity entity = new Entity();
        // entity.setPosition(100, 100);
        // this.level.addEntity(entity);

        for (IEntityPluginService plugin : getPluginServices()) {
            plugin.start(gameData, this.level);
        }

        gameData.addLevel(this.level);
    }

    @Override
    public void stop(GameData gameData, Level world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
    private Collection<? extends IEntityPluginService> getPluginServices() {
        return ServiceLoader.load(IEntityPluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
