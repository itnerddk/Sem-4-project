package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;

public interface IGamePluginService {

    void start(GameData gameData, Mission world);

    void stop(GameData gameData, Mission world);
}
