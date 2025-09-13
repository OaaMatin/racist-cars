import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Objects {
    private int Lane; //شماره لاینی که مانع توش قرار میگیره
    public ImageView obstacle; //تصویر اون مانع

    public Objects(int Lane) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();
        this.Lane = Lane;

        try {
            //انتخاب رندوم تصویر مانع
            int n = (int) (Math.random() * 3);
            Image obstacleImage;

            if (n == 1) {
                obstacleImage = new Image("resources/pictures/obsticale.png");
            } else if (n == 2) {
                obstacleImage = new Image("resources/pictures/obsticale2.png");
            } else {
                obstacleImage = new Image("resources/pictures/obsticale3.png");
            }

            //ساخته شدن مانع و تنظیم اندازه مانع
            obstacle = new ImageView(obstacleImage);
            obstacle.setFitWidth(screenWidth / 20);
            obstacle.setFitHeight(screenWidth / 20);

            //تعیین لاین برای موقعیت مانع
            if (Lane < 5) {
                obstacle.setX(screenWidth * (0.06 + 0.08 * Lane));
            } else {
                obstacle.setX(screenWidth * (0.56 + 0.08 * (Lane - 5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //تغییر موقعیت عمودی مانع
    public void setObjectY(double objectY) {
        obstacle.setY(objectY);
    }

    public ImageView getObstacle() {
        return obstacle;
    }

    public int getLane() {
        return this.Lane;
    }
}