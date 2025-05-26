import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Racing_Game extends Application {
    Set<KeyCode> pressedkeys = new HashSet<>();
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();

    AtomicBoolean car1_ismoving = new AtomicBoolean(false);
    AtomicBoolean car2_ismoving = new AtomicBoolean(false);

    RoadMap map1;
    RoadMap map2;

    ImageView car1;
    ImageView car2;

    double velocity1 = 0;
    double velocity2 = 0;
    double acceleration = 0.05;
    double maxVelocity = 20;

    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, screenWidth, screenHeight);
        scene.setOnKeyPressed(e -> pressedkeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressedkeys.remove(e.getCode()));

        map1 = new RoadMap(screenWidth / 2, screenHeight);
        map2 = new RoadMap(screenWidth / 2, screenHeight);
        map2.setLayoutX(screenWidth / 2);

        try {
            Image car1Image = new Image("car1.png");
            Image car2Image = new Image("car2.png");

            car1 = new ImageView(car1Image);
            car2 = new ImageView(car2Image);

            car1.setFitWidth(screenWidth / 20);
            car1.setFitHeight(screenHeight / 10);
            car2.setFitWidth(screenWidth / 20);
            car2.setFitHeight(screenHeight / 10  );

            car1.setLayoutX(screenWidth * 0.14);
            car1.setLayoutY(screenHeight * 0.8);

            car2.setLayoutX(screenWidth * 0.88);
            car2.setLayoutY(screenHeight * 0.8);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        root.getChildren().addAll(map1, map2, car1, car2);

        AnimationTimer timer = new AnimationTimer() {
            public void handle(long now) {
                Cars.move1(car1, pressedkeys, car1_ismoving);
                Cars.move2(car2, pressedkeys, car2_ismoving);
                update();
            }
        };

        timer.start();

        stage.setTitle("Racing Game");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
        Cars.Control_car1=2;
        Cars.Control_car2=5;
    }

    void update() {
        if (velocity1 < maxVelocity) velocity1 += acceleration;
        if (velocity2 < maxVelocity) velocity2 += acceleration;
        if (velocity1 > maxVelocity) velocity1 = maxVelocity;
        if (velocity2 > maxVelocity) velocity2 = maxVelocity;

        map1.updateMap1(velocity1);
        map2.updateMap2(velocity2);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
