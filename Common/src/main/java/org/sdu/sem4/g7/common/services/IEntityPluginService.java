package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;

public interface IEntityPluginService {

    void start(GameData gameData, Mission mission);

    void stop(GameData gameData, Mission mission);
}
