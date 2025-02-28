package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;

public interface ILevelPluginService {

    void start(GameData gameData);

    void stop(GameData gameData);
}
