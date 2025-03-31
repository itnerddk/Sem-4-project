package org.sdu.sem4.g7.common.data;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.sdu.sem4.g7.common.services.IAudioProcessingService;
import org.sdu.sem4.g7.common.services.IMissionLoaderService;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GameData {

    private int displayWidth  = 800;
    private int displayHeight = 800;
    private double delta;

    // TODO: This is maybe not the best way, but let's change it later
    private IMissionLoaderService missionLoaderService;
    private IAudioProcessingService audioProcessingService;

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

    private long startTime;

    public GameData() {
        // Initialize the start time when the game begins
        startTime = System.currentTimeMillis();

        // Initialize the keys
        for (Keys key : Keys.values()) {
            keys.put(key, false);
            keysLast.put(key, false);
        }
    }

    // Get the elapsed time in seconds
    public double getTime() {
        // Get the current time in milliseconds and convert to seconds
        long currentTime = System.currentTimeMillis();
        return (currentTime - startTime) / 1000.0;
    }


    // Key handling
    // Key data
    public enum Keys {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        SPACE
    }
    
    EnumMap<Keys, Boolean> keys = new EnumMap<>(Keys.class);
    EnumMap<Keys, Boolean> keysLast = new EnumMap<>(Keys.class);
    
    public void setPressed(Keys key, boolean pressed) {
        this.keys.put(key, pressed);
    }
    public boolean isDown(Keys key) {
        return keys.get(key);
    }
    public boolean isPressed(Keys key) {
        return keys.get(key) && !keysLast.get(key);
    }
    public boolean isReleased(Keys key) {
        return !keys.get(key) && keysLast.get(key);
    }
    public void updateKeys() {
        keysLast.putAll(keys);
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

    public IAudioProcessingService getAudioProcessingService() {
        return audioProcessingService;
    }
    
    public void setAudioProcessingService(IAudioProcessingService audioProcessingService) {
        this.audioProcessingService = audioProcessingService;
    }

    /**
     * Plays an audio file with the given name and volume.
     * @param soundName the name of the sound file to play (without extension)
     * @param volume the volume level (0.0 to 1.0)
     */
    public void playAudio(String soundName, float volume) {
        if (audioProcessingService != null) {
            audioProcessingService.playSound(soundName, volume);
        } else {
            System.err.println("Audio processing service is not set.");
        }
    }
}
