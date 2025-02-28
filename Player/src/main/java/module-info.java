
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

module Player {
    requires Common;
    requires CommonBullet;   
    uses org.sdu.sem4.g7.common.bullet.BulletSPI;
    provides IGamePluginService with org.sdu.sem4.g7.playersystem.PlayerPlugin;
    provides IEntityProcessingService with org.sdu.sem4.g7.playersystem.PlayerControlSystem;
    
}
