import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
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

        Button SinglePlayerBtn = new Button("Single Player");
        SinglePlayerBtn.getStyleClass().add("neon-button-yellow");
        SinglePlayerBtn.setPrefSize(200, 60);

        Button MultiPlayerBtn = new Button("Multi Player");
        MultiPlayerBtn.getStyleClass().add("neon-button-cyan");
        MultiPlayerBtn.setPrefSize(200, 60);

        Button exitBtn = new Button("Exit");
        exitBtn.getStyleClass().add("neon-button-pink");
        exitBtn.setPrefSize(200, 60);

        Slider volumeSlider = new Slider(0, 1, 0.5);
        volumeSlider.setOrientation(Orientation.VERTICAL);

        Label speakerLabel = new Label("\uD83D\uDD0A");
        speakerLabel.setStyle("-fx-font-size: 28px; -fx-text-fill: white;");

        VBox volumeControl = new VBox(5, volumeSlider, speakerLabel);
        volumeControl.setAlignment(Pos.CENTER);

        Pane pane = new Pane();
        pane.setBackground(new Background(backgroundImage));
        pane.getChildren().addAll(SinglePlayerBtn, MultiPlayerBtn, exitBtn, imageView, volumeControl);

        SinglePlayerBtn.setOnAction(e -> {
            mediaPlayer.stop();
            new Single_Player_Customize().start(primaryStage);
        });
        MultiPlayerBtn.setOnAction(e -> {
            mediaPlayer.stop();
            new Customize().start(primaryStage);
        });
        exitBtn.setOnAction(e -> ((Stage) exitBtn.getScene().getWindow()).close());

        Scene scene = new Scene(pane);
        scene.getStylesheets().add("resources/CSS/neon-buttons.css");

        // Center buttons horizontally
        SinglePlayerBtn.layoutXProperty().bind(scene.widthProperty().subtract(SinglePlayerBtn.widthProperty()).divide(2));
        MultiPlayerBtn.layoutXProperty().bind(scene.widthProperty().subtract(MultiPlayerBtn.widthProperty()).divide(2));
        exitBtn.layoutXProperty().bind(scene.widthProperty().subtract(exitBtn.widthProperty()).divide(2));

        // Vertical positions as a fraction of the scene height
        SinglePlayerBtn.layoutYProperty().bind(scene.heightProperty().multiply(0.725));
        MultiPlayerBtn.layoutYProperty().bind(scene.heightProperty().multiply(0.815));
        exitBtn.layoutYProperty().bind(scene.heightProperty().multiply(0.90));

        // Volume control near left edge, proportional Y
        volumeControl.layoutXProperty().set(15);
        volumeControl.layoutYProperty().bind(scene.heightProperty().multiply(0.78));

        // Center the logo
        scene.widthProperty().addListener((obs, ov, nv) -> centerImage(imageView, scene));
        scene.heightProperty().addListener((obs, ov, nv) -> centerImage(imageView, scene));

        // Bind volume
        mediaPlayer.volumeProperty().bind(volumeSlider.valueProperty());
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double v = newVal.doubleValue();
            speakerLabel.setText(v == 0 ? "\uD83D\uDD07" : (v <= 0.3 ? "\uD83D\uDD08" : (v <= 0.7 ? "\uD83D\uDD09" : "\uD83D\uDD0A")));
        });

        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
//        primaryStage.initStyle(StageStyle.UNDECORATED);
//        primaryStage.setFullScreenExitHint("");
        primaryStage.setFullScreen(true);
        primaryStage.show();
        Platform.runLater(() -> {
            var screen = Screen.getScreensForRectangle(primaryStage.getX(), primaryStage.getY(), primaryStage.getWidth(), primaryStage.getHeight()).stream().findFirst().orElse(Screen.getPrimary());
            var vb = screen.getVisualBounds();
            primaryStage.setX(vb.getMinX());
            primaryStage.setY(vb.getMinY());
            primaryStage.setWidth(vb.getWidth());
            primaryStage.setHeight(vb.getHeight());
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