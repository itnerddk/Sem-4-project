package org.sdu.sem4.g7.common.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.services.IMissionLoaderService;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameData {

    private int displayWidth  = 800;
    private int displayHeight = 800;
    private final GameKeys keys = new GameKeys();
    private double delta;

    // TODO: This is maybe not the best way, but let's change it later
    private IMissionLoaderService missionLoaderService;

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

    //#region Debug

    public Map<String, Node> debugEntities = new ConcurrentHashMap<>();
    public Map<String, String> debugMap = new ConcurrentHashMap<>();
    private final boolean debugMode = true;

    public void addDebug(String key, String value) {
        if (debugMap.containsKey(key)) {
            debugMap.replace(key, value);
        } else {
            debugMap.put(key, value);
        }
    }
    
    public void addDebugCircle(String key, Entity parent, float radius) {
        Node e = new Circle(radius);
        e.setTranslateX(parent.getPosition().getX() + parent.getSprite().getImage().getWidth() / 2);
        e.setTranslateY(parent.getPosition().getY() + parent.getSprite().getImage().getHeight() / 2);
        e.setRotate(parent.getRotation());
        if (debugEntities.containsKey(key)) {
            debugEntities.replace(key, e);
        } else {
            debugEntities.put(key, e);
        }
    }

    public void addDebugRectangle(String key, Entity parent, float width, float height) {
        Node e = new javafx.scene.shape.Rectangle(width, height);
        e.getStyleClass().add("debug-rect");
        e.setTranslateX(parent.getPosition().getX());
        e.setTranslateY(parent.getPosition().getY());
        e.setRotate(parent.getRotation());
        if (debugEntities.containsKey(key)) {
            debugEntities.replace(key, e);
        } else {
            debugEntities.put(key, e);
        }
    }

    public void addDebugLine(String key, Vector2 from, Vector2 to, float length) {
        Node e = new Line(from.getX(), from.getY(), to.getX(), to.getY());
        if (debugEntities.containsKey(key)) {
            debugEntities.replace(key, e);
        } else {
            debugEntities.put(key, e);
        }
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public IMissionLoaderService getMissionLoaderService() {
        return missionLoaderService;
    }

    public void setMissionLoaderService(IMissionLoaderService missionLoaderService) {
        this.missionLoaderService = missionLoaderService;
    }
}
