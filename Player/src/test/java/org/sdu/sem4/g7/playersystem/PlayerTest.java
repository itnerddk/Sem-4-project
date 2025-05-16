package org.sdu.sem4.g7.playersystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;

public class PlayerTest {

    @Test
    public void testPlayerMovement() {
        WorldData worldData = new WorldData();
        Player player = new Player();
        worldData.addEntity(player);

        GameData gameData = Mockito.mock(GameData.class);
        Mockito.when(gameData.isDown(GameData.Keys.UP)).thenReturn(true);
        Mockito.when(gameData.isDown(GameData.Keys.DOWN)).thenReturn(false);

        PlayerControlSystem playerControlSystem = new PlayerControlSystem();
        playerControlSystem.process(gameData, worldData);
        assertNotNull(player);
        assertEquals(0.3, player.getSpeed(), 0.02);
    }
}
