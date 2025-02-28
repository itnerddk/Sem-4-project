package org.sdu.sem4.g7.common.bullet;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;

/**
 *
 * @author corfixen
 */
public interface BulletSPI {
    Entity createBullet(Entity e, GameData gameData);
}
