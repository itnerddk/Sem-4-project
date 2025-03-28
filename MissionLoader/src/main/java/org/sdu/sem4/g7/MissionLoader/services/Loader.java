package org.sdu.sem4.g7.MissionLoader.services;

import java.io.IOException;

import org.sdu.sem4.g7.MissionLoader.internalServices.FileSystemService;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IGamePluginService;




/*
 * Game plugin
 */
public class Loader implements IGamePluginService {

	@Override
	public void start(GameData gameData, WorldData world) {


		// Ensure filestructure is created, and create example missions if none exists
		FileSystemService fss = new FileSystemService();

		try {
			// Ensure file structure exists
			fss.createFileStructure();

			// Create a default tile if it does not exists
			fss.createDefaultTile();

			// Create a defualt mission if it does not exists
			fss.createDefaultMission();
		} catch (IOException ex) {
			System.err.println("Could not write default file(s) to disk! Please see the MissionLoader...");
			ex.printStackTrace();
		}

		// Create an instance of MissionLoaderService and set the reference in gameData
		try {
			gameData.setMissionLoaderService(new MissionLoaderService(gameData, world));
		} catch (IOException e) {
			System.err.println("Could not create a MissionLoaderService!");
			e.printStackTrace();
		}




	}

	@Override
	public void stop(GameData gameData, WorldData world) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'stop'");
	}
    
}