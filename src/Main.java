import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Main extends Application {
    double screenWidth = Screen.getPrimary().getBounds().getWidth();
    double screenHeight = Screen.getPrimary().getBounds().getHeight();
    private MediaPlayer mediaPlayer;

    public void start(Stage primaryStage) {
        Image image = new Image("resources/pictures/background_wall.png");
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        Image image2 = new Image("resources/pictures/logo2.png");
        ImageView imageView = new ImageView(image2);
        imageView.setFitWidth(500);
        imageView.setPreserveRatio(true);

        String musicFile = "src/resources/musics/Light_of_the_Seven.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();

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

        Slider volumeSlider = new Slider(0, 1, mediaPlayer.getVolume());
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        volumeSlider.setLayoutX(10);
        volumeSlider.setLayoutY(screenHeight * 0.8);
        volumeSlider.setOrientation(Orientation.VERTICAL);
        Label speakerLabel = new Label("\uD83D\uDD0A");
        speakerLabel.setStyle("-fx-font-size: 25px; -fx-text-fill: white;");
        speakerLabel.setLayoutX(5);
        speakerLabel.setLayoutY(screenHeight * 0.958);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));
        pane.getChildren().add(startBtn);
        pane.getChildren().add(exitBtn);
        pane.getChildren().add(imageView);
        pane.getChildren().add(volumeSlider);
        pane.getChildren().add(speakerLabel);

        startBtn.setOnAction(e -> {
            mediaPlayer.stop();
            Customize customize = new Customize();
            customize.start(primaryStage);
        });

        exitBtn.setOnAction(e -> {
            Stage stage = (Stage) exitBtn.getScene().getWindow();
            stage.close();
        });

        Scene scene = new Scene(pane, screenWidth, screenHeight);
        scene.widthProperty().addListener((obs, oldVal, newVal) -> centerImage(imageView, scene));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> centerImage(imageView, scene));
        scene.getStylesheets().add("resources/CSS/neon-buttons.css");
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            double cyanWidth = startBtn.getWidth();
            double pinkWidth = exitBtn.getWidth();
            startBtn.setLayoutX((screenWidth - cyanWidth) / 2);
            exitBtn.setLayoutX((screenWidth - pinkWidth) / 2);
        });
        centerImage(imageView, scene);
    }

    private void centerImage(ImageView imageView, Scene scene) {
        double centerX = (scene.getWidth() - imageView.getFitWidth()) / 2;
        imageView.setX(centerX);
    }

    public static void main(String[] args) {
        launch(args);
    }

}