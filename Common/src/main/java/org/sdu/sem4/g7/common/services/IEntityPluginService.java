package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Level;

public interface IEntityPluginService {

    void start(GameData gameData, Level world);

    void stop(GameData gameData, Level world);
}
