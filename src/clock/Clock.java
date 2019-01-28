package clock;

import utilities.EffectUtilities;
import birthday.Birthday;
import com.sun.glass.ui.Screen;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Demonstrates drawing a clock in JavaFX
 */
public class Clock extends Application {

    final String BRAND_NAME = "FROSH";
    final double CLOCK_RADIUS = 80;
    static AnalogueClock clock;
    protected static Birthday birthday;
    static Options options;
    
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {

// lay out the scene.
        Pane box = new Pane();
        box.setId("clocks");
        box.setPickOnBounds(false);
        box.getChildren().addAll(clock = makeAnalogueClock(stage), options = new Options(clock));
        box.setBackground(Background.EMPTY);
        birthday = new Birthday(clock.birthday);
        birthday.startBDayCheck();

        final Scene scene = new Scene(box);
        scene.setFill(Color.TRANSPARENT);

//        box.setOnMouseClicked(showOptionsHandler());
//sec is made to enable the app to run without showing on the taskbar
        Stage sec = new Stage();
        sec.initOwner(stage);
        sec.initStyle(StageStyle.TRANSPARENT);
        // show the scene.
        stage.initStyle(StageStyle.UTILITY);
        stage.setOpacity(0);
        stage.setAlwaysOnTop(true);
        sec.setScene(scene);
        sec.setX(Screen.getMainScreen().getWidth() * 0.88);
        sec.setY(Screen.getMainScreen().getHeight() * 0.1);
        EffectUtilities.makeDraggable(sec, box);
        stage.show();
        sec.show();
    }

    private AnalogueClock makeAnalogueClock(Stage stage) {
        final AnalogueClock analogueClock = new AnalogueClock(BRAND_NAME, CLOCK_RADIUS);
        analogueClock.setOnMouseEntered(ae -> analogueClock.glowOnHover());
        analogueClock.setOnMouseExited(ae -> analogueClock.exitGlowOnHover());
        EffectUtilities.fadeOnDoubleClick(analogueClock, closeStageEventHandler(stage));
        return analogueClock;
    }

    private EventHandler<ActionEvent> closeStageEventHandler(final Stage stage) {
        return (ActionEvent actionEvent) -> {
            stage.close();
        };
    }

    public static void toggleOptions(Node node, boolean bool) {
        options.layoutXProperty().bind(node.layoutXProperty());
        options.layoutXProperty().bind(node.layoutXProperty());
        options.setVisible(bool);
    }
}
