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

public class Customize extends Application {
    private Stage stage;
    private int selectedIndexCar1 = 0;
    private int selectedIndexCar2 = 0;
    private Rectangle selectCar1;
    private Rectangle selectCar2;
    private Button readycar1;
    private Button readycar2;
    private boolean iscar1ready = false;
    private boolean iscar2ready = false;
    private List<ImageView> car1Images = new ArrayList<>();
    private List<ImageView> car2Images = new ArrayList<>();
    private int selectedButtonIndex = 0;
    private VBox startCancelBox;
    private String selectedImageCar1Path = "";
    private String selectedImageCar2Path = "";

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        //تیتر
        Label mainLabel = new Label("Multi Player Customize");
        mainLabel.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 32));
        mainLabel.setTextFill(Color.WHITE);
        mainLabel.setAlignment(Pos.CENTER);

        //بخش سمت چپ مربوط به بازیکن شماره ۱
        Label car1Label = new Label("Car1");
        car1Label.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        car1Label.setTextFill(Color.WHITE);

        GridPane car1Grid = new GridPane();
        car1Grid.setPrefSize(600, 600);
        car1Grid.setHgap(2);
        car1Grid.setVgap(20);

        double cellWidth = 600.0 / 4;
        double cellHeight = 600.0 / 2;

        //ماشین های بازیکن شماره ۱
        String[] imageUrlsCar1 = {
                "resources/pictures/car1skin1.png",
                "resources/pictures/car1skin2.png",
                "resources/pictures/skin11.png",
                "resources/pictures/arya'scar.png",
                "resources/pictures/car1.png",
                "resources/pictures/car2.png",
                "resources/pictures/car3.png",
                "resources/pictures/car4.png",
        };

        //قرار دادن تصاویر ماشین ها در باکس
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 4; col++) {
                StackPane cell = new StackPane();

                Rectangle rect = new Rectangle(cellWidth, cellHeight);
                rect.setFill(Color.color(1, 1, 1, 0.3));
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(cellWidth - 20);
                imageView.setFitHeight(cellHeight - 20);
                imageView.setPreserveRatio(true);

                int index = row * 4 + col;
                if (index < imageUrlsCar1.length && !imageUrlsCar1[index].isEmpty()) {
                    imageView.setImage(new Image(imageUrlsCar1[index], true));
                    car1Images.add(imageView);
                }

                cell.getChildren().addAll(rect, imageView);
                car1Grid.add(cell, col, row);
            }
        }

        //حلقه انتخاب شدن ماشین بازیکن ۱
        selectCar1 = new Rectangle(cellWidth, cellHeight);
        selectCar1.setFill(Color.TRANSPARENT);
        selectCar1.setStroke(Color.PINK);
        selectCar1.setStrokeWidth(3);
        car1Grid.getChildren().add(selectCar1);

        VBox car1Box = new VBox(10, car1Label, car1Grid);
        car1Box.setAlignment(Pos.TOP_CENTER);
        car1Box.setPadding(new Insets(0, 0, 0, 10));

        //باکس سمت راست بازیکن شماره ۲
        Label car2Label = new Label("Car2");
        car2Label.setFont(Font.font("Courier New", FontWeight.BOLD, 24));
        car2Label.setTextFill(Color.WHITE);

        GridPane car2Grid = new GridPane();
        car2Grid.setPrefSize(600, 600);
        car2Grid.setHgap(2);
        car2Grid.setVgap(20);

        double cell2Width = 600.0 / 2;
        double cell2Height = 600.0 / 2;

        //ادرس تصویر ماشین های بازیکن شماره ۲
        String[] imageUrlsCar2 = {
                "resources/pictures/car1skin1.png",
                "resources/pictures/car1.png",
                "resources/pictures/car1skin2.png",
                "resources/pictures/car2.png"
        };

        //جاگذاری تصاویر در باکس
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                StackPane cell = new StackPane();

                Rectangle rect = new Rectangle(cell2Width, cell2Height);
                rect.setFill(Color.color(1, 1, 1, 0.3));
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(1);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(cell2Width - 20);
                imageView.setFitHeight(cell2Height - 20);
                imageView.setPreserveRatio(true);

                int index = row * 2 + col;
                if (index < imageUrlsCar2.length && !imageUrlsCar2[index].isEmpty()) {
                    imageView.setImage(new Image(imageUrlsCar2[index], true));
                    car2Images.add(imageView);
                }

                cell.getChildren().addAll(rect, imageView);
                car2Grid.add(cell, col, row);
            }
        }

        //حلقه انتخاب شدن ماشین بازیکن شماره ۲
        selectCar2 = new Rectangle(cell2Width, cell2Height);
        selectCar2.setFill(Color.TRANSPARENT);
        selectCar2.setStroke(Color.PINK);
        selectCar2.setStrokeWidth(3);
        car2Grid.getChildren().add(selectCar2);

        VBox car2Box = new VBox(10, car2Label, car2Grid);
        car2Box.setAlignment(Pos.TOP_CENTER);
        car2Box.setPadding(new Insets(0, 10, 0, 0));

        //باکس سمت راست و چپ رو کنار هم میزاره
        HBox rectanglesBox = new HBox(20, car1Box, car2Box);
        rectanglesBox.setAlignment(Pos.CENTER);

        selectedIndexCar1 = 0;
        selectedImageCar1Path = imageUrlsCar1[0];
        updateSelectionPosition(car1Grid, selectCar1, 0, 4, cellWidth, cellHeight);
        updateCar1ImageZoom(0, -1);

        selectedIndexCar2 = 0;
        selectedImageCar2Path = imageUrlsCar2[0];
        updateSelectionPosition(car2Grid, selectCar2, 0, 2, cell2Width, cell2Height);
        updateCar2ImageZoom(0, -1);

        //بکگراند صفحه
        VBox content = new VBox(20);
        Image bgImage = new Image(getClass().getResource("resources/pictures/background3.jpeg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(
                bgImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true) // متناسب با اندازه VBox
        );
        content.setBackground(new Background(backgroundImage));

        content.setPadding(new Insets(0, 50, 30, 50));
        content.setAlignment(Pos.TOP_CENTER);
        content.getChildren().addAll(mainLabel, rectanglesBox);
        StackPane root = new StackPane(content);

        //دکمه های ready زیر هر باکس
        readycar1 = new Button("Ready Car1 (Ctrl)");
        readycar1.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10;");
        car1Box.getChildren().add(readycar1);
        readycar2 = new Button("Ready Car2 (Shift)");
        readycar2.setStyle("-fx-background-color:#8e44ad; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 10;");
        car2Box.getChildren().add(readycar2);

        //تغییر وضعیت به آماده
        readycar1.setOnAction(e -> {
            iscar1ready = !iscar1ready;
            readycar1.setText(iscar1ready ? "Car1 Ready ✔️" : "Ready Car1 (Ctrl)");
        });
        readycar2.setOnAction(e -> {
            iscar2ready = !iscar2ready;
            readycar2.setText(iscar2ready ? "Car2 Ready ✔️" : "Ready Car2 (Shift)");
        });

        //برگشت به صفحه منو
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color:#c0392b; -fx-text-fill:white; -fx-background-radius: 13; -fx-font-size: 20px; -fx-cursor: hand");
        backButton.setPrefSize(150, 40);

        StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);
        StackPane.setMargin(backButton, new Insets(0, 0, 10, 0));
        root.getChildren().add(backButton);

        backButton.setOnAction(e -> {
            Main mainMenu = new Main();
            try {
                mainMenu.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(root, screenWidth, screenHeight);

        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            //حرکت های بازیکن شماره ۱ WASD
            if (code == KeyCode.A) {
                int prev = selectedIndexCar1;
                if (selectedIndexCar1 > 0) {
                    selectedIndexCar1--;
                    selectedImageCar1Path = imageUrlsCar1[selectedIndexCar1];
                    updateSelectionPosition(car1Grid, selectCar1,
                            selectedIndexCar1, 4, cellWidth, cellHeight);
                    updateCar1ImageZoom(selectedIndexCar1, prev);
                }
            } else if (code == KeyCode.D) {
                int prev = selectedIndexCar1;
                if (selectedIndexCar1 < imageUrlsCar1.length - 1) {
                    selectedIndexCar1++;
                    selectedImageCar1Path = imageUrlsCar1[selectedIndexCar1];
                    updateSelectionPosition(car1Grid, selectCar1,
                            selectedIndexCar1, 4, cellWidth, cellHeight);
                    updateCar1ImageZoom(selectedIndexCar1, prev);
                }
            } else if (code == KeyCode.W) {
                int prev = selectedIndexCar1;
                if (selectedIndexCar1 - 4 >= 0) {
                    selectedIndexCar1 -= 4;
                    selectedImageCar1Path = imageUrlsCar1[selectedIndexCar1];
                    updateSelectionPosition(car1Grid, selectCar1,
                            selectedIndexCar1, 4, cellWidth, cellHeight);
                    updateCar1ImageZoom(selectedIndexCar1, prev);
                }
            } else if (code == KeyCode.S) {
                int prev = selectedIndexCar1;
                if (selectedIndexCar1 + 4 < imageUrlsCar1.length) {
                    selectedIndexCar1 += 4;
                    selectedImageCar1Path = imageUrlsCar1[selectedIndexCar1];
                    updateSelectionPosition(car1Grid, selectCar1,
                            selectedIndexCar1, 4, cellWidth, cellHeight);
                    updateCar1ImageZoom(selectedIndexCar1, prev);
                }
            //حرکت های بازیکن شماره ۲ با دکمه های جهتی
            } else if (code == KeyCode.LEFT) {
                int prev = selectedIndexCar2;
                if (selectedIndexCar2 > 0) {
                    selectedIndexCar2--;
                    selectedImageCar2Path = imageUrlsCar2[selectedIndexCar2];
                    updateSelectionPosition(car2Grid, selectCar2,
                            selectedIndexCar2, 2, cell2Width, cell2Height);
                    updateCar2ImageZoom(selectedIndexCar2, prev);
                }
            } else if (code == KeyCode.RIGHT) {
                int prev = selectedIndexCar2;
                if (selectedIndexCar2 < imageUrlsCar2.length - 1) {
                    selectedIndexCar2++;
                    selectedImageCar2Path = imageUrlsCar2[selectedIndexCar2];
                    updateSelectionPosition(car2Grid, selectCar2,
                            selectedIndexCar2, 2, cell2Width, cell2Height);
                    updateCar2ImageZoom(selectedIndexCar2, prev);
                }
            } else if (code == KeyCode.UP) {
                int prev = selectedIndexCar2;
                if (selectedIndexCar2 - 2 >= 0) {
                    selectedIndexCar2 -= 2;
                    selectedImageCar2Path = imageUrlsCar2[selectedIndexCar2];
                    updateSelectionPosition(car2Grid, selectCar2,
                            selectedIndexCar2, 2, cell2Width, cell2Height);
                    updateCar2ImageZoom(selectedIndexCar2, prev);
                }
            } else if (code == KeyCode.DOWN) {
                int prev = selectedIndexCar2;
                if (selectedIndexCar2 + 2 < imageUrlsCar2.length) {
                    selectedIndexCar2 += 2;
                    selectedImageCar2Path = imageUrlsCar2[selectedIndexCar2];
                    updateSelectionPosition(car2Grid, selectCar2,
                            selectedIndexCar2, 2, cell2Width, cell2Height);
                    updateCar2ImageZoom(selectedIndexCar2, prev);
                }

            //آماده شدن با کیبورد
            } else if (code == KeyCode.CONTROL) {
                iscar1ready = !iscar1ready;
                readycar1.setText(iscar1ready ? "Car1 Ready ✔️" : "Ready Car1 (Ctrl)");
                checkIfBothReady(root);
            } else if (code == KeyCode.SHIFT) {
                iscar2ready = !iscar2ready;
                readycar2.setText(iscar2ready ? "Car2 Ready ✔️" : "Ready Car2 (Shift)");
                checkIfBothReady(root);
            } else if (startCancelBox != null) {
                if (code == KeyCode.UP || code == KeyCode.DOWN)
                    selectedButtonIndex = (selectedButtonIndex == 0) ? 1 : 0;
                else if (code == KeyCode.ENTER)
                    ((Button) startCancelBox.getChildren().get(selectedButtonIndex)).fire();
            }
        });
        primaryStage.setTitle("Multi Player Customize");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
        root.requestFocus();
    }

    private void updateCar1ImageZoom(int newindex, int oldindex) {
        if (oldindex >= 0 && oldindex < car1Images.size()) {
            car1Images.get(oldindex).setScaleX(1);
            car1Images.get(oldindex).setScaleY(1);
        }
        if (newindex >= 0 && newindex < car1Images.size()) {
            car1Images.get(newindex).setScaleX(1.1);
            car1Images.get(newindex).setScaleY(1.1);
        }
    }

    private void updateCar2ImageZoom(int newindex, int oldindex) {
        if (oldindex >= 0 && oldindex < car1Images.size()) {
            car2Images.get(oldindex).setScaleX(0.8);
            car2Images.get(oldindex).setScaleY(0.8);
        }
        if (newindex >= 0 && newindex < car1Images.size()) {
            car2Images.get(newindex).setScaleX(0.9);
            car2Images.get(newindex).setScaleY(0.9);
        }
    }

    //جابجایی حلقه انتخاب ماشین
    private void updateSelectionPosition(GridPane grid, Rectangle rect, int index, int columns, double cellWidth, double cellHeight) {
        int row = index / columns;
        int col = index % columns;
        rect.setTranslateX(col * (cellWidth + grid.getHgap() + 2));
        rect.setTranslateY(row * (cellHeight + grid.getVgap() + 2));
    }

    //وقتی بازیکنا آماده شدن صفحه رو تار میکنه و دکمه استارت و کنسل رو نشون میده
    private void checkIfBothReady(Pane root) {
        if (iscar1ready && iscar2ready && startCancelBox == null) {
            for (Node node : root.getChildren())
                node.setOpacity(0.5);
            Button startBtn = new Button("Start Game");
            startBtn.setPrefSize(150,40);
            Button cancelBtn = new Button("Cancel");
            cancelBtn.setPrefSize(150,40);

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
            selectedButtonIndex = 0;

            //عملیات استارت زدن
            startBtn.setOnAction(e -> {
                Racing_Game racing = new Racing_Game(selectedImageCar1Path, selectedImageCar2Path);

                Scene customizeScene = stage.getScene();
                Scene racingScene = racing.createScene(stage, customizeScene);

                stage.setScene(racingScene);
                stage.setFullScreen(true);
            });

            //عملیات کنسل کردن
            cancelBtn.setOnAction(e -> {
                root.getChildren().remove(startCancelBox);
                startCancelBox = null;
                for (Node node : root.getChildren())
                    node.setOpacity(1);
                iscar1ready = false;
                iscar2ready = false;
                readycar1.setText("Ready Car1 (Ctrl)");
                readycar2.setText("Ready Car2 (Shift)");
                root.requestFocus();
            });
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}