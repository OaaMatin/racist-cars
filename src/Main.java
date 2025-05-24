import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.Cursor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.effect.DropShadow;
import javafx.util.Duration;

public class Main extends Application {
    private double hue = 0;
    private double hue2 = 0;

    public void start(Stage primaryStage) {
        Image cursorImage = new Image("fuck_finger.png");
        ImageCursor customCursor = new ImageCursor(cursorImage,
                cursorImage.getWidth() / 2, cursorImage.getHeight() / 2);
        Pane root = new Pane();


        Button startBtn = new Button("Start");
        startBtn.setLayoutX(155);
        startBtn.setLayoutY(500);


        Button exitBtn = new Button("Exit");
        exitBtn.setLayoutX(555);
        exitBtn.setLayoutY(500);


        DropShadow shadow = new DropShadow();
        shadow.setOffsetX(0);
        shadow.setOffsetY(0);
        shadow.setRadius(17.5);
        shadow.setSpread(0.1);
        shadow.setColor(Color.hsb(hue, 1.0, 1.0));
        startBtn.setEffect(shadow);


        DropShadow shadow2 = new DropShadow();
        shadow2.setOffsetX(0);
        shadow2.setOffsetY(0);
        shadow2.setRadius(17.5);
        shadow2.setSpread(0.1);
        shadow2.setColor(Color.hsb(hue, 1.0, 1.0));
        exitBtn.setEffect(shadow);


        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            hue += 1;
            if (hue > 360) hue = 0;
            shadow.setColor(Color.hsb(hue, 1.0, 1.0));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        Timeline timeline2 = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            hue2 += 1;
            if (hue2 > 360) hue2 = 0;
            shadow2.setColor(Color.hsb(hue2, 1.0, 1.0));
        }));
        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();


        startBtn.setOnMouseEntered(e -> {
            startBtn.setCursor(customCursor);
        });

        startBtn.setOnMouseExited(e -> {
            startBtn.setCursor(Cursor.DEFAULT);
        });


        exitBtn.setOnMouseEntered(e -> {
            exitBtn.setCursor(customCursor);
        });
        exitBtn.setOnMouseExited(e -> {
            exitBtn.setCursor(Cursor.DEFAULT);
        });


        startBtn.setStyle(
                "-fx-background-color: white;" + "-fx-text-fill: black;" + "-fx-font-size: 20px;" + "-fx-padding: 10 30;" + "-fx-background-radius: 15;"
        );


        exitBtn.setStyle(
                "-fx-background-color: black;" + "-fx-text-fill: white;" + "-fx-font-size: 20px;" + "-fx-padding: 10 30;" + "-fx-background-radius: 15;"
        );

        startBtn.setOnAction(e -> {
            Racing_Game customize = new Racing_Game();
            Stage stage = new Stage();
            customize.start(stage);
        });


        exitBtn.setOnAction(e -> {
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();
        });

        Rectangle rectangle = new Rectangle(0, 0, 400, 600);
        rectangle.setFill(Color.BLACK);
        root.getChildren().add(rectangle);
        root.getChildren().add(startBtn);
        root.getChildren().add(exitBtn);
        Text title = new Text("RACIST CARS");
        title.setX(250);
        title.setY(150);
        title.setStyle("-fx-font-size: 60px; -fx-font-family: 'Courier New'; -fx-fill: black; -fx-font-weight: bold;");
        root.getChildren().add(title);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
