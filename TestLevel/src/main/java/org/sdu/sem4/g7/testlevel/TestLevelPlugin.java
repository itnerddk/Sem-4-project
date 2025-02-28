package org.sdu.sem4.g7.testlevel;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.services.IGamePluginService;

public class TestLevelPlugin extends Level implements IGamePluginService {

    private Level level;

    @Override
    public void start(GameData gameData, Level world) {
        // Implement if needed
        this.level = new TestLevel();
        Entity entity = new Entity();
        entity.setRadius(1);
        entity.setPolygonCoordinates(-5, -5, 10, 0, -5, 5);
        entity.setX(gameData.getDisplayHeight() / 2);
        entity.setY(gameData.getDisplayWidth() / 2);
        entity.setRadius(8);
        this.level.addEntity(entity);

        gameData.addLevel(this.level);
    }

    @Override
    public void stop(GameData gameData, Level world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
}
