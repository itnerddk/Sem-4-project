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
import org.sdu.sem4.g7.common.Config.CommonConfig;
import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityPluginService;
import org.sdu.sem4.g7.common.services.IMissionLoaderService;
import org.sdu.sem4.g7.common.services.ServiceLocator;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Provides an implementation of IMissionLoaderService
 */
public class MissionLoaderService implements IMissionLoaderService {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Gamedata
	 */
	private GameData gameData;

	/**
	 * WorldData
	 */
	private WorldData worldData;

	/**
	 * All missions metadata
	 */
	private List<Mission> missions;

	/**
	 * All tiles (full objects)
	 */
	private Map<Integer, TileObject> tiles;

	/**
	 * Size of the map (filled by the render method)
	 */
	private int mapSizeX;
	private int mapSizeY;

	public MissionLoaderService() {
		this.missions = new ArrayList<>();
		this.tiles = new HashMap<>();
	}

	/**
	 * Insert the gameData and worldData into the service
	 * @param gameData
	 * @param worldData
	 * @throws IOException
	 */
	public void insert(GameData gameData, WorldData worldData) throws IOException {
		this.gameData = gameData;
		this.worldData = worldData;
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
			// Iterate all enemies and based on max health multiple difficulty
			try {
				List<EnemyStartPositionObject> enemies = getMissionObject(mission.getId()).getEnemies();
				System.out.println(mission.getName() + "(" + mission.getId() + ") has " + enemies.size() + " enemies" + " with a difficulty of " + mission.getDifficulty());
				for (EnemyStartPositionObject enemy : enemies) {
					// Enemies with a max health of 100 should contribute with something around 1.05x
					// f(x)=0.7+1.5 (1-ℯ^(-((x)/(400))))
					mission.setDifficulty((float)(mission.getDifficulty() * Math.max(1.0, (0.7 + 1.5 * (1 - Math.exp(-(((float)enemy.getHealth()) / (400))))))));
				}
				System.out.println(mission.getName() + " has a difficulty of " + mission.getDifficulty());
			} catch (Exception e) {
				System.out.println("No level file, not calculating difficulty");
			}
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

	/**
	 * Render the map tiles
	 */
	private void renderMapTiles(List<List<Integer>> map, WorldData world) {

			// set map size properties
			this.mapSizeX = map.get(0).size() * 96; // TODO: Change tile size to be dynamic
			this.mapSizeY = map.size() * 96; // TODO: Change tile size to be dynamic

			// Render the map
			for (int y = 0; y < map.size(); y++) {

				for (int x = 0; x < map.get(y).size(); x++) {
	
				// get tile information
				TileObject tileInfo = tiles.get(map.get(y).get(x));

				// Create a entity to render
				Entity tileEntity = new TileEntity();

				// set sprite
				tileEntity.setSprite(new File(Config.tilesDir, tileInfo.getImage()).toURI(), CommonConfig.DEFAULT_SCALING);
			
				if (y == 0) CommonConfig.setTileSize((int) tileEntity.getSprite().getImage().getWidth());

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

		// Fill AI with map
		ServiceLocator.getLogicService().ifPresentOrElse(
				service -> {
					service.init(mission.getMap(), gameData);
				},
				() -> {
					System.err.println("Logic service not found");
				}
		);
		// Fill Raycasting with map
		ServiceLocator.getRayCastingService().ifPresentOrElse(
			service -> {
				service.init(mission.getMap());
			},
			() -> {
				System.err.println("Raycasting service not found");
			}
		);

		// Create a new world
		WorldData world = new WorldData();

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
				enemy.setPosition(espo.getX() * CommonConfig.getTileSize(), espo.getY() * CommonConfig.getTileSize());
				enemy.setHealth(espo.getHealth());
				enemy.setMaxHealth(espo.getHealth());
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
			player.setPosition(mission.getPlayer().getX() * CommonConfig.getTileSize(), mission.getPlayer().getY() * CommonConfig.getTileSize());
			world.addEntity(player);
		} catch (Exception ex) {
			System.err.println("Could not create a player!");
			ex.printStackTrace();
			return null;
		}

		this.worldData = world; // I think it's needed, because im pretty sure the constructor won't get a reference. Because it is null, until this method have been invoked.
		return world;
	}

	@Override
	public int getMapSizeX() {
		return mapSizeX;
	}

	@Override
	public int getMapSizeY() {
		return mapSizeY;
	}

	private Collection<? extends IEntityPluginService> getPluginServices() {
        return ServiceLoader.load(IEntityPluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

}
