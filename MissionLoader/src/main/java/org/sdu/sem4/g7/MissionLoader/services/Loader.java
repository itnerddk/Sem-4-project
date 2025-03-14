package org.sdu.sem4.g7.MissionLoader.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.MissionLoader.objects.EnemyStartPositionObject;
import org.sdu.sem4.g7.MissionLoader.objects.MissionObject;
import org.sdu.sem4.g7.MissionLoader.objects.PlayerStartPositionObject;
import org.sdu.sem4.g7.MissionLoader.objects.TileObject;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IGamePluginService;

/*
 * Game plugin
 * 
 * This file is responsible for loading all missions
 */
public class Loader implements IGamePluginService {

	private final static File dataDir = new File("./data");
	private final static File missionsDir = new File(dataDir, "missions");
	private final static File tilesDir = new File(dataDir, "tiles");

	/**
	 * Creates filestructure for Mission data, if it does not already exists
	 */
	private void createFileStructure() {
		// Create datadir if it does not exists
		if (!dataDir.isDirectory()) {
			dataDir.mkdirs();
		}

		// create missions dir if it does not exists
		if (!missionsDir.isDirectory()) {
			missionsDir.mkdir();
		}

		// create tiles dir if it does not exists
		if (!tilesDir.isDirectory()) {
			tilesDir.mkdir();
		}
	}

	/*
	 * Creates a default tile, if it does not exists
	 */
	private void createDefaultTile() {
		if (!new File(tilesDir, "0.json").exists()) {

			// Create a new tile
			TileObject tile = new TileObject();
			tile.setId(0);
			tile.setImage("0.png"); // images will be loaded from the tiles dir
			tile.setZ(1);
			tile.setCollision(true);
			tile.setImmoveable(true);
			tile.setHealth(1);

			// TODO: Serialize to json and save it
		}
	}

	/*
	 * Create a default mission, if it does not exists
	 * 
	 * NOTE: I do this, in order to create a complete json file. With everything set just as the serializer expects it.
	 */
	private void createDefaultMission() {
		if (!new File(missionsDir, "0.json").exists()) {

			// Create a new enenmy start position
			EnemyStartPositionObject espo = new EnemyStartPositionObject();
			espo.setEntityType("Enemies");
			espo.setX(100);
			espo.setY(100);
			espo.setHealth(100);

			// Create a new player start position
			PlayerStartPositionObject pspo = new PlayerStartPositionObject();
			pspo.setX(200);
			pspo.setY(200);

			// Create a new Mission Object
			MissionObject mo = new MissionObject();
			mo.setId(0);
			mo.setName("Demo");
			mo.setDescription("This is a demo mission!");
			mo.setDifficulty(5);

			mo.setPlayer(pspo);

			mo.setEnemies(new ArrayList<>());
			mo.getEnemies().add(espo);

			mo.setMap(new ArrayList<>());
			List<List<Integer>> ymap = mo.getMap();

			int demoMapSize = 20;

			for (int y = 0; y < demoMapSize; y++) {
				
				// create x axis for y
				ymap.set(y, new ArrayList<>());

				List<Integer> xMap = ymap.get(y);

				// create x values
				for (int x = 0; x < demoMapSize; x++) {
					xMap.add(0);
				}
			}
			

			// TODO: Serialize to json and save it
		}
	}


	@Override
	public void start(GameData gameData, WorldData world) {
		
		// Ensure file structure exists
		createFileStructure();

		// Create a default tile if it does not exists
		createDefaultTile();

		// Create a defualt mission if it does not exists
		createDefaultMission();

		// TODO: Load a map of all tiles

		// Load Common.Mission data from all mission files, and save it to a list

		// Create a reference to MissionLoaderService in worldData


	}

	@Override
	public void stop(GameData gameData, WorldData world) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'stop'");
	}
    
}