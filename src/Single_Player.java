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
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class Single_Player {
    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;

    private double targetX;
    private double maxDistance = 0;

    double velocity = 0;
    double acceleration = 0.025;
    double maxVelocity = 20;
    double distance = 0;

    final double screenHeight = Screen.getPrimary().getBounds().getHeight();
    final double screenWidth  = Screen.getPrimary().getBounds().getWidth();

    double laneWidth;

    private Label distanceLabel;
    private final String carImagePath;

    final double finishDistance = 50000;
    final int maxCrashes = 6;
    final ArrayList<ImageView> hearts = new ArrayList<>();

    public Single_Player(String carPath) {
        this.carImagePath = carPath;
    }

    private Pane root;
    final Set<KeyCode> pressedKeys = new HashSet<>();
    final ArrayList<Objects_Single> obstacles = new ArrayList<>();

    final AtomicBoolean car_isMoving = new AtomicBoolean(false);

    RoadMap_Single map;
    ImageView car;
    Rectangle finishLine;
    AnimationTimer timer;
    MediaPlayer player;

    boolean gameOver = false;

    int numberOfLanes = 5;
    int currentLane   = 2;
    int crashCount    = 0;

    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        // keyboard focus safety
        root.setFocusTraversable(true);
        root.requestFocus();

        // music
        try {
            Media media = new Media(getClass().getResource("/resources/musics/Gorilaz.mp3").toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(1.0);
            player.setOnReady(player::play);
        } catch (Exception ignored) {
            player = null;
        }

        stage.setOnCloseRequest(event -> {
            if (player != null) player.stop();
        });

        // key input
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED,
                e -> pressedKeys.add(e.getCode()));
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED,
                e -> pressedKeys.remove(e.getCode()));
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

            //car centered in lane
            double halfLane = laneWidth / 2.0;
            double laneCenterX = map.getLayoutX() + map.grassWidth + currentLane * laneWidth + halfLane;
            car.setLayoutX(laneCenterX - car.getFitWidth() / 2.0);
            car.setLayoutY(screenHeight * 0.7);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // finish line
        finishLine = new Rectangle(map.getLayoutX() + map.grassWidth, 0, map.roadWidth, 10);
        finishLine.setFill(Color.RED);
        finishLine.setVisible(false);

        // volume control
        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setOrientation(Orientation.VERTICAL);
        Label speakerLabel = new Label("\uD83D\uDD0A");
        speakerLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");

        VBox volumeControl = new VBox(5, volumeSlider, speakerLabel);
        volumeControl.setAlignment(Pos.CENTER);
        volumeControl.layoutXProperty().set(15);
        volumeControl.layoutYProperty().bind(scene.heightProperty().multiply(0.78));

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

        root.getChildren().addAll(map, car, finishLine, volumeControl);

        distanceLabel = new Label();
        distanceLabel.setTextFill(Color.WHITE);
        distanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        distanceLabel.setLayoutX(screenWidth - 360);
        distanceLabel.setLayoutY(10);
        maxDistance = loadMaxDistance();
        distanceLabel.setText(String.format("Distance: %.0f  |  Max: %.0f", distance, maxDistance));
        root.getChildren().add(distanceLabel);

        // hearts
        for (int i = 0; i < maxCrashes; i++) {
            Image heartImage = new Image(getClass().getResource("/resources/pictures/heart.png").toExternalForm());
            ImageView heart = new ImageView(heartImage);
            heart.setFitWidth(40);
            heart.setFitHeight(40);
            heart.setLayoutX(10 + i * 45);
            heart.setLayoutY(10);
            hearts.add(heart);
            root.getChildren().add(heart);
        }

        for (int lane = 0; lane < 5; lane++) {
            Objects_Single obj = new Objects_Single(lane);
            obj.setObjectY(-Math.random() * 1500);
            obstacles.add(obj);
            root.getChildren().add(obj.getObstacle());
        }

        Button pauseBtn = new Button("Pause");
        pauseBtn.setStyle("-fx-background-color:#ff3399; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 20px; -fx-cursor: hand");
        pauseBtn.setPrefSize(120, 40);
        pauseBtn.setLayoutX((screenWidth - pauseBtn.getPrefWidth()) / 2);
        pauseBtn.setLayoutY(screenHeight - pauseBtn.getPrefHeight() - 20);
        pauseBtn.setFocusTraversable(false);

        Pane pauseOverlay = new Pane();
        pauseOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        pauseOverlay.setPrefSize(screenWidth, screenHeight);
        pauseOverlay.setVisible(false);

        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px;");
        continueBtn.setPrefSize(150, 50);
        continueBtn.setLayoutX((screenWidth - continueBtn.getPrefWidth()) / 2);
        continueBtn.setLayoutY(screenHeight / 2 - 60);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px;");
        cancelBtn.setPrefSize(150, 50);
        cancelBtn.setLayoutX((screenWidth - cancelBtn.getPrefWidth()) / 2);
        cancelBtn.setLayoutY(screenHeight / 2 + 20);

        pauseOverlay.getChildren().addAll(continueBtn, cancelBtn);

        pauseBtn.setOnAction(e -> {
            timer.stop();
            if (player != null) player.pause();
            pauseOverlay.setVisible(true);
        });

        continueBtn.setOnAction(e -> {
            timer.start();
            if (player != null) player.play();
            pauseOverlay.setVisible(false);
        });

        cancelBtn.setOnAction(e -> {
            cleanup();
            try {
                Single_Player_Customize customize = new Single_Player_Customize();
                customize.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(pauseBtn, pauseOverlay);

        // game loop
        timer = new AnimationTimer() {
            public void handle(long now) {
                if (!gameOver) {
                    moveCarBetweenLanes(car, pressedKeys, car_isMoving);
                    update();
                    if (distance >= finishDistance) {
                        gameOver = true;
                        stop();
                        if (distance > maxDistance) {
                            maxDistance = distance;
                            saveMaxDistance();
                        }
                    }
                }
            }
        };

        timer.start();

        // fullscreen window
        stage.setTitle("Single Player");
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreen(true);
        stage.show();
    }

    void update() {
        // speed
        if (velocity < maxVelocity) velocity += acceleration;
        if (velocity > maxVelocity) velocity = maxVelocity;

        // scroll
        map.updateMap1(velocity);
        distance += velocity;
        if (distance > maxDistance) maxDistance = distance;

        distanceLabel.setText(String.format("Distance: %.0f  |  Max: %.0f", distance, maxDistance));

        // spawn more objects
        int desiredObstacles = Math.min(
                INITIAL_OBSTACLES_PER_ROAD + (int)(distance / OBSTACLE_INCREMENT_DISTANCE),
                MAX_OBSTACLES_PER_ROAD
        );
        while (obstacles.size() < desiredObstacles) {
            int randomLane = (int)(Math.random() * 5);
            boolean tooClose = false;
            for (Objects_Single existing : obstacles) {
                if (existing.getLane() == randomLane &&
                        Math.abs(existing.getObstacle().getY() + velocity - (-100)) < 150) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                Objects_Single newObj = new Objects_Single(randomLane);
                newObj.setObjectY(-100);
                obstacles.add(newObj);
                root.getChildren().add(newObj.getObstacle());
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
                    for (Objects_Single obj2 : obstacles) {
                        if (obj2 != obj && obj2.getLane() == lane) {
                            double otherY = obj2.getObstacle().getY();
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
                    for (Objects_Single obj2 : obstacles) {
                        if (obj2 != obj && obj2.getLane() == lane) {
                            double otherY = obj2.getObstacle().getY();
                            if (otherY < minY) minY = otherY;
                            hasOther = true;
                        }
                    }
                    if (hasOther) iv.setY(minY - obstacleHeight - gap);
                    else iv.setY(-Math.random() * 2000);
                }
            }
        }

        // accidents
        for (Objects_Single obj : obstacles) {
            ImageView iv = obj.getObstacle();
            if (car.getBoundsInParent().intersects(iv.getBoundsInParent())) {
                iv.setY(-Math.random() * 2000);
                velocity *= 0.8;

                crashCount++;
                if (crashCount <= hearts.size()) hearts.get(crashCount - 1).setVisible(false);

                if (crashCount >= maxCrashes) {
                    gameOver = true;
                    timer.stop();
                    saveMaxDistance();
                    showGameOverMessage();
                }
                break;
            }
        }

        if (distance >= finishDistance - screenHeight) {
            finishLine.setVisible(true);
            double finishLaneY = car.getLayoutY() - (finishDistance - distance);
            finishLine.setY(finishLaneY);
        }
    }

    private void cleanup() {
        if (timer != null) timer.stop();
        if (player != null) player.stop();
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

    private void showGameOverMessage() {
        Label gameOverLabel = new Label("GAME OVER!");
        gameOverLabel.setTextFill(Color.RED);
        gameOverLabel.setStyle("-fx-font-size: 64px; -fx-font-weight: bold;");
        gameOverLabel.setLayoutX(screenWidth / 2 - 150);
        gameOverLabel.setLayoutY(screenHeight / 2 - 100);
        root.getChildren().add(gameOverLabel);
    }

    public void moveCarBetweenLanes(ImageView car, Set<KeyCode> pressedKeys, AtomicBoolean isMoving) {
        if (isMoving.get()) return;

        targetX = car.getLayoutX();
        double halfLane = laneWidth / 2.0;

        if (pressedKeys.contains(KeyCode.LEFT) && currentLane > 0) {
            currentLane--;
            targetX = map.getLayoutX() + map.grassWidth + laneWidth * currentLane + halfLane - car.getFitWidth() / 2.0;
            car.setRotate(-15);
            pressedKeys.remove(KeyCode.LEFT);
        } else if (pressedKeys.contains(KeyCode.RIGHT) && currentLane < numberOfLanes - 1) {
            currentLane++;
            targetX = map.getLayoutX() + map.grassWidth + laneWidth * currentLane + halfLane - car.getFitWidth() / 2.0;
            car.setRotate(15);
            pressedKeys.remove(KeyCode.RIGHT);
        } else {
            return;
        }

        isMoving.set(true);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.15), car);
        transition.setToX(targetX - car.getLayoutX());
        transition.setOnFinished(e -> {
            car.setLayoutX(targetX);
            car.setTranslateX(0);
            car.setRotate(0);
            isMoving.set(false);
        });
        transition.play();
    }
}
