package org.sdu.sem4.g7.tank;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;

import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;
import org.sdu.sem4.g7.common.services.ITurretProviderService;
import org.sdu.sem4.g7.tank.parts.Turret;

public class TurretLoader implements IPreGamePluginService, ITurretProviderService {
    private static final ArrayList<Provider<? extends Entity>> turrets = new ArrayList<>();

    public List<Provider<? extends Entity>> getTurrets() {
        return turrets;
    }

    @Override
    public void start(GameData gameData, WorldData world) {
        loadTurrets().forEach(turrets::add);
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        turrets.clear();
    }

    private Collection<Provider<? extends Turret>> loadTurrets() {
        return ServiceLoader.load(Turret.class).stream().collect(toList());
    }
}