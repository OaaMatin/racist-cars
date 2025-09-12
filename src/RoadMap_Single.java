import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class RoadMap_Single extends Pane {
    public double width;
    public double height;

    public double grassWidth;
    public double roadWidth;
    public double laneWidth;

    private final int lanes = 5; // single player: 5 lanes
    private final List<Rectangle> laneLines = new ArrayList<>();

    public RoadMap_Single(double width, double height) {
        this.width = width;
        this.height = height;

        // centered road: 80% road, 10% grass on each side
        grassWidth = width * 0.1;
        roadWidth  = width * 0.8;
        laneWidth  = roadWidth / lanes;

        // background: grass | road | grass
        Rectangle leftGrass  = new Rectangle(0, 0, grassWidth, height);
        leftGrass.setFill(Color.web("#ab9cb8")); // shoulder/grass color

        Rectangle road = new Rectangle(grassWidth, 0, roadWidth, height);
        road.setFill(Color.web("#1F1B24"));

        Rectangle rightGrass = new Rectangle(grassWidth + roadWidth, 0, grassWidth, height);
        rightGrass.setFill(Color.web("#ab9cb8"));

        getChildren().addAll(leftGrass, road, rightGrass);

        // dashed lane separators (lanes-1 = 4 lines for 5 lanes)
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00FFFF"));
        glow.setRadius(10);

        for (int i = 1; i < lanes; i++) {
            double x = grassWidth + i * laneWidth;
            // make a column of dashes down the screen
            for (int j = 0; j < 15; j++) {
                Rectangle dash = new Rectangle(x - 2, j * 110, 4, 30);
                dash.setFill(Color.web("#FF00FF"));
                dash.setEffect(glow);
                laneLines.add(dash);
                getChildren().add(dash);
            }
        }
    }

    // single-player scrolling update
    public void updateMap1(double velocity) {
        for (Rectangle dash : laneLines) {
            dash.setY(dash.getY() + velocity);
            if (dash.getY() > height) {
                dash.setY(dash.getY() - height - 40);
            }
        }
    }

    // keep method for compatibility; same behavior as updateMap1
    public void updateMap2(double velocity) {
        updateMap1(velocity);
    }
}
