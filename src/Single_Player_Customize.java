import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Single_Player_Customize extends Application {
    private int selectedIndexCar = 0;
    private Rectangle selectCar;
    private Button readyCar;
    private boolean isCarReady = false;
    private List<ImageView> carImages = new ArrayList<>();
    private int selectedBtnIndex = 0;
    private VBox startCancelBox;
    private String selectedImageCarPath = "";

    @Override
    public void start(Stage primaryStage) {
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        Label mainLabel = new Label("Single Player Customize");
        mainLabel.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 48));
        mainLabel.setTextFill(Color.WHITE);
        mainLabel.setAlignment(Pos.CENTER);

        Label carLabel = new Label("Select Car");
        carLabel.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        carLabel.setTextFill(Color.WHITE);

        GridPane carGrid = new GridPane();
        carGrid.setPrefSize(600, 600);
        carGrid.setHgap(2);
        carGrid.setVgap(2);

        double cellWidth = 150.0;
        double cellHeight = 300.0;

        String[] imageUrlsCar = {
                "resources/pictures/car1skin1.png",
                "resources/pictures/car1skin2.png",
                "resources/pictures/car1.png",
                "resources/pictures/car2.png",
                "resources/pictures/arya'scar.png",
                "resources/pictures/car4.png",
                "resources/pictures/car3.png",
                "resources/pictures/car1skin2.png"
        };

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                StackPane cell = new StackPane();

                Rectangle rect1 = new Rectangle(cellWidth, cellHeight);
                rect1.setFill(Color.color(1.0, 1.0, 1.0, 0.3));
                rect1.setStroke(Color.BLACK);
                rect1.setStrokeWidth(1.0);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(cellWidth - 20);
                imageView.setFitHeight(cellHeight - 20);
                imageView.setPreserveRatio(true);

                int index = row * 4 + col;
                if (index < imageUrlsCar.length && !imageUrlsCar[index].isEmpty()) {
                    imageView.setImage(new Image(imageUrlsCar[index], true));
                    carImages.add(imageView);
                }

                cell.getChildren().addAll(rect1, imageView);
                carGrid.add(cell, col, row);
            }
        }
        selectCar = new Rectangle(cellWidth, cellHeight);
        selectCar.setFill(Color.TRANSPARENT);
        selectCar.setStroke(Color.PINK);
        selectCar.setStrokeWidth(3);
        carGrid.getChildren().add(selectCar);

        VBox carBox = new VBox(10, carLabel, carGrid);
        carBox.setAlignment(Pos.CENTER);
        carBox.setPadding(new Insets(10, 10, 10, 10));

        selectedIndexCar = 0;
        selectedImageCarPath = imageUrlsCar[0];
        updateSelectionPosition(carGrid, selectCar, 0, 4, cellWidth, cellHeight);
        updateCarImageZoom(0, -1);

        VBox content = new VBox(20);
        content.setPadding(new Insets(0, 50, 30, 50));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: rgb(68, 7, 53);");
        content.getChildren().add(mainLabel);
        StackPane root = new StackPane(content);

        readyCar = new Button("Ready! (SHIFT)");
        readyCar.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10px");
        carBox.getChildren().add(readyCar);

        readyCar.setOnAction(event -> {
            isCarReady = !isCarReady;
            readyCar.setText(isCarReady ? "You're Ready!" : "Ready! (SHIFT)");
        });

        Scene scene = new Scene(root, screenWidth, screenHeight);

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();
            if (code == KeyCode.LEFT) {
                int prev = selectedIndexCar;
                if (selectedIndexCar > 0) {
                    selectedIndexCar--;
                    selectedImageCarPath = imageUrlsCar[selectedIndexCar];
                    updateSelectionPosition(carGrid, selectCar, selectedIndexCar, 4, cellWidth, cellHeight);
                    updateCarImageZoom(selectedBtnIndex, prev);
                }
            } else if (code == KeyCode.RIGHT) {
                int prev = selectedIndexCar;
                if (selectedIndexCar < imageUrlsCar.length - 1) {
                    selectedIndexCar++;
                    selectedImageCarPath = imageUrlsCar[selectedIndexCar];
                    updateSelectionPosition(carGrid, selectCar, selectedIndexCar, 4, cellWidth, cellHeight);
                    updateCarImageZoom(selectedBtnIndex, prev);
                }
            } else if (code == KeyCode.UP) {
                int prev = selectedIndexCar;
                if (selectedIndexCar - 4 >= 0) {
                    selectedIndexCar -= 4;
                    selectedImageCarPath = imageUrlsCar[selectedIndexCar];
                    updateSelectionPosition(carGrid, selectCar, prev, 4, cellWidth, cellHeight);
                    updateCarImageZoom(selectedIndexCar, prev);
                }
            } else if (code == KeyCode.DOWN) {
                int prev = selectedIndexCar;
                if (selectedIndexCar + 4 < imageUrlsCar.length) {
                    selectedIndexCar += 4;
                    selectedImageCarPath = imageUrlsCar[selectedIndexCar];
                    updateSelectionPosition(carGrid, selectCar, prev, 4, cellWidth, cellHeight);
                    updateCarImageZoom(selectedIndexCar, prev);
                }
            } else if (code == KeyCode.SHIFT) {
                isCarReady = !isCarReady;
                readyCar.setText(isCarReady ? "Ready!" : "Ready! (SHIFT)");
                checkIfReady(root);
            } else if (startCancelBox != null) {
                if (code == KeyCode.UP || code == KeyCode.DOWN)
                    selectedBtnIndex = (selectedBtnIndex == 0) ? 1 : 0;
                else if (code == KeyCode.ENTER)
                    ((Button) startCancelBox.getChildren().get(selectedBtnIndex)).fire();
            }
        });

        primaryStage.setTitle("Single Player Customize");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        root.requestFocus();
    }

    private void updateCarImageZoom(int newIndex, int oldIndex) {
        if (oldIndex >= 0 && oldIndex < carImages.size()) {
            carImages.get(oldIndex).setScaleX(1);
            carImages.get(oldIndex).setScaleY(1);
        }
        if (newIndex >= 0 && newIndex < carImages.size()) {
            carImages.get(newIndex).setScaleX(1.15);
            carImages.get(newIndex).setScaleY(1.15);
        }
    }

    private void updateSelectionPosition(GridPane grid, Rectangle rect, int index, int columns, double cellWidth, double cellHeight) {
        int row = index / columns;
        int col = index % columns;
        rect.setTranslateX(col * (cellWidth + grid.getHgap() + 2));
        rect.setTranslateY(row * (cellHeight + grid.getVgap() + 2));
    }

    private void checkIfReady(Pane root) {
        if (isCarReady && startCancelBox == null) {
            for (Node node : root.getChildren())
                node.setOpacity(0.2);
            Button startBtn = new Button("Start");
            Button cancelBtn = new Button("Cancel");

            startBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 15px;");
            cancelBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 15px;");

            startBtn.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
            cancelBtn.setFont(Font.font("Courier New", FontWeight.BOLD, 24));

            startBtn.setFocusTraversable(true);
            cancelBtn.setFocusTraversable(true);

            startCancelBox = new VBox(20, startBtn, cancelBtn);
            startCancelBox.setAlignment(Pos.CENTER);
            StackPane.setAlignment(startCancelBox, Pos.CENTER);
            root.getChildren().add(startCancelBox);
            selectedBtnIndex = 0;

            startBtn.setOnAction(e -> {
                Single_Player single_player = new Single_Player(selectedImageCarPath);
                try {
                    single_player.start(new Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            cancelBtn.setOnAction(e -> {
                root.getChildren().remove(startCancelBox);
                startCancelBox = null;
                for (Node node : root.getChildren())
                    node.setOpacity(1);
                isCarReady = false;
                readyCar.setText("Ready!");
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}