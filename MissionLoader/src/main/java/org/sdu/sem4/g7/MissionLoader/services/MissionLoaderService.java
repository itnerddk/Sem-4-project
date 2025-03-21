package org.sdu.sem4.g7.MissionLoader.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.MissionLoader.config.Config;
import org.sdu.sem4.g7.MissionLoader.objects.EnemyStartPositionObject;
import org.sdu.sem4.g7.MissionLoader.objects.MissionObject;
import org.sdu.sem4.g7.MissionLoader.objects.TileEntity;
import org.sdu.sem4.g7.MissionLoader.objects.TileObject;
import org.sdu.sem4.g7.MissionLoader.objects.WorldDataObject;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IMissionLoaderService;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Provides an implementation of IMissionLoaderService
 */
public class MissionLoaderService implements IMissionLoaderService {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/*
	 * Gamedata
	 */
	private final GameData gameData;

	/*
	 * All missions metadata
	 */
	private List<Mission> missions;

	/*
	 * All tiles (full objects)
	 */
	private Map<Integer, TileObject> tiles;


	public MissionLoaderService(GameData gameData) throws IOException {
		this.gameData = gameData;
		this.missions = new ArrayList<>();
		this.tiles = new HashMap<>();

		loadTiles();
		loadMissionsMetadata();
	}

	/**
	 * Load tiles into local hashmap, with the tile id as the map's key 
	 * @throws IOException
	 */
	private void loadTiles() throws IOException {
		for (File tileFile : Config.tilesDir.listFiles()) {
			
			// Skip non json files
			if (!tileFile.getName().contains(".json")) {
				continue;
			}

			TileObject tile = objectMapper.readValue(tileFile, TileObject.class);

			tiles.put(tile.getId(), tile);
		}
	}

	/**
	 * Loads missions metadata into local missions list
	 * 
	 * @throws IOException 
	 */
	private void loadMissionsMetadata() throws IOException {
		for (File missionFile : Config.missionsDir.listFiles()) {
			Mission mission = objectMapper.readValue(missionFile, Mission.class); // NOTE: This does not load the entire mission object, just the metadata!
			missions.add(mission);
		}
	}

	/**
	 * Returns a full mission object (MissionObject) for a given mission
	 * 
	 * @param id
	 * @return MissionObject
	 * @throws IOException if object cannot be deserialized or does not exists
	 */
	private MissionObject getMissionObject(int id) throws IOException {
		return objectMapper.readValue(new File(Config.missionsDir, String.valueOf(id) + ".json"), MissionObject.class);
	}

	/*
	 * Render the map tiles
	 */
	private void renderMapTiles(List<List<Integer>> map, WorldData world) {
			for (int y = 0; y < map.size(); y++) {
				for (int x = 0; x < map.get(y).size(); x++) {
	
					// get tile information
					TileObject tileInfo = tiles.get(map.get(y).get(x));

				// Create a entity to render
				Entity tileEntity = new TileEntity();

				// set sprite
				tileEntity.setSprite(new File(Config.tilesDir, tileInfo.getImage()).toURI(), Config.tileSize);

				// set position
				tileEntity.setPosition(x * tileEntity.getSprite().getImage().getWidth(), y * tileEntity.getSprite().getImage().getHeight());

				// set z index
				tileEntity.setzIndex(tileInfo.getZ());

				// set collistion of tile
				tileEntity.setCollision(tileInfo.isCollision());

				// set immoveable
				tileEntity.setImmoveable(tileInfo.isImmoveable());

				// set health of tile
				tileEntity.setHealth(tileInfo.getHealth());

				// add tile to world
				world.addEntity(tileEntity);
			}
		}
	}

	@Override
	public List<Mission> getMissions() {
		return missions;
	}

	@Override
	public Mission getMission(int id) {
		return missions.get(id);
	}

	@Override
	public int missionCount() {
		return missions.size();
	}

	@Override
	public WorldData loadMission(int id) {
		System.out.println("Loading mission " + id);

		// Get full mission object
		MissionObject mission;
		try {
			mission = getMissionObject(id);
		} catch (IOException e) {
			System.err.println("Could not load mission id " + id);
			e.printStackTrace();
			return null;
		}

		// Create a new world
		WorldData world = new WorldDataObject();

		// Load plugins
		for (IEntityPluginService plugin : getPluginServices()) {
            plugin.start(gameData, world);
        }

		// render map
		renderMapTiles(mission.getMap(), world);

		// Create enemies
		for (EnemyStartPositionObject espo : mission.getEnemies()) {
			Entity enemy;
			try {
				enemy = world.getEntityTypes().get(espo.getEntityType()).get(0).getDeclaredConstructor().newInstance();
				enemy.setPosition(espo.getX(), espo.getY());
				enemy.setHealth(espo.getHealth());
				enemy.setMaxHealth(100);
				System.out.println("set enemy health from mission: " + espo.getHealth());
				
				// Set maxHealth to the same value as health if not explicitly defined
        		if (enemy.getMaxHealth() == 0) {
            	enemy.setMaxHealth(espo.getHealth());
}
				
				world.addEntity(enemy);
				world.addEntity(enemy.getChildren());
			} catch (Exception ex) {
				System.err.println("Could not create a player!");
				ex.printStackTrace();
			}
		}

		// Create player
		try {
			Entity player = world.getEntityTypes().get("Players").get(0).getDeclaredConstructor().newInstance();
			player.setPosition(mission.getPlayer().getX(), mission.getPlayer().getY());
			player.setHealth(100);
			world.addEntity(player);
			world.addEntity(player.getChildren());
		} catch (Exception ex) {
			System.err.println("Could not create a player!");
			ex.printStackTrace();
			return null;
		}

		return world;
	}

	private Collection<? extends IEntityPluginService> getPluginServices() {
        return ServiceLoader.load(IEntityPluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
    
}
