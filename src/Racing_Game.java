
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Racing_Game {

    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;
    private static final double ACCELERATION = 0.03;
    private static final double MAX_VELOCITY = 26;
    private static final double FINISH_DISTANCE = 30000;

    private final String car1ImagePath;
    private final String car2ImagePath;

    public Racing_Game(String car1Path, String car2Path) {
        this.car1ImagePath = car1Path;
        this.car2ImagePath = car2Path;
    }

    private Pane root;

    private final Set<KeyCode> pressedKeys = new HashSet<>();

    private final double screenWidth = Screen.getPrimary().getBounds().getWidth();
    private final double screenHeight = Screen.getPrimary().getBounds().getHeight();

    private final ArrayList<Objects> obstacles1 = new ArrayList<>();
    private final ArrayList<Objects> obstacles2 = new ArrayList<>();

    private final AtomicBoolean car1IsMoving = new AtomicBoolean(false);
    private final AtomicBoolean car2IsMoving = new AtomicBoolean(false);

    private RoadMap map1;
    private RoadMap map2;
    private ImageView car1;
    private ImageView car2;

    private Stage stageRef;
    private Scene customizeSceneRef;

    private double velocity1 = 0;
    private double velocity2 = 0;
    private double distance1 = 0;
    private double distance2 = 0;

    private boolean gameOver = false;
    private Rectangle finishLine1;
    private Rectangle finishLine2;

    private AnimationTimer timer;
    private MediaPlayer player;

    public Scene createScene(Stage stage, Scene customizeScene) {
        this.stageRef = stage;
        this.customizeSceneRef = customizeScene;

        root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        root.setFocusTraversable(true);
        root.requestFocus();

        try {
            Media media = new Media(getClass()
                    .getResource("/resources/musics/Gorilaz.mp3")
                    .toExternalForm());
            player = new MediaPlayer(media);
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.7);
            player.setOnReady(player::play);
        } catch (Exception ignored) {
            // If the music is missing, continue silently.
        }

        stage.setOnCloseRequest(e -> cleanup());

        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED,
                e -> pressedKeys.add(e.getCode()));
        scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED,
                e -> pressedKeys.remove(e.getCode()));

        scene.setOnMouseClicked(e -> root.requestFocus());

        map1 = new RoadMap(screenWidth / 2, screenHeight);
        map2 = new RoadMap(screenWidth / 2, screenHeight);
        map2.setLayoutX(screenWidth / 2);

        try {
            car1 = new ImageView(new Image(car1ImagePath));
            car2 = new ImageView(new Image(car2ImagePath));

            car1.setFitWidth(screenWidth / 20);
            car1.setFitHeight(screenHeight / 8);
            car2.setFitWidth(screenWidth / 20);
            car2.setFitHeight(screenHeight / 8);

            car1.setLayoutX(screenWidth * 0.14);
            car1.setLayoutY(screenHeight * 0.8);

            car2.setLayoutX(screenWidth * 0.88);
            car2.setLayoutY(screenHeight * 0.8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        finishLine1 = new Rectangle(map1.getLayoutX() + map1.grassWidth, 0, map1.roadWidth, 8);
        finishLine1.setFill(Color.RED);
        finishLine1.setVisible(false);

        finishLine2 = new Rectangle(map2.getLayoutX() + map2.grassWidth, 0, map2.roadWidth, 8);
        finishLine2.setFill(Color.RED);
        finishLine2.setVisible(false);

        root.getChildren().addAll(map1, map2, car1, car2, finishLine1, finishLine2);

        for (int lane = 0; lane < 5; lane++) {
            Objects obj1 = new Objects(lane);
            obj1.setObjectY(-Math.random() * 1500);
            obstacles1.add(obj1);
            root.getChildren().add(obj1.getObstacle());
        }
        for (int lane = 5; lane < 10; lane++) {
            Objects obj2 = new Objects(lane);
            obj2.setObjectY(-Math.random() * 1500);
            obstacles2.add(obj2);
            root.getChildren().add(obj2.getObstacle());
        }

        Button pauseBtn = new Button("Pause");
        pauseBtn.setStyle("-fx-background-color:#e91e63; -fx-text-fill: white; -fx-background-radius: 13; -fx-font-size: 22px; -fx-cursor: hand");
        pauseBtn.setPrefSize(150, 40);
        pauseBtn.setLayoutX((screenWidth - pauseBtn.getPrefWidth()) / 2);
        pauseBtn.setLayoutY(screenHeight - pauseBtn.getPrefHeight() - 20);

        pauseBtn.setFocusTraversable(false);

        Pane pauseOverlay = new Pane();
        pauseOverlay.setStyle("-fx-background-color: rgba(0,0,0,0.6);");
        pauseOverlay.setPrefSize(screenWidth, screenHeight);
        pauseOverlay.setVisible(false);

        Button continueBtn = new Button("Continue");
        continueBtn.setStyle("-fx-background-color:#27ae60; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px; -fx-cursor: hand;");
        continueBtn.setPrefSize(150, 40);
        continueBtn.setLayoutX((screenWidth - continueBtn.getPrefWidth()) / 2);
        continueBtn.setLayoutY(screenHeight / 2 - 90);

        Button rematchBtn = new Button("Rematch");
        rematchBtn.setStyle("-fx-background-color:#2980b9; -fx-text-fill:white; -fx-background-radius:13; -fx-font-size:22px; -fx-cursor: hand;");
        rematchBtn.setPrefSize(150, 40);
        rematchBtn.setLayoutX((screenWidth - rematchBtn.getPrefWidth()) / 2);
        rematchBtn.setLayoutY(screenHeight / 2 - 30);

        Button cancelBtn = new Button("Exit");
        cancelBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 22px; -fx-cursor: hand;");
        cancelBtn.setPrefSize(150, 40);
        cancelBtn.setLayoutX((screenWidth - cancelBtn.getPrefWidth()) / 2);
        cancelBtn.setLayoutY(screenHeight / 2 + 30);

        pauseOverlay.getChildren().addAll(continueBtn,rematchBtn, cancelBtn);

        pauseBtn.setOnAction(e -> {
            timer.stop();
            if (player != null) {
                player.pause();
            }
            pauseOverlay.setVisible(true);
        });

        rematchBtn.setOnAction(e -> {
            Racing_Game fresh = new Racing_Game(car1ImagePath, car2ImagePath);
            Scene newScene = fresh.createScene(stageRef, customizeSceneRef);
            stageRef.setScene(newScene);
            stageRef.setFullScreen(true);
        });

        continueBtn.setOnAction(e -> {
            timer.start();
            if (player != null) {
                player.play();
            }
            pauseOverlay.setVisible(false);
        });

        cancelBtn.setOnAction(e -> {
            cleanup();
            stageRef.setScene(customizeSceneRef);
            stageRef.setFullScreen(true);
        });

        root.getChildren().addAll(pauseBtn, pauseOverlay);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameOver) {
                    return;
                }

                Cars.move1(car1, pressedKeys, car1IsMoving);
                Cars.move2(car2, pressedKeys, car2IsMoving);
                update();
                if (distance1 >= FINISH_DISTANCE) {
                    gameOver = true;
                    stop();
                    showWin("Car 1");
                } else if (distance2 >= FINISH_DISTANCE) {
                    gameOver = true;
                    stop();
                    showWin("Car 2");
                }
            }
        };
        timer.start();

        Cars.Control_car1 = 2;
        Cars.Control_car2 = 5;

        return scene;
    }

    private void update() {
        // Velocity
        if (velocity1 < MAX_VELOCITY) {
            velocity1 += ACCELERATION;
        }
        if (velocity2 < MAX_VELOCITY) {
            velocity2 += ACCELERATION;
        }
        if (velocity1 > MAX_VELOCITY) {
            velocity1 = MAX_VELOCITY;
        }
        if (velocity2 > MAX_VELOCITY) {
            velocity2 = MAX_VELOCITY;
        }

        // Scroll maps
        map1.updateMap1(velocity1);
        map2.updateMap2(velocity2);

        // Distance traveled
        distance1 += velocity1;
        distance2 += velocity2;

        //objects showing car 1
        int desiredObstacles1 = Math.min(
                INITIAL_OBSTACLES_PER_ROAD + (int) (distance1 / OBSTACLE_INCREMENT_DISTANCE),
                MAX_OBSTACLES_PER_ROAD
        );
        while (obstacles1.size() < desiredObstacles1) {
            int randomLane1 = (int) (Math.random() * 5);
            boolean tooClose = false;
            for (Objects existing : obstacles1) {
                if (existing.getLane() == randomLane1
                        && Math.abs(existing.getObstacle().getY() + velocity1 - (-100)) < 150) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                Objects newObj1 = new Objects(randomLane1);
                newObj1.setObjectY(-100);
                obstacles1.add(newObj1);
                root.getChildren().add(newObj1.getObstacle());
            }
        }

        // objects showing car 2
        int desiredObstacles2 = Math.min(
                INITIAL_OBSTACLES_PER_ROAD + (int) (distance2 / OBSTACLE_INCREMENT_DISTANCE),
                MAX_OBSTACLES_PER_ROAD
        );
        while (obstacles2.size() < desiredObstacles2) {
            int randomLane2 = 5 + (int) (Math.random() * 5);
            boolean tooClose = false;
            for (Objects existing : obstacles2) {
                if (existing.getLane() == randomLane2
                        && Math.abs(existing.getObstacle().getY() + velocity2 - (-100)) < 150) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                Objects newObj2 = new Objects(randomLane2);
                newObj2.setObjectY(-100);
                obstacles2.add(newObj2);
                root.getChildren().add(newObj2.getObstacle());
            }
        }

        // moving & showing again objects car 1
        for (Objects obj : obstacles1) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity1);
            if (iv.getY() > screenHeight) {
                recycleObstacle(obstacles1, obj);
            }
        }

        // moving & showing again objects car 2
        for (Objects obj : obstacles2) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity2);
            if (iv.getY() > screenHeight) {
                recycleObstacle(obstacles2, obj);
            }
        }

        // accidents
        for (Objects obj : obstacles1) {
            ImageView iv = obj.getObstacle();
            if (car1.getBoundsInParent().intersects(iv.getBoundsInParent())) {
                iv.setY(-Math.random() * 2000);
                velocity1 *= 0.8;
                break;
            }
        }
        for (Objects obj : obstacles2) {
            ImageView iv = obj.getObstacle();
            if (car2.getBoundsInParent().intersects(iv.getBoundsInParent())) {
                iv.setY(-Math.random() * 2000);
                velocity2 *= 0.8;
                break;
            }
        }

        // Show finish lines when close
        if (distance1 >= FINISH_DISTANCE - screenHeight) {
            finishLine1.setVisible(true);
            double finishLineY1 = car1.getLayoutY() - (FINISH_DISTANCE - distance1);
            finishLine1.setY(finishLineY1);
        }
        if (distance2 >= FINISH_DISTANCE - screenHeight) {
            finishLine2.setVisible(true);
            double finishLineY2 = car2.getLayoutY() - (FINISH_DISTANCE - distance2);
            finishLine2.setY(finishLineY2);
        }
    }

    private void recycleObstacle(ArrayList<Objects> list, Objects obj) {
        ImageView iv = obj.getObstacle();
        double obstacleHeight = iv.getBoundsInParent().getHeight();
        double gap = 50;
        int lane = obj.getLane();
        boolean suitable = false;
        int attempts = 0;
        double candidateY;
        while (!suitable && attempts < 10) {
            candidateY = -Math.random() * 2000;
            suitable = true;
            for (Objects other : list) {
                if (other != obj && other.getLane() == lane) {
                    double otherY = other.getObstacle().getY();
                    if (Math.abs(candidateY - otherY) < obstacleHeight + gap) {
                        suitable = false;
                        break;
                    }
                }
            }
            if (suitable) {
                iv.setY(candidateY);
            }
            attempts++;
        }
        if (!suitable) {
            double minY = Double.POSITIVE_INFINITY;
            boolean hasOther = false;
            for (Objects other : list) {
                if (other != obj && other.getLane() == lane) {
                    double otherY = other.getObstacle().getY();
                    if (otherY < minY) {
                        minY = otherY;
                    }
                    hasOther = true;
                }
            }
            if (hasOther) {
                iv.setY(minY - obstacleHeight - gap); 
            }else {
                iv.setY(-Math.random() * 2000);
            }
        }
    }

    // Stop loop and music
    private void cleanup() {
        if (timer != null) {
            timer.stop();
        }
        if (player != null) {
            try {
                player.stop();
            } catch (Exception ignored) {
            }
        }
    }

    private void showWin(String winner) {
        // Dim the background
        Rectangle overlay = new Rectangle(screenWidth, screenHeight, Color.color(0, 0, 0, 0.65));
        overlay.setMouseTransparent(false); // block clicks to the game when finished

        // Message + buttons
        javafx.scene.control.Label title = new javafx.scene.control.Label(winner + " WINS!");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 48px; -fx-font-weight: bold;");

        Button rematchBtn = new Button("Rematch");
        rematchBtn.setStyle("-fx-background-color:#2980b9; -fx-text-fill:white; -fx-font-size:20px; -fx-font-weight:bold; -fx-background-radius:13; -fx-cursor: hand;");
        rematchBtn.setPrefSize(150, 40);

        Button backBtn = new Button("Exit");
        backBtn.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-font-size:20px; -fx-font-weight:bold; -fx-background-radius:13; -fx-cursor: hand;");
        backBtn.setPrefSize(150, 40);

        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox(20, title, rematchBtn, backBtn);
        box.setAlignment(javafx.geometry.Pos.CENTER);

        // Center the box
        javafx.scene.layout.StackPane overlayRoot = new javafx.scene.layout.StackPane(overlay, box);
        overlayRoot.setPrefSize(screenWidth, screenHeight);
        javafx.scene.layout.StackPane.setAlignment(box, javafx.geometry.Pos.CENTER);

        // Put overlay on top
        root.getChildren().add(overlayRoot);

        // Stop input & music
        cleanup();

        // Buttons
        rematchBtn.setOnAction(e -> {
            Racing_Game fresh = new Racing_Game(car1ImagePath, car2ImagePath);
            Scene newScene = fresh.createScene(stageRef, customizeSceneRef);
            stageRef.setScene(newScene);
            stageRef.setFullScreen(true);
        });

        backBtn.setOnAction(e -> {
            stageRef.setScene(customizeSceneRef);
            stageRef.setFullScreen(true);
        });

        // Don’t let buttons steal arrow keys later if you add more logic
        rematchBtn.setFocusTraversable(false);
        backBtn.setFocusTraversable(false);
    }

}
