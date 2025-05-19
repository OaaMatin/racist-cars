import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Cars {
    public static void display(GraphicsContext matinGraph,double x1, double y1,double x2,double y2 ,double cars_width,double cars_height) {
        matinGraph.setFill(Color.MAGENTA);
        matinGraph.fillRect(x1, y1, cars_width, cars_height);
        matinGraph.setFill(Color.HOTPINK);
        matinGraph.fillRect(x2, y2, cars_width, cars_height);


    }

}
