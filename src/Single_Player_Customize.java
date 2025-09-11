import javafx.application.Application;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Single_Player_Customize extends Application {
    private static final int rows = 2;
    private static final int cols = 4;

    private static final int cell_witdh = 150;
    private static final int cell_height = 300;
    private static final double V_gap = 8;
    private static final double H_gap = 8;
    private static final double box_witdh = cell_witdh * cols + (cols - 1) * H_gap;
    private static final double box_height = cell_height * rows + (rows - 1) * V_gap;

    private int selected_index = 0;
    private final List<ImageView> carImages = new ArrayList<>();
    private String selectedImagePath = "";
    private static final String[] car_image_paths = {
            "/recources/pictures/car1skin1.png",
            "/resources/pictures/car1skin2.png",
            "/resources/pictures/car1.png",
            "/resources/pictures/car2.png",
            "/resources/pictures/arya'scar.png",
            "/resources/pictures/car4.png",
            "/resources/pictures/car3.png",
            "/resources/pictures/car1skin2.png"
    };

    @Override
    public void start(Stage primaryStage) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}