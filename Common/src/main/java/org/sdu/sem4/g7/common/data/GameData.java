package org.sdu.sem4.g7.common.data;

import java.util.ArrayList;

public class GameData {

    private int displayWidth  = 800 ;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();

    private ArrayList<Mission> missions = new ArrayList<>();
    private ArrayList<Entity> turrets = new ArrayList<>();


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

    public void addMission(Mission mission) {
        missions.add(mission);
    }

    public ArrayList<Mission> getMissions() {
        return missions;
    }

    public void setMissions(ArrayList<Mission> missions) {
        this.missions = missions;
    }

    public ArrayList<Entity> getTurrets() {
        return turrets;
    }

    public void addTurrets(Entity turret) {
        this.turrets.add(turret);
    }

    public void setTurrets(ArrayList<Entity> turrets) {
        this.turrets = turrets;
    }

}
