
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.geometry.Insets;

public class Customize {

    private final int ROWS = 3;
    private final int COLS = 2;

    private int currentRow = 0;
    private int currentCol = 0;
    private StackPane[][] imagePanes = new StackPane[ROWS][COLS];

    public Pane getView() {
        // === Car 1 and Car 2 Text ===
        Text car1 = new Text("Dear Car 1");
        car1.setX(120);
        car1.setY(60);
        car1.setStyle("-fx-font-size: 30px; -fx-font-family: 'Courier New'; -fx-fill: magenta; -fx-font-weight: bold;");
        DropShadow normalShadow1 = new DropShadow(4, 4, 4, Color.HOTPINK);
        DropShadow hoverShadow1 = new DropShadow(2, 2, 2, Color.HOTPINK);
        normalShadow1.setSpread(0.3);
        hoverShadow1.setSpread(0.25);
        car1.setEffect(normalShadow1);
        car1.setOnMouseEntered(e -> car1.setEffect(hoverShadow1));
        car1.setOnMouseExited(e -> car1.setEffect(normalShadow1));

        Text car2 = new Text("Car 2");
        car2.setX(560);
        car2.setY(60);
        car2.setStyle("-fx-font-size: 30px; -fx-font-family: 'Courier New'; -fx-fill: black; -fx-font-weight: bold;");
        DropShadow normalShadow2 = new DropShadow(4, 4, 4, Color.GRAY);
        DropShadow hoverShadow2 = new DropShadow(2, 2, 2, Color.GRAY);
        normalShadow2.setSpread(0.3);
        hoverShadow2.setSpread(0.25);
        car2.setEffect(normalShadow2);
        car2.setOnMouseEntered(e -> car2.setEffect(hoverShadow2));
        car2.setOnMouseExited(e -> car2.setEffect(normalShadow2));

        // === Image Grid ===
        GridPane imageGrid = new GridPane();
        imageGrid.setPadding(new Insets(10));
        imageGrid.setHgap(10);
        imageGrid.setVgap(10);
        imageGrid.setLayoutX(100);
        imageGrid.setLayoutY(100);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int index = row * COLS + col + 1; // image index from 1 to 6
                Image image = new Image("Images/car" + index + ".png");
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                StackPane pane = new StackPane(imageView);
                pane.setStyle("-fx-border-color: transparent; -fx-border-width: 3;");

                imageGrid.add(pane, col, row);
                imagePanes[row][col] = pane;
            }
        }

        // === Pane and keyboard handling ===
        Pane root = new Pane(car1, car2, imageGrid);
        root.setFocusTraversable(true);
        root.setOnKeyPressed(this::handleKeyPress);

// Request focus so it can receive key input
        root.requestFocus();

        return root;

    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            case W -> currentRow = (currentRow - 1 + ROWS) % ROWS;
            case S -> currentRow = (currentRow + 1) % ROWS;
            case A -> currentCol = (currentCol - 1 + COLS) % COLS;
            case D -> currentCol = (currentCol + 1) % COLS;
        }
        updateSelection();
    }

    private void updateSelection() {
        // Clear all borders
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                imagePanes[r][c].setStyle("-fx-border-color: transparent; -fx-border-width: 3;");
            }
        }

        // Highlight selected
        StackPane selected = imagePanes[currentRow][currentCol];
        selected.setStyle("-fx-border-color: deepskyblue; -fx-border-width: 3;");
    }

    // Optional: Get selected image pane
    public StackPane getSelectedPane() {
        return imagePanes[currentRow][currentCol];
    }
}

