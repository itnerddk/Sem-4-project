package org.sdu.sem4.g7.inventory;

import java.util.ArrayList;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.IInventoryService;
import org.sdu.sem4.g7.playersystem.Player;
import org.sdu.sem4.g7.tank.parts.Turret;

public class Inventory implements IInventoryService, IGamePluginService, IEntityProcessingService {
    static ArrayList<Turret> turrets = new ArrayList<>();
    static int turretIndex = 0;

    @Override
    public void add(Entity turret) {
        if (!(turret instanceof Turret)) {
            throw new IllegalArgumentException("Entity is not a turret");
        }
        turrets.add((Turret) turret);
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
        setTurret(world); 
    }

    @Override
    public void stop(GameData gameData, WorldData world) {
        // do nothing
    }
}
