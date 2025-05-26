import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class RoadMap extends Pane {
    double width;
    double height;
    double grassWidth;
    double roadWidth;
    double laneWidth;

    List<Rectangle> laneLines = new ArrayList<>();

    public RoadMap(double width, double height) {
        this.width = width;
        this.height = height;
        grassWidth = width * 0.1;
        roadWidth = width * 0.8;
        laneWidth = roadWidth / 5;

        Rectangle leftGrass = new Rectangle(0, 0, grassWidth, height);
        leftGrass.setFill(Color.GREEN);

        Rectangle road = new Rectangle(grassWidth, 0, roadWidth, height);
        road.setFill(Color.GRAY);

        Rectangle rightGrass = new Rectangle(grassWidth + roadWidth, 0, grassWidth, height);
        rightGrass.setFill(Color.GREEN);

        getChildren().addAll(leftGrass, road, rightGrass);

        for (int i = 1; i < 5; i++) {
            double x = grassWidth + i * laneWidth;
            for (int j = 0; j < 10; j++) {
                Rectangle dash = new Rectangle(x - 2, j * 100, 4, 40);
                dash.setFill(Color.WHITE);
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
}
