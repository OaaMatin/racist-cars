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

    private final int lanes = 5;
    private final List<Rectangle> laneLines = new ArrayList<>();

    public RoadMap_Single(double width, double height) {
        this.width = width;
        this.height = height;

        //رسم جاده و حاشیه اون
        grassWidth = width * 0.1;
        roadWidth  = width * 0.8;
        laneWidth  = roadWidth / lanes;

        Rectangle leftGrass  = new Rectangle(0, 0, grassWidth, height);
        leftGrass.setFill(Color.web("#ab9cb8"));

        Rectangle road = new Rectangle(grassWidth, 0, roadWidth, height);
        road.setFill(Color.web("#1F1B24"));

        Rectangle rightGrass = new Rectangle(grassWidth + roadWidth, 0, grassWidth, height);
        rightGrass.setFill(Color.web("#ab9cb8"));

        getChildren().addAll(leftGrass, road, rightGrass);

        //رسم خط چین ها با یه افکت
        DropShadow glow = new DropShadow();
        glow.setColor(Color.web("#00FFFF"));
        glow.setRadius(10);

        for (int i = 1; i < lanes; i++) {
            double x = grassWidth + i * laneWidth;
            for (int j = 0; j < 15; j++) {
                Rectangle dash = new Rectangle(x - 2, j * 110, 4, 30);
                dash.setFill(Color.web("#FF00FF"));
                dash.setEffect(glow);
                laneLines.add(dash);
                getChildren().add(dash);
            }
        }
    }

    // برای حرکت دادن خط چین هاست
    public void updateMap1(double velocity) {
        for (Rectangle dash : laneLines) {
            dash.setY(dash.getY() + velocity);
            if (dash.getY() > height) {
                dash.setY(dash.getY() - height - 40);
            }
        }
    }
}
