package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

public class CannonTurretPlugin implements IPreGamePluginService {

    @Override
    public void start(GameData gameData, WorldData world) {
        System.out.println("Cannon Turret plugin started.");
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
}
