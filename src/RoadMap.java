<<<<<<< HEAD
=======
import javafx.scene.effect.DropShadow;
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class RoadMap extends Pane {
    double width;
    double height;
<<<<<<< HEAD
=======

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
    double grassWidth;
    double roadWidth;
    double laneWidth;

    List<Rectangle> laneLines = new ArrayList<>();

    public RoadMap(double width, double height) {
        this.width = width;
        this.height = height;
<<<<<<< HEAD
=======

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
        grassWidth = width * 0.1;
        roadWidth = width * 0.8;
        laneWidth = roadWidth / 5;

        Rectangle leftGrass = new Rectangle(0, 0, grassWidth, height);
<<<<<<< HEAD
        leftGrass.setFill(Color.GREEN);

        Rectangle road = new Rectangle(grassWidth, 0, roadWidth, height);
        road.setFill(Color.GRAY);

        Rectangle rightGrass = new Rectangle(grassWidth + roadWidth, 0, grassWidth, height);
        rightGrass.setFill(Color.GREEN);

        getChildren().addAll(leftGrass, road, rightGrass);

=======
        leftGrass.setFill(Color.CYAN);

        Rectangle road = new Rectangle(grassWidth, 0, roadWidth, height);
        road.setFill(Color.web("#1F1B24"));

        Rectangle rightGrass = new Rectangle(grassWidth + roadWidth, 0, grassWidth, height);
        rightGrass.setFill(Color.CYAN);

        getChildren().addAll(leftGrass, road, rightGrass);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00FFFF"));
        glow.setRadius(10);

>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
        for (int i = 1; i < 5; i++) {
            double x = grassWidth + i * laneWidth;
            for (int j = 0; j < 10; j++) {
                Rectangle dash = new Rectangle(x - 2, j * 100, 4, 40);
<<<<<<< HEAD
                dash.setFill(Color.WHITE);
=======
                dash.setFill(Color.web("#FF00FF"));
                dash.setEffect(glow);
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
                laneLines.add(dash);
                getChildren().add(dash);
            }
        }
    }

    public void updateMap1(double velocity) {
        for (Rectangle dash : laneLines) {
            dash.setY(dash.getY() + velocity);
            if (dash.getY() > height) {
                dash.setY(dash.getY() - height - 40);
            }
        }
    }

    public void updateMap2(double velocity) {
        for (Rectangle dash : laneLines) {
            dash.setY(dash.getY() + velocity);
            if (dash.getY() > height) {
                dash.setY(dash.getY() - height - 40);
            }
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> 6c224e63c9a249fc0f107f1c97d2e2e5181e3c66
