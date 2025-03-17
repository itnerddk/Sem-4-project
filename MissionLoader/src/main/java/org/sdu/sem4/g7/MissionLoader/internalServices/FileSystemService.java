package org.sdu.sem4.g7.MissionLoader.internalServices;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sdu.sem4.g7.MissionLoader.config.Config;
import org.sdu.sem4.g7.MissionLoader.objects.EnemyStartPositionObject;
import org.sdu.sem4.g7.MissionLoader.objects.MissionObject;
import org.sdu.sem4.g7.MissionLoader.objects.PlayerStartPositionObject;
import org.sdu.sem4.g7.MissionLoader.objects.TileObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class FileSystemService {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    /**
	 * Creates filestructure for Mission data, if it does not already exists
	 */
	public void createFileStructure() {
		// Create datadir if it does not exists
		if (!Config.dataDir.isDirectory()) {
			System.out.println(" - Creating data dir!");
			Config.dataDir.mkdirs();
		}

		// create missions dir if it does not exists
		if (!Config.missionsDir.isDirectory()) {
			System.out.println(" - Creating missions dir!");
			Config.missionsDir.mkdir();
		}

		// create tiles dir if it does not exists
		if (!Config.tilesDir.isDirectory()) {
			System.out.println(" - Creating tiles dir!");
			Config.tilesDir.mkdir();
		}
	}

	/*
	 * Creates a default tile, if it does not exists
	 */
	public void createDefaultTile() throws IOException {
		if (!new File(Config.tilesDir, "0.json").exists()) {
			System.out.println(" - Creating default tile!");

			// Create a new tile
			TileObject tile = new TileObject();
			tile.setId(0);
			tile.setImage("0.png"); // images will be loaded from the tiles dir
			tile.setZ(1);
			tile.setCollision(true);
			tile.setImmoveable(true);
			tile.setHealth(1);

			// Serialize and save as json
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.tilesDir, "0.json")));
			writer.write(objectMapper.writeValueAsString(tile));
			writer.close();
		}
	}

	/*
	 * Create a default mission, if it does not exists
	 * 
	 * NOTE: I do this, in order to create a complete json file. With everything set just as the serializer expects it.
	 */
	public void createDefaultMission() throws IOException {
		if (!new File(Config.missionsDir, "0.json").exists()) {
			System.out.println(" - Creating default mission!");

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
				ymap.add(new ArrayList<>());

				List<Integer> xMap = ymap.get(y);

				// create x values
				for (int x = 0; x < demoMapSize; x++) {
					xMap.add(0);
				}
			}
			

			// Serialize and save as json
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(Config.missionsDir, "0.json")));
			writer.write(objectMapper.writeValueAsString(mo));
			writer.close();
		}
	}
}
