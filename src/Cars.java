import javafx.animation.TranslateTransition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Cars {
    public static int Control_car1 = 2;
    public static int Control_car2 = 5;

    public static void move1(ImageView car1, Set<KeyCode> kc, AtomicBoolean car1_ismoving) {
        if (car1_ismoving.get()) return;

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), car1);

        if (kc.contains(KeyCode.A) && Control_car1 > 1) {
            transition.setByX(-screenWidth * 0.08);
            Control_car1--;
        } else if (kc.contains(KeyCode.D) && Control_car1 < 5) {
            transition.setByX(screenWidth * 0.08);
            Control_car1++;
        } else {
            return;
        }

        car1_ismoving.set(true);
        transition.setOnFinished(e -> car1_ismoving.set(false));
        transition.play();
    }

    public static void move2(ImageView car2, Set<KeyCode> kc, AtomicBoolean car2_ismoving) {
        if (car2_ismoving.get()) return;

        Rectangle2D bounds = Screen.getPrimary().getBounds();
        double screenWidth = bounds.getWidth();

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.25), car2);

        if (kc.contains(KeyCode.LEFT) && Control_car2 > 1) {
            transition.setByX(-screenWidth * 0.08);
            Control_car2--;
        } else if (kc.contains(KeyCode.RIGHT) && Control_car2 < 5) {
            transition.setByX(screenWidth * 0.08);
            Control_car2++;
        } else {
            return;
        }

        car2_ismoving.set(true);
        transition.setOnFinished(e -> car2_ismoving.set(false));
        transition.play();
    }
}
