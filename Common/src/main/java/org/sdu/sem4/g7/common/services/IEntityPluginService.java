package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;

public interface IEntityPluginService {

    void start(GameData gameData, WorldData mission);

    void stop(GameData gameData, WorldData mission);
}
