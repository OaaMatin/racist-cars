import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

public class Racing_Game extends Application {

    Rectangle2D bounds = Screen.getPrimary().getBounds();
    double screenWidth = bounds.getWidth();
    double screenHeight = bounds.getHeight();
    double car1X = screenWidth/8;
    double car1Y = 0.8*screenHeight;
    double car2X = (92.5/100)*screenWidth;
    double car2Y = 0.8*screenHeight;
    double cars_width = screenWidth/20;
    double cars_height = screenHeight/15;
    @Override
    public void start(Stage primaryStage) {
        Set<KeyCode> pressedKeys = new HashSet<>();
        Canvas canvas = new Canvas(screenWidth, screenHeight);
        GraphicsContext matinGraph = canvas.getGraphicsContext2D();
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));


        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (pressedKeys.contains(KeyCode.A)) {
                    car1X -= screenHeight/10;
                }
                if (pressedKeys.contains(KeyCode.D)) {
                    car1X += screenHeight/10;
                }
                if (pressedKeys.contains(KeyCode.LEFT)) {
                    car2X -= screenHeight/10;
                }
                if (pressedKeys.contains(KeyCode.RIGHT)) {
                    car2X += screenHeight/10;
                }
                matinGraph.setFill(Color.WHITE);
                matinGraph.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                for(int i=1;i<=9;i++){
                    matinGraph.setFill(Color.BLACK);
                    matinGraph.fillRect((screenWidth*0.10*i)-(screenWidth/200-screenWidth/1000),0,screenWidth/100-screenWidth/500,screenHeight );
                }
                Cars.display(matinGraph, car1X, car1Y, car2X, car2Y, cars_width, cars_height);
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