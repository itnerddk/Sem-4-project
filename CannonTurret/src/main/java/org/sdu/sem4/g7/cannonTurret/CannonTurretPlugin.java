package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

public class CannonTurretPlugin implements IPreGamePluginService {

    @Override
    public void start(GameData gameData, Mission world) {
        System.out.println("Cannon Turret plugin started.");
        CannonTurret cTurret = new CannonTurret();
        // gameData.addTurrets(cTurret);
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }
    
}
