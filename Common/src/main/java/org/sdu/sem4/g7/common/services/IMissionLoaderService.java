package org.sdu.sem4.g7.common.services;

import java.util.List;

import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.data.WorldData;

public interface IMissionLoaderService {
    
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

    /**
     * Returns if player has died, an thereby lost the game
     * 
     * @return true if lost
     */
    public boolean isGameLost();


    /**
     * Returns if player have won, player wins when there is no more enemies alive
     * 
     * @return true if won
     */
    public boolean isGameWon();

}
