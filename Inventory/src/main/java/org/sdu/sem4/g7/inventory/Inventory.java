package org.sdu.sem4.g7.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.*;
import org.sdu.sem4.g7.playersystem.Player;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Inventory implements IInventoryService, IGamePluginService, IEntityProcessingService {
    static ArrayList<Turret> turrets = new ArrayList<>();
    static int turretIndex = 0;

    @Override
    public void add(Entity turret) {
        if (!(turret instanceof Turret newTurret)) return;

        String newId = newTurret.getWeaponId();
        boolean exists = turrets.stream().anyMatch(t -> t.getWeaponId().equals(newId));

        if (!exists) {
            turrets.add(newTurret);
        }
    }


    @Override
    public void remove(Entity turret) {
        turrets.remove(turret);
    }

    @Override
    public void process(GameData gameData, WorldData world) {
        if (gameData.isPressed(Keys.TAB)) {
            turretIndex++;
            if (turretIndex >= turrets.size()) {
                turretIndex = 0;
            }
            setTurret(world);
        }
    }

    private void setTurret(WorldData world) {
        if (turrets.size() == 0) {
            System.out.println("No turret available");
            return;
        }
        for (Player player : world.getEntities(Player.class)) {
            Turret currentTurret = player.getTurret();
            Turret turret = turrets.get(turretIndex);
            if (currentTurret != null) {
                turret.setRotation(currentTurret.getRotation());
                world.removeEntity(currentTurret);
            }
            player.setTurret(turret);
            world.addEntity(turret);
            player.subProcess();
        }
    }

    @Override
    public void start(GameData gameData, WorldData world) {
        turretIndex = 0;

        if (ServiceLoader.load(IBoughtWeaponsService.class).findFirst().isEmpty()) {
            loadAllAvailableWeapons();
        }

        setTurret(world);
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        // do nothing
    }

    private void loadAllAvailableWeapons() {
        turrets.clear();

        ServiceLoader<IWeaponShopInfo> loader = ServiceLoader.load(IWeaponShopInfo.class);

        for (IWeaponShopInfo info : loader) {
            String weaponId = info.getWeaponId();

            ServiceLoader<IGamePluginService> gamePlugins = ServiceLoader.load(IGamePluginService.class);

            for (IGamePluginService plugin : gamePlugins) {
                if (plugin instanceof Turret turret) {
                    if (turret.getWeaponId().equals(weaponId)) {
                        add(turret);
                    }
                }
            }
        }
    }

    @Override
    public IWeaponInstance getCurrentTurret() {
        if (turrets.isEmpty()) return null;
        return turrets.get(turretIndex);
    }

    @Override
    public List<IWeaponInstance> getAllOwnedTurrets() {
        return List.copyOf(turrets);
    }


}
