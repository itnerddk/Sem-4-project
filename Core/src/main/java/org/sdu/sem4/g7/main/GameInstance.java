package org.sdu.sem4.g7.main;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import org.sdu.sem4.g7.common.data.Entity;
import org.sdu.sem4.g7.common.data.GameData;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.data.WorldData;
import org.sdu.sem4.g7.common.services.IEntityProcessingService;
import org.sdu.sem4.g7.common.services.IGamePluginService;
import org.sdu.sem4.g7.common.services.IPostEntityProcessingService;
import org.sdu.sem4.g7.common.services.IPreGamePluginService;

import java.util.Collection;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

public class GameInstance {

    private final GameData gameData = new GameData();
    private WorldData worldData;
    private final Map<Entity, Node> sprites = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Canvas overlayCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
    public static Text debugText = new Text(10, 20, "");
    private final Group debugGroup = new Group();

    public GameInstance() {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(debugText);
        overlayCanvas.setViewOrder(-9999);
        gameWindow.getChildren().add(overlayCanvas);

        for (IPreGamePluginService plugin : getPrePluginServices()) {
            plugin.start(gameData, worldData);
        }
        for (IGamePluginService plugin : getPluginServices()) {
            plugin.start(gameData, worldData);
        }

        this.worldData = gameData.getMissionLoaderService().loadMission(1);

        for (Entity entity : worldData.getEntities()) {
            sprites.put(entity, entity.getSprite());
            gameWindow.getChildren().add(entity.getSprite());
        }

        render();
    }

    public Scene getScene() {
        Scene scene = new Scene(gameWindow);
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        scene.setOnKeyPressed(event -> setupKeys(event, true));
        scene.setOnKeyReleased(event -> setupKeys(event, false));
        return scene;
    }

    private void setupKeys(KeyEvent event, boolean pressed) {
        switch (event.getCode()) {
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
                if (now - lastTick >= 28_000_000) {
                    update();
                    gameData.updateKeys();
                    gameData.setDelta((now - lastTick) * 1.0e-9);
                    gameData.addDebug("Entity Count", String.valueOf(worldData.getEntities().size()));
                    gameData.addDebug("Delta", String.valueOf((Math.round(gameData.getDelta() * 10000) / 10.0)));
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

    private void draw() {
        GraphicsContext gc = overlayCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, overlayCanvas.getWidth(), overlayCanvas.getHeight());

        if (worldData != null) {
            for (Entity spriteEntity : sprites.keySet()) {
                if(!worldData.getEntities().contains(spriteEntity)){
                    ImageView removedSprite = (ImageView) sprites.get(spriteEntity);
                    sprites.remove(spriteEntity);
                    gameWindow.getChildren().remove(removedSprite);
                }
            }
            if (gameData.isDebugMode()) {
                debugGroup.getChildren().clear();
                debugGroup.viewOrderProperty().set(1000);
                gameWindow.getChildren().remove(debugGroup);
                for (Node node : gameData.debugEntities.values()) {
                    debugGroup.getChildren().add(node);
                }
                gameWindow.getChildren().add(debugGroup);
            }

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