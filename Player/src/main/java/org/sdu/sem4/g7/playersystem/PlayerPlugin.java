package org.sdu.sem4.g7.playersystem;

import java.net.URISyntaxException;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.services.IEntityPluginService;


public class PlayerPlugin implements IEntityPluginService {
    @Override
    public void start(GameData gameData, Level world) {
        System.out.println("Player plugin started");
        // Spawn player in middle of level
        Player player = new Player();
        player.setPosition(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        world.addEntity(player);
    }

    @Override
    public void stop(GameData gameData, Level world) {
        // Implement entity
    }
}
