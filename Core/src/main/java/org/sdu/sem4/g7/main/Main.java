package org.sdu.sem4.g7.main;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameKeys;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;
import org.sdu.sem4.g7.tank.parts.Tank;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private WorldData worldData;
    private final Map<Entity, Node> sprites = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Canvas overlayCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());

    public static void main(String[] args) {
        launch(Main.class);
    }

    public static Text debugText = new Text(10, 20, "");

    @Override
    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(debugText);
        // Draw the overlay canvas for health bar
        overlayCanvas.setViewOrder(-9999);
        gameWindow.getChildren().add(overlayCanvas);

        Scene scene = new Scene(gameWindow);

        scene.getStylesheets().add(this.getClass().getClassLoader().getResource("style.css").toExternalForm());

        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                gameData.getKeys().setKey(GameKeys.DOWN, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.DOWN)) {
                gameData.getKeys().setKey(GameKeys.DOWN, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }

        });

        // Lookup all Game Plugins using ServiceLoader
        for (IPreGamePluginService iPrePluginService : getPrePluginServices()) {
            iPrePluginService.start(gameData, worldData);
        }

        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, worldData);
        }

        //System.out.println("worldDatas loaded: " + gameData.getworldDatas().size());
        //this.worldData = gameData.getworldDatas().get(0);

        // Load worldData 0 for testing
        System.out.println("worldDatas available: " + gameData.getMissionLoaderService().missionCount());
        this.worldData = gameData.getMissionLoaderService().loadMission(1); // Loading mission 0 for testing
        

        // System.out.println("Turrets loaded: " + gameData.getTurrets().size());

        for (Entity entity : worldData.getEntities()) {
            sprites.put(entity, entity.getSprite());
            gameWindow.getChildren().add(entity.getSprite());
        }
        render();
        window.setScene(scene);
        window.setTitle("TANK WARS");
        window.show();
    }

    private void render() {
        new AnimationTimer() {
            long lastTick;
            @Override
            public void handle(long now) {
                // Framerate cap
                if (now - lastTick >= 28_000_000) {
                    update();
                    gameData.getKeys().update();
                    gameData.setDelta((now - lastTick) * 1.0e-9);
                    gameData.addDebug("Entity Count", String.valueOf(worldData.getEntities().size()));
                    gameData.addDebug("Delta", String.valueOf((Math.round(gameData.getDelta() * 10000) / 10.0))); // Turning nano seconds into ms
                    lastTick = now;
                    draw();
                }
            }

        }.start();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, worldData);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, worldData);
        }       
    }

    Group debugGroup = new Group();

    private void draw() {
        GraphicsContext gc = overlayCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, overlayCanvas.getWidth(), overlayCanvas.getHeight()); // Clear previous drawings

        // If the entity is gone from the world, we check if the entity is an instance of a tank, if so we cast it to the Tank type, and for each tank we draw a health bar
        if (worldData != null) {
            // If the entity is gone from the world, remove the sprite and entity from the sprites buffer
            for (Entity spriteEntity : sprites.keySet()) {
                if(!worldData.getEntities().contains(spriteEntity)){   
                    ImageView removedSprite = (ImageView) sprites.get(spriteEntity);               
                    sprites.remove(spriteEntity);
                    gameWindow.getChildren().remove(removedSprite);
                }
            }
            // Debugging
            if (gameData.isDebugMode()) {
                debugGroup.getChildren().clear();
                debugGroup.viewOrderProperty().set(1000);
                gameWindow.getChildren().remove(debugGroup);
                for (Node node : gameData.debugEntities.values()) {
                    debugGroup.getChildren().add(node);
                }
                gameWindow.getChildren().add(debugGroup);
            }

            // Iterate through all entities in the world and update their position and rotation
            for (Entity entity : worldData.getEntities()) {          
                ImageView sprite = (ImageView) sprites.get(entity);
                if (sprite == null) {
                    sprite = entity.getSprite();
                    sprites.put(entity, sprite);
                    gameWindow.getChildren().add(sprite);
                }
                entity.render(gc);
            }
        }
        debugText.setText("");
        for (String key : gameData.debugMap.keySet()) {
            // System.out.println(key + ": " + gameData.debugMap.get(key));
            debugText.setText(debugText.getText() + key + ": " + gameData.debugMap.get(key) + "\n");
        }
    }

    private Collection<? extends IPreGamePluginService> getPrePluginServices() {
        return ServiceLoader.load(IPreGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IGamePluginService> getPluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
