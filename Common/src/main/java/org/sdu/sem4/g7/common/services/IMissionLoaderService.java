package org.sdu.sem4.g7.common.services;

import java.util.List;

import org.sdu.sem4.g7.common.data.Mission;

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
     * Loads a specific mission into worldData, and returns it
     * 
     * @param id
     * @return mission or null if not exists 
     */
    public Mission loadMission(int id);


    /**
     * Unloads the current mission from worldData
     */
    public void unloadMission();

}
