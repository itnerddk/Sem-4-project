package org.sdu.sem4.g7.MissionLoader.config;

import java.io.File;

/*
 * Provides the config values for this module
 */
public class Config {
    /*
     * Pixel size of a tile
     */
    public final static int tileSize = 6;

    public final static File dataDir = new File("./data");
	public final static File missionsDir = new File(dataDir, "missions");
	public final static File tilesDir = new File(dataDir, "tiles");
}
