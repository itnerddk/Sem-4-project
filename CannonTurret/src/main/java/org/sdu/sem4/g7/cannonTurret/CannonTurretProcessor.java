package org.sdu.sem4.g7.cannonTurret;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.tank.parts.Turret;

public class CannonTurretProcessor implements IEntityProcessingService {

	@Override
	public void process(GameData gameData, Mission mission) {
		// Implementation of the process method
        for (Entity e : mission.getEntities(CannonTurret.class)) {
            Turret t = (Turret) e;
            t.setPosition(t.getTank().getPosition());
        }
	}
}
