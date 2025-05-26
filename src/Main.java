import javafx.application.Application;
<<<<<<< HEAD
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class Main extends Application {
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();

    public void start(Stage primaryStage) {
        Image image = new Image("images/background_wall.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        Image image2 = new Image("images/logo2.png");
        ImageView imageView = new ImageView(image2);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        Button startBtn = new Button("Start");
        startBtn.getStyleClass().add("neon-button-cyan");
        startBtn.setLayoutY(screenHeight * 0.8);
        startBtn.setPrefWidth(150);
        startBtn.setPrefHeight(60);

        Button exitBtn = new Button("Exit");
        exitBtn.getStyleClass().add("neon-button-pink");
        exitBtn.setLayoutY(screenHeight * 0.9);
        exitBtn.setPrefWidth(150);
        exitBtn.setPrefHeight(60);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));
        pane.getChildren().add(startBtn);
        pane.getChildren().add(exitBtn);
        pane.getChildren().add(imageView);

        startBtn.setOnAction(e -> {
            Customize customize = new Customize();
            Scene customizeScene = new Scene(customize.getView(), screenWidth, screenHeight);
            primaryStage.setScene(customizeScene);
        });

=======
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
        Image cursorImage = new Image("resources/pictures/fuck_finger.png");
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


>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
        exitBtn.setOnAction(e -> {
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();
        });

<<<<<<< HEAD
        Scene scene = new Scene(pane, screenWidth, screenHeight);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> centerImage(imageView, scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> centerImage(imageView, scene));
        scene.getStylesheets().add("neon-buttons.css");
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            double cyanWidth = startBtn.getWidth();
            double pinkWidth = exitBtn.getWidth();

            // Set button position to center
            startBtn.setLayoutX((screenWidth - cyanWidth) / 2);
            exitBtn.setLayoutX((screenWidth - pinkWidth) / 2);
        });

        centerImage(imageView, scene);
    }

    private void centerImage(ImageView imageView, Scene scene) {
        double centerX = (scene.getWidth() - imageView.getFitWidth()) / 2;
        imageView.setX(centerX);

=======
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
