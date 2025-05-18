import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class Racing_Game extends Application {

    // Car positions
    double car1Y = 450;
    double car2Y = 450;

    // Set of keys currently pressed
    Set<KeyCode> pressedKeys = new HashSet<>();

    @Override
    public void start(Stage primaryStage) {
        Canvas canvas = new Canvas(800, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Group root = new Group(canvas);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Racing Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Handle key press
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        // Game loop using AnimationTimer
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Check key input and update positions
                if (pressedKeys.contains(KeyCode.W)) {
                    car1Y -= 50;
                }
                if (pressedKeys.contains(KeyCode.S)) {
                    car1Y += 50;
                }
                if (pressedKeys.contains(KeyCode.UP)) {
                    car2Y -= 50;
                }
                if (pressedKeys.contains(KeyCode.DOWN)) {
                    car2Y += 50;
                }

                // Clear canvas
                gc.setFill(Color.LIGHTGRAY);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // Draw cars
                gc.setFill(Color.CYAN);
                gc.fillRect(200, car1Y, 50, 100);
                gc.setFill(Color.RED);
                gc.fillRect(600, car2Y, 50, 100);
            }
        };

        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
