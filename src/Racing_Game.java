import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Racing_Game extends Application {

    Rectangle2D bounds = Screen.getPrimary().getBounds();
    double screenWidth = bounds.getWidth();
    double screenHeight = bounds.getHeight();
    double car1X = screenWidth / 8;
    double car1Y = 0.8 * screenHeight;
    double car2X = (92.5 / 100) * screenWidth;
    double car2Y = 0.8 * screenHeight;
    double cars_width = screenWidth / 20;
    double cars_height = screenHeight / 15;
    AtomicBoolean car1_ismoving = new AtomicBoolean(false);
    AtomicBoolean car2_ismoving = new AtomicBoolean(false);

    public void start(Stage primaryStage) {
        Set<KeyCode> pressedKeys = new HashSet<>();
        Pane root = new Pane();
        Scene scene = new Scene(root);

        Rectangle[] lines = new Rectangle[9];
        for (int i = 1; i <= 9; i++) {
            Rectangle line = new Rectangle(
                    (screenWidth * 0.10 * i) - (screenWidth / 200 - screenWidth / 1000),
                    0,
                    screenWidth / 100 - screenWidth / 500,
                    screenHeight
            );
            line.setFill(Color.BLACK);
            lines[i - 1] = line;
        }

        Rectangle car1 = new Rectangle(car1X, car1Y, cars_width, cars_height);
        car1.setFill(Color.MAGENTA);
        Rectangle car2 = new Rectangle(car2X, car2Y, cars_width, cars_height);
        car2.setFill(Color.HOTPINK);

        root.getChildren().addAll(lines);
        root.getChildren().addAll(car1, car2);

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));
        car1.setX(car1X);
        car2.setX(car2X);

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
               Cars.move1(car1,pressedKeys,car1_ismoving);
               Cars.move2(car2,pressedKeys,car2_ismoving);



                Cars.display(car1, car2);
            }
        };
        gameLoop.start();
        primaryStage.setTitle("Racing Game");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
