package clock;

import utilities.StringUtilities;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Creates a digital clock display as a simple label.
 * Format of the clock display is hh:mm:ss aa, where:
 *   hh Hour in am/pm (1-12)
 *   mm Minute in hour
 *   ss Second in minute
 *   aa Am/pm marker
 * Time is the system time for the local timezone.
 * see: digital-clock.css for css formatting rules for the clock.
 */
 
 public class DigitalClock extends Label {
  public DigitalClock() {
    setId("digitalClock");

    getStylesheets().add(
      ResourceResolver.getResourceFor(
        getClass(),
          "css/digital-clock.css"
      )
    );

    bindToTime();
  }

  // the digital clock updates once a second.
  private void bindToTime() {
    Timeline timeline = new Timeline(
      new KeyFrame(Duration.seconds(0),
        new EventHandler<ActionEvent>() {
          @Override public void handle(ActionEvent actionEvent) {
            LocalTime time = LocalTime.now();
            String hourString   = StringUtilities.pad(2, ' ', time.get(ChronoField.CLOCK_HOUR_OF_AMPM) == 0 ? "12" : time.get(ChronoField.CLOCK_HOUR_OF_AMPM) + "");
            String minuteString = StringUtilities.pad(2, '0', time.get(ChronoField.MINUTE_OF_HOUR) + "");
            String secondString = StringUtilities.pad(2, '0', time.get(ChronoField.SECOND_OF_MINUTE) + "");
            String ampmString   = time.get(ChronoField.AMPM_OF_DAY) == 0 ? "AM" : "PM";
            setText(hourString + ":" + minuteString + ":" + secondString + " " + ampmString);
          }
        }
      ),
      new KeyFrame(Duration.seconds(1))
    );
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
