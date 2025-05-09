package org.sdu.sem4.g7.MissionLoader.services;

import org.sdu.sem4.g7.MissionLoader.objects.TileEntity;
import org.sdu.sem4.g7.MissionLoader.objects.TileObject;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;

public class TileService implements IEntityProcessingService{

    @Override
    public void process(GameData gameData, WorldData world) {
        for (Entity e : world.getEntities(TileEntity.class)) {
            TileEntity tile = (TileEntity) e;
            if (tile.getHealth() <= 0) {
                world.removeEntity(tile);

                MissionLoaderService missionLoaderService = (MissionLoaderService) gameData.getMissionLoaderService();
                TileObject backgroundTile = missionLoaderService.getTiles().get(tile.getReplacedByOnDeath());
                TileEntity backgroundTileEntity = new TileEntity(backgroundTile);
                backgroundTileEntity.setPosition(tile.getPosition());
                world.addEntity(backgroundTileEntity);
            }
        }
    }

}
