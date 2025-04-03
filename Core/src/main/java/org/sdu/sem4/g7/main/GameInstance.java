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
import javafx.scene.paint.Color;

import org.sdu.sem4.g7.common.data.*;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.services.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class GameInstance {

    private final GameData gameData;
    private final WorldData worldData;

    private final Pane gameWindow = new Pane();
    private final Canvas gameCanvas;
    private final Map<Entity, Node> sprites = new ConcurrentHashMap<>();
    private final Group debugGroup = new Group();
    private static final Text debugText = new Text(10, 20, "");

    private final Collection<IGamePluginService> pluginServices;
    private final Collection<IEntityProcessingService> entityProcessors;
    private final Collection<IPostEntityProcessingService> postProcessors;

    public GameInstance(GameData gameData, WorldData worldData) {
        this.gameData = gameData;
        this.worldData = worldData;

        this.gameCanvas = new Canvas(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        this.pluginServices = loadServices(IGamePluginService.class);
        this.entityProcessors = loadServices(IEntityProcessingService.class);
        this.postProcessors = loadServices(IPostEntityProcessingService.class);

        setupCanvas();
        startPlugins();
        loadEntities();
        startRenderLoop();
    }

    private void setupCanvas() {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameCanvas.setViewOrder(-9999);
        gameWindow.getChildren().addAll(gameCanvas, debugText);
        gameWindow.setStyle("-fx-background-color: black;");
    }

    private void startPlugins() {
        for (IGamePluginService plugin : pluginServices) {
            plugin.start(gameData, worldData);
        }
    }

    private void loadEntities() {
        for (Entity entity : worldData.getEntities()) {
            Node sprite = entity.getSprite();
            if (sprite != null) {
                sprites.put(entity, sprite);
                gameWindow.getChildren().add(sprite);
            }
        }
    }

    private void startRenderLoop() {
        new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (now - lastTick >= 28_000_000) {
                    update();
                    draw();
                    lastTick = now;
                }
            }
        }.start();
    }

    private void update() {
        gameData.updateKeys();
        for (IEntityProcessingService processor : entityProcessors) {
            processor.process(gameData, worldData);
        }
        for (IPostEntityProcessingService processor : postProcessors) {
            processor.process(gameData, worldData);
        }
    }

    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Follow player
        worldData.getEntities().stream()
                .filter(e -> e.getEntityType() == EntityType.PLAYER)
                .findFirst()
                .ifPresent(player -> {
                    Vector2 pos = new Vector2(player.getPosition()).multiply(-1)
                            .add(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
                    gameWindow.setTranslateX(pos.getX());
                    gameWindow.setTranslateY(pos.getY());
                });

        // Sync entities and render
        for (Entity entity : worldData.getEntities()) {
            ImageView sprite = (ImageView) sprites.get(entity);
            if (sprite == null) {
                sprite = entity.getSprite();
                if (sprite != null) {
                    sprites.put(entity, sprite);
                    if (!gameWindow.getChildren().contains(sprite)) {
                        gameWindow.getChildren().add(sprite);
                    }
                }
            }
            entity.render(gc);
        }

        // Remove deleted entities
        sprites.keySet().removeIf(entity -> {
            if (!worldData.getEntities().contains(entity)) {
                Node sprite = sprites.remove(entity);
                if (sprite != null) {
                    gameWindow.getChildren().remove(sprite);
                }
                return true;
            }
            return false;
        });



        // Games doesn't load properly if debug is removed.
        // Debug overlay
        if (gameData.isDebugMode()) {
            debugGroup.getChildren().clear();
            debugGroup.viewOrderProperty().set(1000);
            gameWindow.getChildren().remove(debugGroup);
            for (Node node : gameData.debugEntities.values()) {
                debugGroup.getChildren().add(node);
            }
            gameWindow.getChildren().add(debugGroup);
        }

        // Debug text
        debugText.setText("");
        for (String key : gameData.debugMap.keySet()) {
            debugText.setText(debugText.getText() + key + ": " + gameData.debugMap.get(key) + "\n");
        }
    }

    private <T> Collection<T> loadServices(Class<T> type) {
        return ServiceLoader.load(type).stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }

    public Pane getGameView() {
        return gameWindow;
    }

    public Scene getScene() {
        Scene scene = new Scene(gameWindow);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(event -> setupKeys(event, true));
        scene.setOnKeyReleased(event -> setupKeys(event, false));
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
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
                System.out.println("Shooting");
                break;
            default:
                break;
        }
    }
}
