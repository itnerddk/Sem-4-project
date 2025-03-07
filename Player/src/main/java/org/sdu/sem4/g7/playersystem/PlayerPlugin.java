package org.sdu.sem4.g7.playersystem;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.tank.parts.Turret;


public class PlayerPlugin implements IEntityPluginService {
    @Override
    public void start(GameData gameData, Mission world) {
        System.out.println("Player plugin started");
        // Spawn player in middle of mission
        Player player = new Player();
        player.setTurret((Turret) gameData.getTurrets().get(0));
        player.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        player.setHealth(100);
        world.addEntity(player);
        world.addEntity(player.getTurret());
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        // Implement entity
    }
}
