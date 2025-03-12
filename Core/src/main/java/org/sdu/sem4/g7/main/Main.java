package org.sdu.sem4.g7.main;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameKeys;
import org.sdu.sem4.g7.common.data.Mission;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import static java.util.stream.Collectors.toList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    private final GameData gameData = new GameData();
    private Mission mission;
    private final Map<Entity, ImageView> sprites = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();

    public static void main(String[] args) {
        launch(Main.class);
    }

    public static Text debugText = new Text(10, 20, "");

    @Override
    public void start(Stage window) {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(debugText);

        Scene scene = new Scene(gameWindow);
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

        // Lookup all Game Plugins using ServiceLoader, this is also where missions load
        for (IPreGamePluginService iPrePluginService : getPrePluginServices()) {
            iPrePluginService.start(gameData, mission);
        }

        for (IGamePluginService iGamePlugin : getPluginServices()) {
            iGamePlugin.start(gameData, mission);
        }

        System.out.println("Missions loaded: " + gameData.getMissions().size());
        this.mission = gameData.getMissions().get(0);

        System.out.println("Turrets loaded: " + gameData.getTurrets().size());

        for (Entity entity : mission.getEntities()) {
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
                    gameData.addDebug("Delta", String.valueOf((Math.round(gameData.getDelta() * 10000) / 10.0))); // Turning nano seconds into ms
                    lastTick = now;
                }
                draw();
            }

        }.start();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, mission);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, mission);
        }       
    }

    private void draw() {
        // If the entity is gone from the world, remove the sprite and entity from the sprites buffer
        for (Entity spriteEntity : sprites.keySet()) {
            if(!mission.getEntities().contains(spriteEntity)){   
                ImageView removedSprite = sprites.get(spriteEntity);               
                sprites.remove(spriteEntity);
                gameWindow.getChildren().remove(removedSprite);
            }
        }
        // Iterate through all entities in the world and update their position and rotation
        for (Entity entity : mission.getEntities()) {                      
            ImageView sprite = sprites.get(entity);
            if (sprite == null) {
                sprite = new ImageView();
                sprites.put(entity, sprite);
                gameWindow.getChildren().add(sprite);
            }
            sprite.setTranslateX(entity.getPosition().getX());
            sprite.setTranslateY(entity.getPosition().getY());
            sprite.setRotate(entity.getRotation());
        }
        debugText.setText("");
        for (String key : gameData.debugMap.keySet()) {
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
