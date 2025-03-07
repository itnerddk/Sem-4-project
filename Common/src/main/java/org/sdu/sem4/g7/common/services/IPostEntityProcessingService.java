package org.sdu.sem4.g7.common.services;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;

/**
 *
 * @author jcs
 */
public interface IPostEntityProcessingService {

    void process(GameData gameData, Mission world);
}
