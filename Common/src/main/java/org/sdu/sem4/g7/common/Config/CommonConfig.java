package org.sdu.sem4.g7.common.Config;

public class CommonConfig {
    public static boolean DEBUG = false;
    public static final int DEFAULT_SCALING = 5;
    private static int TILE_SIZE;

    public static boolean isDEBUG() {
        return DEBUG;
    }
    public static void setDEBUG(boolean dEBUG) {
        DEBUG = dEBUG;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }
    public static void setTileSize(int tileSize) {
        System.out.println("Tile size set to: " + tileSize);
        TILE_SIZE = tileSize;
    }
}
