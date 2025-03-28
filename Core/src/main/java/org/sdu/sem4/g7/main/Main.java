package org.sdu.sem4.g7.main;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.data.Vector2;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private WorldData worldData;
    private final Map<Entity, Node> sprites = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Canvas gameCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());

    public static void main(String[] args) {
        launch(Main.class);
    }

    public static Text debugText = new Text(10, 20, "");

    @Override
    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(debugText);
        // Draw the overlay canvas for health bar
        gameCanvas.setViewOrder(-9999);
        gameWindow.getChildren().add(gameCanvas);

        Scene scene = new Scene(gameWindow);
        scene.setFill(javafx.scene.paint.Color.BLACK);

        scene.getStylesheets().add(this.getClass().getClassLoader().getResource("style.css").toExternalForm());

        scene.setOnKeyPressed(event -> setupKeys(event, true));
        scene.setOnKeyReleased(event -> setupKeys(event, false));

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
        gameCanvas.setWidth(gameData.getMissionLoaderService().getMapSizeX());
        gameCanvas.setHeight(gameData.getMissionLoaderService().getMapSizeY());

        window.widthProperty().addListener((obs, oldVal, newVal) -> {
            gameData.setDisplayWidth(newVal.intValue());
        });
        window.heightProperty().addListener((obs, oldVal, newVal) -> {
            gameData.setDisplayHeight(newVal.intValue());
        });
        

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

    void setupKeys(KeyEvent event, boolean pressed) {
        switch(event.getCode()) {
            case UP:
            case W:
                this.gameData.setPressed(Keys.UP, pressed);
                break;
            case RIGHT:
            case D:
                this.gameData.setPressed(Keys.RIGHT, pressed);
                break;
            case DOWN:
            case S:
                this.gameData.setPressed(Keys.DOWN, pressed);
                break;
            case LEFT:
            case A:
                this.gameData.setPressed(Keys.LEFT, pressed);
                break;
            case SPACE:
                this.gameData.setPressed(Keys.SPACE, pressed);
                break;
                default:
                break;
        }
    }

    private void render() {
        new AnimationTimer() {
            long lastTick;
            @Override
            public void handle(long now) {
                // Framerate cap
                if (now - lastTick >= 28_000_000) {
                    update();
                    gameData.updateKeys();
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
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()); // Clear previous drawings

        // Center the game window on the player
        for (Entity entity : worldData.getEntities()) {
            if (entity.getEntityType() == EntityType.PLAYER) {
                Vector2 playerPos = new Vector2(entity.getPosition()).multiply(-1);
                playerPos.add(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);

                Vector2 windowPos = new Vector2(gameWindow.getTranslateX(), gameWindow.getTranslateY());

                windowPos.lerp(playerPos, 5 * gameData.getDelta());
                System.out.println(gameData.getDelta() * 5);

                gameWindow.setTranslateX(windowPos.getX());
                gameWindow.setTranslateY(windowPos.getY());
            }
        }

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
