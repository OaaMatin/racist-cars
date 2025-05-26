import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Racing_Game extends Application {
    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;
    private static final int Max_Items_Per_Cars=4;

    private Pane root;
    Set<KeyCode> pressedkeys = new HashSet<>();

    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();

    ArrayList<Objects> obstacles1 = new ArrayList<>();
    ArrayList<Objects> obstacles2 = new ArrayList<>();

    AtomicBoolean car1_ismoving = new AtomicBoolean(false);
    AtomicBoolean car2_ismoving = new AtomicBoolean(false);

    RoadMap map1;
    RoadMap map2;

    ImageView car1;
    ImageView car2;

    double velocity1 = 0;
    double velocity2 = 0;
    double acceleration = 0.03;
    double maxVelocity = 26;
    double distance1 = 0;
    double distance2 = 0;
    final double finishDistance = 30000;
    boolean gameOver = false;

    Rectangle finishLine1;
    Rectangle finishLine2;

    AnimationTimer timer;
    MediaPlayer player;

    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);

        Media media = new Media(getClass().getResource("/resources/musics/Wendsday.mp3").toExternalForm());
        player = new MediaPlayer(media);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(1.0);
        player.setOnReady(() -> {
            player.play();
        });




        scene.setOnKeyPressed(e -> pressedkeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedkeys.remove(e.getCode()));

        map1 = new RoadMap(screenWidth / 2, screenHeight);
        map2 = new RoadMap(screenWidth / 2, screenHeight);
        map2.setLayoutX(screenWidth / 2);

        try {
            Image car1Image = new Image("resources/pictures/car1skin1.png");
            Image car2Image = new Image("resources/pictures/car1skin2.png");

            car1 = new ImageView(car1Image);
            car2 = new ImageView(car2Image);

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
            return;
        }

        finishLine1 = new Rectangle(map1.getLayoutX() + map1.grassWidth, 0, map1.roadWidth, 10);
        finishLine1.setFill(Color.RED);
        finishLine1.setVisible(false);

        finishLine2 = new Rectangle(map2.getLayoutX() + map2.grassWidth, 0, map2.roadWidth, 10);
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

        timer = new AnimationTimer() {
            public void handle(long now) {
                if (!gameOver) {
                    Cars.move1(car1, pressedkeys, car1_ismoving);
                    Cars.move2(car2, pressedkeys, car2_ismoving);
                    update();
                    if (distance1 >= finishDistance) {
                        System.out.println("Car 1 wins!");
                        gameOver = true;
                        timer.stop();
                    } else if (distance2 >= finishDistance) {
                        System.out.println("Car 2 wins!");
                        gameOver = true;
                        timer.stop();
                    }
                }
            }
        };

        timer.start();

        stage.setTitle("Racing Game");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        Cars.Control_car1 = 2;
        Cars.Control_car2 = 5;
    }

    void update() {
        if (velocity1 < maxVelocity) velocity1 += acceleration;
        if (velocity2 < maxVelocity) velocity2 += acceleration;

        if (velocity1 > maxVelocity) velocity1 = maxVelocity;
        if (velocity2 > maxVelocity) velocity2 = maxVelocity;

        map1.updateMap1(velocity1);
        map2.updateMap2(velocity2);

        distance1 += velocity1;
        distance2 += velocity2;

        int desiredObstacles1 = INITIAL_OBSTACLES_PER_ROAD + (int)(distance1 / OBSTACLE_INCREMENT_DISTANCE);
        desiredObstacles1 = Math.min(desiredObstacles1, MAX_OBSTACLES_PER_ROAD);
        while (obstacles1.size() < desiredObstacles1) {
            int randomLane1 = (int)(Math.random() * 5);
            boolean tooClose = false;
            for (Objects existing : obstacles1) {
                if (existing.getLane() == randomLane1 &&
                        Math.abs(existing.getObstacle().getY() + velocity1 - (-100)) < 150) {
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

        int desiredObstacles2 = INITIAL_OBSTACLES_PER_ROAD + (int)(distance2 / OBSTACLE_INCREMENT_DISTANCE);
        desiredObstacles2 = Math.min(desiredObstacles2, MAX_OBSTACLES_PER_ROAD);
        while (obstacles2.size() < desiredObstacles2) {
            int randomLane2 = 5 + (int)(Math.random() * 5);
            boolean tooClose = false;
            for (Objects existing : obstacles2) {
                if (existing.getLane() == randomLane2 &&
                        Math.abs(existing.getObstacle().getY() + velocity2 - (-100)) < 150) {
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

        for (Objects obj : obstacles1) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity1);
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
                    for (Objects other : obstacles1) {
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
                    for (Objects other : obstacles1) {
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
                    } else {
                        iv.setY(-Math.random() * 2000);
                    }
                }
            }
        }

        for (Objects obj : obstacles2) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity2);
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
                    for (Objects other : obstacles2) {
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
                    for (Objects other : obstacles2) {
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
                    } else {
                        iv.setY(-Math.random() * 2000);
                    }
                }
            }
        }

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

        if (distance1 >= finishDistance - screenHeight) {
            finishLine1.setVisible(true);
            double finishLineY1 = car1.getLayoutY() - (finishDistance - distance1);
            finishLine1.setY(finishLineY1);
        }

        if (distance2 >= finishDistance - screenHeight) {
            finishLine2.setVisible(true);
            double finishLineY2 = car2.getLayoutY() - (finishDistance - distance2);
            finishLine2.setY(finishLineY2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}