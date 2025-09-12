import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Objects_Single {
    private final int lane;          // 0..4 for single-player
    private final ImageView obstacle;

    public Objects_Single(int lane) {
        this.lane = lane;

        // Screen metrics (match RoadMap proportions)
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();

        double grassWidth = screenWidth * 0.10;      // same as RoadMap
        double roadWidth  = screenWidth * 0.80;
        double laneWidth  = roadWidth / 5.0;

        // Pick a random obstacle sprite
        Image obstacleImage;
        int n = (int) (Math.random() * 3); // 0,1,2
        try {
            if (n == 1) {
                obstacleImage = new Image(
                        getClass().getResource("/resources/pictures/obsticale.png").toExternalForm()
                );
            } else if (n == 2) {
                obstacleImage = new Image(
                        getClass().getResource("/resources/pictures/obsticale2.png").toExternalForm()
                );
            } else {
                obstacleImage = new Image(
                        getClass().getResource("/resources/pictures/obsticale3.png").toExternalForm()
                );
            }
        } catch (Exception e) {
            // Fallback if resource missing
            obstacleImage = new Image(
                    getClass().getResource("/resources/pictures/obsticale.png").toExternalForm()
            );
        }

        obstacle = new ImageView(obstacleImage);

        // Size (square), same scale you used before
        double size = screenWidth / 20.0;
        obstacle.setFitWidth(size);
        obstacle.setFitHeight(size);

        // X position: center of the given lane
        // lane center = grassWidth + lane*laneWidth + laneWidth/2
        double laneCenterX = grassWidth + lane * laneWidth + laneWidth / 2.0;

        // Place obstacle centered on lane
        obstacle.setX(laneCenterX - size / 2.0);
        // Y is set later via setObjectY(...)
    }

    public void setObjectY(double objectY) {
        obstacle.setY(objectY);
    }

    public ImageView getObstacle() {
        return obstacle;
    }

    public int getLane() {
        return lane;
    }
}
