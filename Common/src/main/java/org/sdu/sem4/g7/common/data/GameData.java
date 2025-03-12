package org.sdu.sem4.g7.common.data;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameData {

    private int displayWidth  = 800;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();
    private double delta;

    private ArrayList<Mission> missions = new ArrayList<>();

    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    public double getDelta() {
        return this.delta;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public ArrayList<Mission> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<Mission> missions) {
        this.missions = missions;
    }


    public Map<String, String> debugMap = new ConcurrentHashMap<>();

    public void addDebug(String key, String value) {
        if (debugMap.containsKey(key)) {
            debugMap.replace(key, value);
        } else {
            debugMap.put(key, value);
        }
    }

}
