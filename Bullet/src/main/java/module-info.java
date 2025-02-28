import org.sdu.sem4.g7.common.bullet.BulletSPI;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;

module Bullet {
    requires Common;
    requires CommonBullet;
    provides IGamePluginService with org.sdu.sem4.g7.bulletsystem.BulletPlugin;
    provides BulletSPI with org.sdu.sem4.g7.bulletsystem.BulletControlSystem;
    provides IEntityProcessingService with org.sdu.sem4.g7.bulletsystem.BulletControlSystem;
}