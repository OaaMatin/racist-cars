
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.animation.AnimationTimer;
import javafx.animation.TranslateTransition;
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
import javafx.util.Duration;

public class Single_Player {

    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;
    private String carImagePath;
    private double targetX;

    public Single_Player(String carPath) {
        this.carImagePath = carPath;
    }

    private Pane root;
    Set<KeyCode> pressedKeys = new HashSet<>();
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();
    ArrayList<Objects> obstacles = new ArrayList<>();
    AtomicBoolean car_isMoving = new AtomicBoolean(false);
    RoadMap map;
    ImageView car;
    double velocity = 0;
    double acceleration = 0.03;
    double maxVelocity = 26;
    double distance = 0;
    final double finishDistance = 30000;
    boolean gameOver = false;
    Rectangle finishLine;
    AnimationTimer timer;
    MediaPlayer player;
    int numberOfLanes = 5;
    int currentLane = 2;
    double laneWidth;

    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        Media media = new Media(getClass().getResource("/resources/musics/Gorilaz.mp3").toExternalForm());
        player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(1.0);
        player.setOnReady(() -> {
            player.play();
        });

        stage.setOnCloseRequest(event -> {
            if (player != null) {
                player.stop();
            }
        });

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        map = new RoadMap(screenWidth, screenHeight);
        laneWidth = map.roadWidth / numberOfLanes;

        try {
            Image carImage = new Image(getClass().getResource(carImagePath).toExternalForm());
            car = new ImageView(carImage);
            car.setFitWidth(screenWidth / 10);
            car.setFitHeight(screenHeight / 4);
            car.setLayoutX(screenWidth * 0.28);
            car.setLayoutY(screenHeight * 0.7);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        finishLine = new Rectangle(map.getLayoutX() + map.grassWidth, 0, map.roadWidth, 10);
        finishLine.setFill(Color.RED);
        finishLine.setVisible(false);

        root.getChildren().addAll(map, car, finishLine);

        for (int lane = 0; lane < 10; lane++) {
            Objects obj = new Objects(lane);
            obj.setObjectY(-Math.random() * 1500);
            obstacles.add(obj);
            root.getChildren().add(obj.getObstacle());
        }

        Button pauseBtn = new Button("Pause");
        pauseBtn.setStyle("-fx-background-color:#ffcbfx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 20px; -fx-cursor: hand");
        pauseBtn.setPrefSize(100, 30);
        pauseBtn.setLayoutX(20);
        pauseBtn.setLayoutY(20);
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
            if (player != null) {
                player.pause();
            }
            pauseOverlay.setVisible(true);
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
            try {
                Single_Player_Customize customize = new Single_Player_Customize();
                customize.start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(pauseBtn, pauseOverlay);

        timer = new AnimationTimer() {
            public void handle(long now) {
                if (!gameOver) {
                    moveCarBetweenLanes(car, pressedKeys, car_isMoving);
                    update();
                    if (distance >= finishDistance) {
                        System.out.println("finish");
                        gameOver = true;
                        timer.stop();
                    }
                }
            }
        };

        timer.start();
        stage.setTitle("Single Player");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    void update() {
        if (velocity < maxVelocity) {
            velocity += acceleration;
        }
        if (velocity > maxVelocity) {
            velocity = maxVelocity;
        }
        map.updateMap1(velocity);
        distance += velocity;
        int desiredObstacles = INITIAL_OBSTACLES_PER_ROAD + (int) (distance / OBSTACLE_INCREMENT_DISTANCE);
        desiredObstacles = Math.min(desiredObstacles, MAX_OBSTACLES_PER_ROAD);
        while (obstacles.size() < desiredObstacles) {
            int randomLane = (int) (Math.random() * 5);
            boolean tooClose = false;
            for (Objects existing : obstacles) {
                if (existing.getLane() == randomLane && Math.abs(existing.getObstacle().getY() + velocity - (-100)) < 150) {
                    tooClose = true;
                    break;
                }
            }
            if (!tooClose) {
                Objects newObj = new Objects(randomLane);
                newObj.setObjectY(-100);
                obstacles.add(newObj);
                root.getChildren().add(newObj.getObstacle());
            }
        }

        for (Objects obj : obstacles) {
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
                    for (Objects obj2 : obstacles) {
                        if (obj2 != obj && obj2.getLane() == lane) {
                            double otherY = obj2.getObstacle().getY();
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
                    for (Objects obj2 : obstacles) {
                        if (obj2 != obj && obj2.getLane() == lane) {
                            double otherY = obj2.getObstacle().getY();
                            if (otherY < minY) {
                                minY = otherY;
                            }
                            hasOther = true;
                        }
                    }
                    if (hasOther) {
                        iv.setY(minY - obstacleHeight - gap);
                    } else {
                        iv.setY(-Math.random() * 2000);
                    }
                }
            }
        }

        for (Objects obj : obstacles) {
            ImageView iv = obj.getObstacle();
            if (car.getBoundsInParent().intersects(iv.getBoundsInParent())) {
                iv.setY(-Math.random() * 2000);
                velocity *= 0.8;
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
        if (timer != null) {
            timer.stop();
        }
        if (player != null) {
            player.stop();
        }
    }

    public void moveCarBetweenLanes(ImageView car, Set<KeyCode> pressedKeys, AtomicBoolean isMoving) {
        if (isMoving.get()) {
            return;
        }

        targetX = car.getLayoutX();
        double halfLane = laneWidth / 3;

        if (pressedKeys.contains(KeyCode.LEFT) && currentLane > 0) {
            currentLane--;
            targetX = map.getLayoutX() + laneWidth * currentLane + halfLane - car.getFitWidth() / 2;
            car.setRotate(-15);
            pressedKeys.remove(KeyCode.LEFT);
        } else if (pressedKeys.contains(KeyCode.RIGHT) && currentLane < numberOfLanes - 1) {
            currentLane++;
            targetX = map.getLayoutX() + laneWidth * currentLane + halfLane - car.getFitWidth() / 2;
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
