import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
<<<<<<< HEAD
import javafx.stage.Screen;
import javafx.stage.Stage;
=======
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Racing_Game extends Application {
<<<<<<< HEAD
    Set<KeyCode> pressedkeys = new HashSet<>();
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();

=======
    private static final int INITIAL_OBSTACLES_PER_ROAD = 5;
    private static final double OBSTACLE_INCREMENT_DISTANCE = 2000;
    private static final int MAX_OBSTACLES_PER_ROAD = 10;

    private Pane root;
    Set<KeyCode> pressedkeys = new HashSet<>();

    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();

    ArrayList<Objects> obstacles1 = new ArrayList<>();
    ArrayList<Objects> obstacles2 = new ArrayList<>();

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
    AtomicBoolean car1_ismoving = new AtomicBoolean(false);
    AtomicBoolean car2_ismoving = new AtomicBoolean(false);

    RoadMap map1;
    RoadMap map2;

    ImageView car1;
    ImageView car2;

    double velocity1 = 0;
    double velocity2 = 0;
    double acceleration = 0.05;
<<<<<<< HEAD
    double maxVelocity = 20;

    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);
=======
    double maxVelocity = 13;
    double distance1 = 0;
    double distance2 = 0;
    final double finishDistance = 10000;
    boolean gameOver = false;

    Rectangle finishLine1;
    Rectangle finishLine2;

    AnimationTimer timer;

    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
        scene.setOnKeyPressed(e -> pressedkeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedkeys.remove(e.getCode()));

        map1 = new RoadMap(screenWidth / 2, screenHeight);
        map2 = new RoadMap(screenWidth / 2, screenHeight);
        map2.setLayoutX(screenWidth / 2);

        try {
<<<<<<< HEAD
            Image car1Image = new Image("car1.png");
            Image car2Image = new Image("car2.png");
=======
            Image car1Image = new Image("resources/pictures/car1skin1.png");
            Image car2Image = new Image("resources/pictures/car1skin2.png");
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66

            car1 = new ImageView(car1Image);
            car2 = new ImageView(car2Image);

            car1.setFitWidth(screenWidth / 20);
<<<<<<< HEAD
            car1.setFitHeight(screenHeight / 10);
            car2.setFitWidth(screenWidth / 20);
            car2.setFitHeight(screenHeight / 10  );
=======
            car1.setFitHeight(screenHeight / 8);
            car2.setFitWidth(screenWidth / 20);
            car2.setFitHeight(screenHeight / 8);
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66

            car1.setLayoutX(screenWidth * 0.14);
            car1.setLayoutY(screenHeight * 0.8);

            car2.setLayoutX(screenWidth * 0.88);
            car2.setLayoutY(screenHeight * 0.8);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

<<<<<<< HEAD
        root.getChildren().addAll(map1, map2, car1, car2);

        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                Cars.move1(car1, pressedkeys, car1_ismoving);
                Cars.move2(car2, pressedkeys, car2_ismoving);
                update();
=======
        finishLine1 = new Rectangle(map1.getLayoutX() + map1.grassWidth, 0, map1.roadWidth, 10);
        finishLine1.setFill(Color.RED);
        finishLine1.setVisible(false);

        finishLine2 = new Rectangle(map2.getLayoutX() + map2.grassWidth, 0, map2.roadWidth, 10);
        finishLine2.setFill(Color.RED);
        finishLine2.setVisible(false);

        root.getChildren().addAll(map1, map2, car1, car2, finishLine1, finishLine2);

        for (int i = 0; i < INITIAL_OBSTACLES_PER_ROAD; i++) {
            int randomLane1 = (int) (Math.random() * 5);
            int randomLane2 = 5 + (int) (Math.random() * 5);

            Objects obj1 = new Objects(randomLane1);
            Objects obj2 = new Objects(randomLane2);

            obj1.setObjectY(-Math.random() * 2000);
            obj2.setObjectY(-Math.random() * 2000);

            obstacles1.add(obj1);
            obstacles2.add(obj2);

            root.getChildren().addAll(obj1.getObstacle(), obj2.getObstacle());
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
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
            }
        };

        timer.start();

        stage.setTitle("Racing Game");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
<<<<<<< HEAD
        Cars.Control_car1=2;
        Cars.Control_car2=5;
=======
        Cars.Control_car1 = 2;
        Cars.Control_car2 = 5;
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
    }

    void update() {
        if (velocity1 < maxVelocity) velocity1 += acceleration;
        if (velocity2 < maxVelocity) velocity2 += acceleration;
<<<<<<< HEAD
=======

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
        if (velocity1 > maxVelocity) velocity1 = maxVelocity;
        if (velocity2 > maxVelocity) velocity2 = maxVelocity;

        map1.updateMap1(velocity1);
        map2.updateMap2(velocity2);
<<<<<<< HEAD
=======

        distance1 += velocity1;
        distance2 += velocity2;


        int desiredObstacles1 = INITIAL_OBSTACLES_PER_ROAD + (int)(distance1 / OBSTACLE_INCREMENT_DISTANCE);
        desiredObstacles1 = Math.min(desiredObstacles1, MAX_OBSTACLES_PER_ROAD);
        while (obstacles1.size() < desiredObstacles1) {
            int randomLane1 = (int)(Math.random() * 5);
            Objects newObj1 = new Objects(randomLane1);
            newObj1.setObjectY(-Math.random() * 2000);
            obstacles1.add(newObj1);
            root.getChildren().add(newObj1.getObstacle());
        }

        int desiredObstacles2 = INITIAL_OBSTACLES_PER_ROAD + (int)(distance2 / OBSTACLE_INCREMENT_DISTANCE);
        desiredObstacles2 = Math.min(desiredObstacles2, MAX_OBSTACLES_PER_ROAD);
        while (obstacles2.size() < desiredObstacles2) {
            int randomLane2 = 5 + (int)(Math.random() * 5);
            Objects newObj2 = new Objects(randomLane2);
            newObj2.setObjectY(-Math.random() * 2000);
            obstacles2.add(newObj2);
            root.getChildren().add(newObj2.getObstacle());
        }

        for (Objects obj : obstacles1) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity1);
            if (iv.getY() > screenHeight) {
                iv.setY(-Math.random() * 2000);
            }
        }

        for (Objects obj : obstacles2) {
            ImageView iv = obj.getObstacle();
            iv.setY(iv.getY() + velocity2);
            if (iv.getY() > screenHeight) {
                iv.setY(-Math.random() * 2000);
            }
        }

        // Collision detection for car1
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
                iv.setY(-Math.random() * 2000); // Obstacle disappears
                velocity2 *= 0.8; // Reduce velocity
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
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
    }

    public static void main(String[] args) {
        launch(args);
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
