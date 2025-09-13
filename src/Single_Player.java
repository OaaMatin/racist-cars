import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.application.Platform;
import javafx.scene.input.KeyCombination;


public class Single_Player {

    // ---- tuning ----
    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;

    private static final double ACCELERATION = 0.025;
    private static final double MAX_VELOCITY = 20;
    private static final double FINISH_DISTANCE = 50000;

    // stage refs (same pattern as Racing_Game)
    private Stage stageRef;
    private Scene customizeSceneRef;

    // chosen skin
    private final String carImagePath;

    // scene graph/layers
    private StackPane root;     // holds gameLayer + overlays
    private Pane gameLayer;     // only this layer gets blurred
    private RoadMap_Single map;
    private ImageView car;
    private Rectangle finishLine;

    // sizing
    private final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private final double screenWidth  = Screen.getPrimary().getBounds().getWidth();

    // state
    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final ArrayList<Objects_Single> obstacles = new ArrayList<>();
    private final AtomicBoolean carIsMoving = new AtomicBoolean(false);

    private int numberOfLanes = 5;
    private int currentLane   = 2;
    private double laneWidth;

    private double velocity = 0;
    private double distance = 0;
    private double maxDistance = 0;

    private boolean gameOver = false;

    // UI / audio
    private AnimationTimer timer;
    private MediaPlayer player;
    private Label distanceLabel;

    // hearts
    private final int maxCrashes = 37;
    private int livesRemaining = maxCrashes;
    private ImageView heartIcon;
    private Label heartCountLabel;


    public Single_Player(String carPath) {
        this.carImagePath = carPath;
    }

    /**
     * Build the single-player Scene (caller sets the scene on the existing Stage).
     */
    public Scene createScene(Stage stage, Scene customizeScene) {
        this.stageRef = stage;
        this.customizeSceneRef = customizeScene;

        gameLayer = new Pane();
        root = new StackPane(gameLayer);  // root hosts game + overlays
        Scene scene = new Scene(root, screenWidth, screenHeight);

        // focus
        root.setFocusTraversable(true);
        root.requestFocus();

        // music
        try {
            Media media = new Media(getClass().getResource("/resources/musics/TokyoـDrift.mp3").toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(1.0);
            player.setOnReady(player::play);
        } catch (Exception ignored) {
            player = null;
        }

        stage.setOnCloseRequest(e -> cleanup());

        // input (filters so buttons never steal)
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED,  e -> pressedKeys.add(e.getCode()));
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, e -> pressedKeys.remove(e.getCode()));
        scene.setOnMouseClicked(e -> root.requestFocus());

        // map & lanes
        map = new RoadMap_Single(screenWidth, screenHeight);
        laneWidth = map.roadWidth / numberOfLanes;

        // car
        try {
            Image carImage = new Image(getClass().getResource(carImagePath).toExternalForm());
            car = new ImageView(carImage);
            car.setFitWidth(screenWidth / 20);
            car.setFitHeight(screenHeight / 8);

            double halfLane = laneWidth / 2.0;
            double laneCenterX = map.getLayoutX() + map.grassWidth + currentLane * laneWidth + halfLane;
            car.setLayoutX(laneCenterX - car.getFitWidth() / 2.0);
            car.setLayoutY(screenHeight * 0.7);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // optional finish line
        finishLine = new Rectangle(map.getLayoutX() + map.grassWidth, 0, map.roadWidth, 10);
        finishLine.setFill(Color.RED);
        finishLine.setVisible(false);

        // volume control
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setOrientation(javafx.geometry.Orientation.VERTICAL);
        Label speakerLabel = new Label("\uD83D\uDD0A");
        speakerLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");

        VBox volumeControl = new VBox(5, volumeSlider, speakerLabel);
        volumeControl.setAlignment(Pos.CENTER);
        volumeControl.setLayoutX(15);
        volumeControl.layoutYProperty().bind(scene.heightProperty().multiply(0.75));

        if (player != null) {
            player.volumeProperty().bind(volumeSlider.valueProperty());
            volumeSlider.valueProperty().addListener((obs, ov, nv) -> {
                double v = nv.doubleValue();
                speakerLabel.setText(v == 0 ? "\uD83D\uDD07" : (v <= 0.3 ? "\uD83D\uDD08" : (v <= 0.7 ? "\uD83D\uDD09" : "\uD83D\uDD0A")));
            });
        } else {
            volumeSlider.setDisable(true);
            speakerLabel.setText("\uD83D\uDD07");
        }

        // add to game layer
        gameLayer.getChildren().addAll(map, car, finishLine, volumeControl);

        // HUD distance
        distanceLabel = new Label();
        distanceLabel.setTextFill(Color.WHITE);
        distanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        distanceLabel.setLayoutX(screenWidth - 360);
        distanceLabel.setLayoutY(10);
        maxDistance = loadMaxDistance();
        distanceLabel.setText(String.format("Distance: %.0f  |  Max: %.0f", distance, maxDistance));
        gameLayer.getChildren().add(distanceLabel);

        // Single heart + counter "xN"
        heartIcon = new ImageView(new Image(getClass().getResource("/resources/pictures/heart.png").toExternalForm()));
        heartIcon.setFitWidth(40);
        heartIcon.setFitHeight(40);
        heartIcon.setLayoutX(10);
        heartIcon.setLayoutY(10);

        heartCountLabel = new Label("x" + livesRemaining);
        heartCountLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        heartCountLabel.setLayoutX(10 + 40 + 8); // to the right of the heart
        heartCountLabel.setLayoutY(14);

        gameLayer.getChildren().addAll(heartIcon, heartCountLabel);


        // initial obstacles (5 lanes)
        for (int lane = 0; lane < 5; lane++) {
            Objects_Single obj = new Objects_Single(lane);
            obj.setObjectY(-Math.random() * 1500);
            obstacles.add(obj);
            gameLayer.getChildren().add(obj.getObstacle());
        }

        // pause UI (Continue / Rematch / Exit) overlay goes ABOVE game layer
        Pane pauseOverlay = buildPauseOverlay();

        Button pauseBtn = new Button("Pause");
        pauseBtn.setStyle("-fx-background-color:#ff3399; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 20px; -fx-cursor: hand");
        pauseBtn.setPrefSize(150, 40);
        pauseBtn.setLayoutX(15);
        pauseBtn.setLayoutY(screenHeight - pauseBtn.getPrefHeight() - 15);
        pauseBtn.setFocusTraversable(false);
        pauseBtn.setOnAction(e -> {
            timer.stop();
            if (player != null) player.pause();
            pauseOverlay.setVisible(true);
        });

        gameLayer.getChildren().add(pauseBtn);
        root.getChildren().add(pauseOverlay); // overlay on top, not blurred

        // game loop
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (gameOver) return;
                moveCarBetweenLanes(car, pressedKeys, carIsMoving);
                update();
                if (distance >= FINISH_DISTANCE) {
                    gameOver = true;
                    stop();
                    if (distance > maxDistance) {
                        maxDistance = distance;
                        saveMaxDistance();
                    }
                    showWin(); // <-- reached finish: show "You Won!"
                }
            }
        };
        timer.start();

        return scene;
    }

    // ---- Pause overlay (Continue / Rematch / Exit) ----
    private Pane buildPauseOverlay() {
        Pane overlay = new Pane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        overlay.setPrefSize(screenWidth, screenHeight);
        overlay.setVisible(false);

        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px; -fx-cursor: hand");
        continueBtn.setPrefSize(150, 40);

        Button rematchBtn = new Button("Rematch");
        rematchBtn.setStyle("-fx-background-color:#2980b9; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand");
        rematchBtn.setPrefSize(150, 40);

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px; -fx-cursor: hand");
        exitBtn.setPrefSize(150, 40);

        VBox box = new VBox(20, continueBtn, rematchBtn, exitBtn);
        box.setAlignment(Pos.CENTER);

        StackPane container = new StackPane(new Rectangle(screenWidth, screenHeight, Color.TRANSPARENT), box);
        container.setPrefSize(screenWidth, screenHeight);
        StackPane.setAlignment(box, Pos.CENTER);

        overlay.getChildren().add(container);

        continueBtn.setOnAction(e -> {
            overlay.setVisible(false);
            if (!gameOver) {
                timer.start();
                if (player != null) player.play();
            }
        });

        rematchBtn.setOnAction(e -> {
            cleanup(); // stop current timer/music
            Single_Player fresh = new Single_Player(carImagePath);
            Scene newScene = fresh.createScene(stageRef, customizeSceneRef);
            stageRef.setScene(newScene);

            // Re-enter fullscreen *after* the scene is attached
            stageRef.setFullScreenExitHint("");
            stageRef.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            Platform.runLater(() -> stageRef.setFullScreen(true));
        });


        exitBtn.setOnAction(e -> {
            cleanup();
            if (customizeSceneRef != null) {
                stageRef.setScene(customizeSceneRef);
                stageRef.setFullScreen(true);
            }
        });

        return overlay;
    }

    // ---- Game Over overlay (blur background only) ----
    private void showGameOver() {
        gameLayer.setEffect(new GaussianBlur(18)); // blur only background

        Label title = new Label("GAME OVER!");
        title.setStyle("-fx-text-fill: red; -fx-font-size: 56px; -fx-font-weight: bold;");

        Button rematchBtn = new Button("Rematch");
        rematchBtn.setStyle("-fx-background-color:#2980b9; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand");
        rematchBtn.setPrefSize(150, 40);

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand");
        exitBtn.setPrefSize(150, 40);

        VBox box = new VBox(24, title, rematchBtn, exitBtn);
        box.setAlignment(Pos.CENTER);

        Rectangle dim = new Rectangle(screenWidth, screenHeight, Color.color(0,0,0,0.55));
        StackPane overlayRoot = new StackPane(dim, box);
        overlayRoot.setPrefSize(screenWidth, screenHeight);
        StackPane.setAlignment(box, Pos.CENTER);

        root.getChildren().add(overlayRoot); // on top, not blurred
        cleanup(); // stop timer/music

        rematchBtn.setOnAction(e -> {
            cleanup(); // stop current timer/music
            Single_Player fresh = new Single_Player(carImagePath);
            Scene newScene = fresh.createScene(stageRef, customizeSceneRef);
            stageRef.setScene(newScene);

            // Re-enter fullscreen *after* the scene is attached
            stageRef.setFullScreenExitHint("");
            stageRef.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            Platform.runLater(() -> stageRef.setFullScreen(true));
        });


        exitBtn.setOnAction(e -> {
            if (customizeSceneRef != null) {
                stageRef.setScene(customizeSceneRef);
                stageRef.setFullScreen(true);
            }
        });
    }

    // ---- Win overlay (blur background only) ----
    private void showWin() {
        gameLayer.setEffect(new GaussianBlur(18)); // blur only background

        Label title = new Label("YOU WON!");
        title.setStyle("-fx-text-fill: #00e676; -fx-font-size: 56px; -fx-font-weight: bold;");

        Button rematchBtn = new Button("Rematch");
        rematchBtn.setStyle("-fx-background-color:#2980b9; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand");
        rematchBtn.setPrefSize(150, 40);

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand");
        exitBtn.setPrefSize(150, 40);

        VBox box = new VBox(24, title, rematchBtn, exitBtn);
        box.setAlignment(Pos.CENTER);

        Rectangle dim = new Rectangle(screenWidth, screenHeight, Color.color(0,0,0,0.55));
        StackPane overlayRoot = new StackPane(dim, box);
        overlayRoot.setPrefSize(screenWidth, screenHeight);
        StackPane.setAlignment(box, Pos.CENTER);

        root.getChildren().add(overlayRoot); // on top, not blurred
        cleanup(); // stop timer/music

        rematchBtn.setOnAction(e -> {
            cleanup(); // stop current timer/music
            Single_Player fresh = new Single_Player(carImagePath);
            Scene newScene = fresh.createScene(stageRef, customizeSceneRef);
            stageRef.setScene(newScene);

            // Re-enter fullscreen *after* the scene is attached
            stageRef.setFullScreenExitHint("");
            stageRef.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            Platform.runLater(() -> stageRef.setFullScreen(true));
        });


        exitBtn.setOnAction(e -> {
            if (customizeSceneRef != null) {
                stageRef.setScene(customizeSceneRef);
                stageRef.setFullScreen(true);
            }
        });
    }

    // ---- per-frame update ----
    private void update() {
        // speed
        if (velocity < MAX_VELOCITY) velocity += ACCELERATION;
        if (velocity > MAX_VELOCITY) velocity = MAX_VELOCITY;

        // scroll map
        map.updateMap1(velocity);
        distance += velocity;
        if (distance > maxDistance) maxDistance = distance;

        distanceLabel.setText(String.format("Distance: %.0f  |  Max: %.0f", distance, maxDistance));

        // progressive spawns
        int desired = Math.min(INITIAL_OBSTACLES_PER_ROAD + (int)(distance / OBSTACLE_INCREMENT_DISTANCE), MAX_OBSTACLES_PER_ROAD);
        while (obstacles.size() < desired) {
            int randomLane = (int)(Math.random() * 5);
            boolean tooClose = false;
            for (Objects_Single ex : obstacles) {
                if (ex.getLane() == randomLane && Math.abs(ex.getObstacle().getY() + velocity - (-100)) < 150) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                Objects_Single obj = new Objects_Single(randomLane);
                obj.setObjectY(-100);
                obstacles.add(obj);
                gameLayer.getChildren().add(obj.getObstacle());
            }
        }

        // move & recycle
        for (Objects_Single obj : obstacles) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity);
            if (iv.getY() > screenHeight) {
                double obstacleHeight = iv.getBoundsInParent().getHeight();
                double gap = 50;
                int lane = obj.getLane();
                boolean suitable = false;
                int attempts = 0;
                double candidateY;

                while (!suitable && attempts < 10) {
                    candidateY = -Math.random() * 2000;
                    suitable = true;
                    for (Objects_Single other : obstacles) {
                        if (other != obj && other.getLane() == lane) {
                            double otherY = other.getObstacle().getY();
                            if (Math.abs(candidateY - otherY) < obstacleHeight + gap) {
                                suitable = false;
                                break;
                            }
                        }
                    }
                    if (suitable) iv.setY(candidateY);
                    attempts++;
                }
                if (!suitable) {
                    double minY = Double.POSITIVE_INFINITY;
                    boolean hasOther = false;
                    for (Objects_Single other : obstacles) {
                        if (other != obj && other.getLane() == lane) {
                            double otherY = other.getObstacle().getY();
                            if (otherY < minY) minY = otherY;
                            hasOther = true;
                        }
                    }
                    if (hasOther) iv.setY(minY - obstacleHeight - gap);
                    else iv.setY(-Math.random() * 2000);
                }
            }
        }

        // collisions -> decrement lives and maybe show GAME OVER
        for (Objects_Single obj : obstacles) {
            ImageView iv = obj.getObstacle();
            if (car.getBoundsInParent().intersects(iv.getBoundsInParent())) {
                iv.setY(-Math.random() * 2000);
                velocity *= 0.8;

                // decrease lives and update counter
                if (livesRemaining > 0) {
                    livesRemaining--;
                    heartCountLabel.setText("x" + livesRemaining);
                }

                if (livesRemaining <= 0) {
                    gameOver = true;
                    if (timer != null) timer.stop();
                    saveMaxDistance();
                    showGameOver();
                }
                break;
            }
        }


        // optional finish line visual
        if (distance >= FINISH_DISTANCE - screenHeight) {
            finishLine.setVisible(true);
            double finishY = car.getLayoutY() - (FINISH_DISTANCE - distance);
            finishLine.setY(finishY);
        }
    }

    // ---- utils ----
    private void cleanup() {
        if (timer != null) timer.stop();
        if (player != null) {
            try { player.stop(); } catch (Exception ignored) {}
        }
    }

    private void saveMaxDistance() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"))) {
            writer.write(String.valueOf(maxDistance));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double loadMaxDistance() {
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            return Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            return 0;
        }
    }

    // lane movement (uses local destX to satisfy lambda capture rules)
    private void moveCarBetweenLanes(ImageView car, Set<KeyCode> pressed, AtomicBoolean isMoving) {
        if (isMoving.get()) return;

        double targetX = car.getLayoutX();
        double halfLane = laneWidth / 2.0;

        if (pressed.contains(KeyCode.LEFT) && currentLane > 0) {
            currentLane--;
            targetX = map.getLayoutX() + map.grassWidth + laneWidth * currentLane + halfLane - car.getFitWidth() / 2.0;
            car.setRotate(-15);
            pressed.remove(KeyCode.LEFT);
        } else if (pressed.contains(KeyCode.RIGHT) && currentLane < numberOfLanes - 1) {
            currentLane++;
            targetX = map.getLayoutX() + map.grassWidth + laneWidth * currentLane + halfLane - car.getFitWidth() / 2.0;
            car.setRotate(15);
            pressed.remove(KeyCode.RIGHT);
        } else {
            return;
        }

        isMoving.set(true);

        final double destX = targetX; // lambda-safe copy
        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.15), car);
        tt.setToX(destX - car.getLayoutX());
        tt.setOnFinished(e -> {
            car.setLayoutX(destX);
            car.setTranslateX(0);
            car.setRotate(0);
            isMoving.set(false);
        });
        tt.play();
    }
}
