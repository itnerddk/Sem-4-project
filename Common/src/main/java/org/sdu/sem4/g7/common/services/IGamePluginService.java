package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;

public interface IGamePluginService {

    void start(GameData gameData, WorldData world);

    void stop(GameData gameData, WorldData world);
}
