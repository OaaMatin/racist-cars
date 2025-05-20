import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class test extends Application {

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // Create a Rectangle node (not using fillRect)
        Rectangle rect = new Rectangle(50, 50, 100, 100); // x, y, width, height
        rect.setFill(Color.BLUE);

        // Add rectangle to the pane
        root.getChildren().add(rect);

        // Create TranslateTransition
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(rect); // apply to the rectangle
        transition.setDuration(Duration.seconds(0.6)); // duration of animation
        transition.setByX(200); // move right by 200 pixels
        transition.setByY(200);
        transition.setCycleCount(TranslateTransition.INDEFINITE); // repeat forever
        transition.setAutoReverse(true); // go back and forth

        // Start the animation
        transition.play();

    }

    public static void main(String[] args) {
        Rectangle rect = new Rectangle(50, 50, 100, 100);
        System.out.println(rect.getX());
    }
}

