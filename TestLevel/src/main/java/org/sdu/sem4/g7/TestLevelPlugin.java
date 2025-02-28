import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.services.ILevelPluginService;

public class TestLevelPlugin implements ILevelPluginService {

    private Level level;

    @Override
    public void start(GameData gameData) {
        // Implement if needed
        Entity entity = new Entity();
        entity.setRadius(1);
        entity.setPolygonCoordinates(0, 0, 1, 0, 1, 1, 0, 1);
        entity.setX(300);
        entity.setY(300);

        level.addEntity(entity);
    }

    @Override
    public void stop(GameData gameData) {
        
    }
    
}
