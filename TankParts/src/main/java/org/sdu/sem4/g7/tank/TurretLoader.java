package org.sdu.sem4.g7.tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.tank.parts.Turret;

public class TurretLoader implements IGamePluginService {
    private static final ArrayList<Turret> turrets = new ArrayList<>();

    public static List<Turret> getTurrets() {
        return turrets;
    }

    @Override
    public void start(GameData gameData, Mission world) {
        for (Turret turret : loadTurrets()) {
            turrets.add(turret);
        }
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        turrets.clear();
    }
    

    private Collection<? extends Turret> loadTurrets() {
        return ServiceLoader.load(Turret.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}