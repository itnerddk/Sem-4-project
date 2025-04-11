package org.sdu.sem4.g7.common.services;

import java.util.List;

/*
 * Service responsable for saving data to persistent storage
 */
public interface IPersistenceService {

    /**
     * 
     * Get string
     * 
     * @param key unique key
     * @return value
     */
    public String getString(String key);


    /**
     * 
     * Set string
     * 
     * @param key unique key
     * @param value
     */
    public void setString(String key, String value);


    /**
     * 
     * Check if key exists
     * 
     * @param key unique key
     * @return true if exists
     */
    public boolean stringExists(String key);

    /**
     * 
     * Get integer
     * 
     * @param key unique key
     * @return value
     */
    public int getInt(String key);


    /**
     * 
     * Set integer
     * 
     * @param key unique key
     * @param value
     */
    public void setInt(String key, int value);

    /**
     * 
     * Check if key exists
     * 
     * @param key unique key
     * @return true if exists
     */
    public boolean intExists(String key);
    

    /**
     * 
     * Get a saved list
     * 
     * @param key unique list key
     * @return List of strings
     */
    public List<String> getStringList(String key);


    /**
     * 
     * Save a list of strings
     * 
     * @param key unique list key
     * @param value list of strings
     */
    public void saveStringList(String key, List<String> value);


    /**
     * 
     * Check if a list exists
     * 
     * @param key unique list key
     * @return true if list exists
     */
    public boolean stringListExists(String key);


    /**
     * 
     * Get a saved list
     * 
     * @param key unique list key
     * @return List of ints
     */
    public List<Integer> getIntList(String key);


    /**
     * 
     * Save a list of ints
     * 
     * @param key unique list key
     * @param value list of ints
     */
    public void saveIntList(String key, List<Integer> value);


    /**
     * 
     * Check if a list exists
     * 
     * @param key unique list key
     * @return true if list exists
     */
    public boolean intListExists(String key);

    

}
