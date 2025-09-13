import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Objects_Single {
    private final int lane;
    private final ImageView obstacle;

    public Objects_Single(int lane) {
        this.lane = lane;

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();

        double grassWidth = screenWidth * 0.10;
        double roadWidth  = screenWidth * 0.80;
        double laneWidth  = roadWidth / 5.0;

        //انتخاب تصویر مانع به صورت تصادفی
        Image obstacleImage;
        int n = (int) (Math.random() * 3);
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
            obstacleImage = new Image(
                    getClass().getResource("/resources/pictures/obsticale.png").toExternalForm()
            );
        }

        obstacle = new ImageView(obstacleImage);

        double size = screenWidth / 20.0;
        obstacle.setFitWidth(size);
        obstacle.setFitHeight(size);

        //قرار دادن مانع در مرکز لاین
        double laneCenterX = grassWidth + lane * laneWidth + laneWidth / 2.0;
        obstacle.setX(laneCenterX - size / 2.0);
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
