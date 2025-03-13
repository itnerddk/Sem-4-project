package org.sdu.sem4.g7.tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.tank.parts.Turret;

public class TurretLoader implements IGamePluginService {
    private static final ArrayList<Provider<? extends Turret>> turrets = new ArrayList<>();

    public static List<Provider<? extends Turret>> getTurrets() {
        return turrets;
    }

    @Override
    public void start(GameData gameData, Mission world) {
        loadTurrets().forEach(turrets::add);
    }

    @Override
    public void stop(GameData gameData, Mission world) {
        turrets.clear();
    }
    

    private Collection<Provider<? extends Turret>> loadTurrets() {
        return ServiceLoader.load(Turret.class).stream().collect(toList());
    }
}