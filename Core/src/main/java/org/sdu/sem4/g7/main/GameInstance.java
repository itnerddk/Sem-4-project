package org.sdu.sem4.g7.main;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.sdu.sem4.g7.UI.controllers.GameResultController;
import org.sdu.sem4.g7.UI.controllers.PauseMenuController;
import org.sdu.sem4.g7.common.data.*;
import org.sdu.sem4.g7.common.data.GameData.Keys;
import org.sdu.sem4.g7.common.enums.EntityType;
import org.sdu.sem4.g7.common.enums.SoundType;
import org.sdu.sem4.g7.common.services.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class GameInstance {

    private final GameData gameData;
    private final WorldData worldData;
    
    private AnimationTimer animationTimer;
    
    private final StackPane rootPane = new StackPane();
    private final Pane gameWindow = new Pane();
    private final int missionId;
    private final Canvas gameCanvas;
    private final Map<Entity, Node> sprites = new ConcurrentHashMap<>();
    private final Group debugGroup = new Group();
    private static final Text debugText = new Text(10, 20, "");
    private boolean paused = false;
    private Parent pauseMenu;
    
    
    private final Collection<IGamePluginService> pluginServices;

    public GameInstance(GameData gameData, WorldData worldData, int missionId) {
        this.missionId = missionId;
        this.gameData = gameData;
        this.worldData = worldData;
        this.gameCanvas = new Canvas(gameData.getMissionLoaderService().getMapSizeX(), gameData.getMissionLoaderService().getMapSizeY());
        gameData.gc = gameCanvas.getGraphicsContext2D();
        this.pluginServices = loadServices(IGamePluginService.class);

        setupCanvas();
        startPlugins();
        loadEntities();
        startRenderLoop();
    }

    private void setupCanvas() {
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameCanvas.setViewOrder(-9999);
        gameWindow.getChildren().addAll(gameCanvas, debugText);
        rootPane.getChildren().add(gameWindow);
        rootPane.setStyle("-fx-background-color: black;");
        createPauseMenu();
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
        this.animationTimer = new AnimationTimer() {
            long lastTick = 0;

            @Override
            public void handle(long now) {
                if (now - lastTick >= 28_000_000) {
                    if (paused) {
                        return;
                    }
                    GraphicsContext gc = gameCanvas.getGraphicsContext2D();
                    gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
                    update();
                    gameData.updateKeys();
                    gameData.setDelta((now - lastTick) * 1.0e-9);
                    // gameData.addDebug("Entity Count", String.valueOf(worldData.getEntities().size()));
                    // gameData.addDebug("Delta", String.valueOf((Math.round(gameData.getDelta() * 10000) / 10.0)));
                    lastTick = now;
                    draw();

                    try {
                        if (worldData.isGameWon()) {
                            float difficulty = gameData.getMissionLoaderService().getMission(missionId - 1).getDifficulty();
                            System.out.println("Difficulty: " + difficulty);
                            gameData.setScoreTarget((int)(1000 * difficulty));
                            int timePenalty = (int)(gameData.getTime() * gameData.getScoreTarget() / 120); // 120 seconds
                            int finalScore = Math.max(0, gameData.getScoreTarget() - timePenalty);
                            
                            // Get the mission difficulty and multiply it on
                            finalScore *= difficulty;

                            List<Entity> players = worldData.getEntities(EntityType.PLAYER);
                            if (players.size() >= 1) {
                                Entity player = players.iterator().next();
                                int healthBonus = (int) (100.0f * (((float) player.getHealth() / (float) player.getMaxHealth()) * difficulty));
                                finalScore += healthBonus;
                            }



                            gameData.setScore(finalScore);
                            gameData.setCoinsEarned((int)(finalScore * 0.25)); // midlertidigt hardcoded

                            // Reward the player with coins
                            ServiceLocator.getCurrencyService().ifPresent(service -> {
                                ServiceLocator.getPersistenceService().ifPresent(persistenceService -> {
                                    // Check if mission has been completed, so the player earn less money
                                    if(persistenceService.intListExists("completedMissions")){
                                        if(persistenceService.getIntList("completedMissions").contains(missionId)){
                                            System.out.println("Mission already completed");
                                            gameData.setCoinsEarned((int) (gameData.getCoinsEarned() * 0.25)); //TODO: temp amount, need to figure out how much money should be earned per mission
                                        }
                                    }
                                    service.addCurrency(gameData.getCoinsEarned());
                                });
                            });

                            // Reward the player with XP
                            ServiceLocator.getLevelService().ifPresent(service -> {
                                service.addXP((int) (gameData.getScore() * 0.1)); //TODO: temp amount
                            });
                            // Save the mission as completed
                            ServiceLocator.getPersistenceService().ifPresent(service -> {
                                List<Integer> compltedMission;
                                if (service.intListExists("completedMissions")) {
                                    compltedMission = service.getIntList("completedMissions");
                                } else {
                                    compltedMission = new ArrayList<>();
                                }
                                if(!compltedMission.contains(missionId)) {
                                    compltedMission.add(missionId);
                                    service.saveIntList("completedMissions", compltedMission);
                                }
                                
                            });

                            showResultOverlay(true, finalScore, 10000, gameData.getCoinsEarned());
                            animationTimer.stop();
                            return;
                        } else if (worldData.isGameLost()) {
                            gameData.setScoreTarget(10000);
                            gameData.setScore(0);
                            gameData.setCoinsEarned(0);
                            showResultOverlay(false, 0, 10000, 0);
                            animationTimer.stop();
                            return;
                        }

                    } catch (IOException ex) {
                        System.err.println("Could not load GameResult.fxml");
                        ex.printStackTrace();
                    }
                }
            }
        };
        animationTimer.start();
    }

    private void showResultOverlay(boolean isWin, int score, int target, int coins) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GameResult.fxml"));
        Parent overlay = loader.load();

        GameResultController controller = loader.getController();
        controller.init(isWin, score, target, coins);

        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.75);");

        FadeTransition ft = new FadeTransition(Duration.seconds(0.5), overlay);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        rootPane.getChildren().add(overlay);
    }

    private void createPauseMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PauseMenu.fxml"));
            pauseMenu = loader.load();
            pauseMenu.setStyle("-fx-background-color: rgba(0,0,0,0.75);");

            PauseMenuController controller = loader.getController();
            controller.setGameInstance(this);
            controller.setGameData(gameData);
            controller.setMissionId(missionId);

            rootPane.getChildren().add(pauseMenu);
            pauseMenu.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resumeGame() {
        paused = false;
        pauseMenu.setVisible(false);
    }

    private void update() {
        for (IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, worldData);
        }
        for (IPostEntityProcessingService processor : getPostEntityProcessingServices()) {
            processor.process(gameData, worldData);
        }
    }

    private void draw() {
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        // gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

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
        Scene scene = new Scene(rootPane);
        scene.setFill(Color.BLACK);
        scene.setOnKeyPressed(event -> setupKeys(event, true));
        scene.setOnKeyReleased(event -> setupKeys(event, false));
        scene.getStylesheets().add(getClass().getResource("/view/style.css").toExternalForm());
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
            case TAB:
                this.gameData.setPressed(Keys.TAB, pressed);
                break;
            case ESCAPE:
                if (pressed) {
                    ServiceLocator.getAudioProcessingService().ifPresent(
                        service -> {
                            service.playSound(SoundType.BUTTON_CLICK, 1.0f);
                        }
                    );
                    paused = !paused;
                    pauseMenu.setVisible(paused);
                }
            default:
                break;
        }
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return ServiceLoader.load(IEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }

    private Collection<? extends IPostEntityProcessingService> getPostEntityProcessingServices() {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
