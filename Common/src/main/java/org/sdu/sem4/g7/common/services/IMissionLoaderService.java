package org.sdu.sem4.g7.common.services;

import java.io.IOException;
import java.util.List;

import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.WorldData;

public interface IMissionLoaderService {
    
    
    /**
	 * Insert the gameData and worldData into the service
	 * @param gameData
	 * @param worldData
	 * @throws IOException
	 */
    public void insert(GameData gameData, WorldData worldData) throws IOException;

    /**
     * Returns a list of all missions available
     * 
     * @return list of missions
     */
    public List<Mission> getMissions();


    /**
     * Returns a specific mission based on its id
     * 
     * @param id
     * @return mission or null if not exists
     */
    public Mission getMission(int id);


    /**
     * Returns numbers of mission available
     * 
     * @return int
     */
    public int missionCount();


    /**
     * Loads a specific mission and returns it
     * 
     * @param id
     * @return WorldData or null if not exists 
     */
    public WorldData loadMission(int id);


    /**
     * Returns the x axis size of the map
     * 
     * @return int size
     */
    public int getMapSizeX();


    /**
     * Returns the y axis size of the map
     * 
     * @return int size
     */
    public int getMapSizeY();

}
