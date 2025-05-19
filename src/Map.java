import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Screen;



public class Map {
    public static void display (GraphicsContext gc){
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();
        double screenHeight = bounds.getHeight();
    }
}
