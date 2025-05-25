import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Items {
    private int Lane;
    public ImageView Item;
 public Items(int Lane){
     this.Lane=Lane;
     Rectangle2D Bounds= Screen.getPrimary().getBounds();
     double screenWidth = Bounds.getWidth();
    }


}
