import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.util.Duration;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cars {
    public static int Control_car1 = 2;
    public static int Control_car2 = 5;

    public static void display(Rectangle car1, Rectangle car2) {

        car1.setFill(Color.MAGENTA);
        car2.setFill(Color.HOTPINK);
    }

    public static void move1(Rectangle car1, Set<KeyCode> kc, AtomicBoolean car1_ismoving) {
        if (car1_ismoving.get()) return;



        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();


        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), car1);

        if (kc.contains(KeyCode.A)&&Control_car1>1) {
            transition.setByX(-screenWidth / 10);
            car1_ismoving.set(true);
            Control_car1--;

        } else if (kc.contains(KeyCode.D)&&Control_car1<5) {
            transition.setByX(screenWidth / 10);
            car1_ismoving.set(true);
            Control_car1++;

        } else {
            return;
        }

        transition.setOnFinished(e -> car1_ismoving.set(false));
        transition.play();
    }

    public static void move2(Rectangle car2, Set<KeyCode> kc, AtomicBoolean car2_ismoving) {
        if (car2_ismoving.get()) return;
        double x = car2.getX();


        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), car2);

        if (kc.contains(KeyCode.LEFT)&&Control_car2>1) {
            transition.setByX(-screenWidth / 10);
            car2_ismoving.set(true);
            Control_car2--;
        } else if (kc.contains(KeyCode.RIGHT)&&Control_car2<5) {
            transition.setByX(screenWidth / 10);
            car2_ismoving.set(true);
            Control_car2++;
        } else {
            return;
        }

        transition.setOnFinished(e -> car2_ismoving.set(false));
        transition.play();
    }

}