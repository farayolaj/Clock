package clock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AnalogueClock extends Group {

    final int HOUR_HAND_LENGTH = 50;
    final int MINUTE_HAND_LENGTH = 75;
    final int SECOND_HAND_LENGTH = 88;
    final int SECOND_HAND_OFFSET = 15;
    private final Group ticks;
    private final Label digiClock;
    public final Label birthday;

    private final Line hourHand;
    private final Line minuteHand;
    private final Line secondHand;
    
    AnalogueClock(String brandName, double clockRadius) {
        this.setId("analogueClock");
        this.getStylesheets().add(ResourceResolver.getResourceFor(this.getClass(), "css/analogue-clock.css"));
        Circle face = this.createClockFace(clockRadius);
        hourHand = this.createHand("hourHand", clockRadius, 0.0D, this.percentOf(50.0D, clockRadius));
        minuteHand = this.createHand("minuteHand", clockRadius, 0.0D, this.percentOf(75.0D, clockRadius));
        secondHand = this.createHand("secondHand", clockRadius, this.percentOf(15.0D, clockRadius), this.percentOf(88.0D, clockRadius));
        this.bindClockHandsToTime(hourHand, minuteHand, secondHand);
        this.getChildren().addAll(new Node[]{face, this.digiClock = this.createDigitalClock(face), this.birthday = this.createBDayLabel(face), this.ticks = this.createTicks(clockRadius), this.createSpindle(clockRadius), hourHand, minuteHand, secondHand});
    }

    private Group createTicks(double clockRadius) {
        double TICK_START_OFFSET = this.percentOf(83.0D, clockRadius);
        double TICK_END_OFFSET = this.percentOf(93.0D, clockRadius);
        Group ticks = new Group();

        for (int i = 0; i < 12; ++i) {
            if (i != 0 && i != 3 && i != 6 && i != 9) {
                Line tick = new Line(0.0D, -TICK_START_OFFSET, 0.0D, -TICK_END_OFFSET);
                tick.getStyleClass().add("tick");
                tick.setLayoutX(clockRadius);
                tick.setLayoutY(clockRadius);
                tick.getTransforms().add(new Rotate((i * 30)));
                ticks.getChildren().add(tick);
            } else {
                Label tick = new Label(Integer.toString(i == 0 ? 12 : i));
                tick.setLayoutX(clockRadius - 3.0D);
                tick.setLayoutY(clockRadius - 8.0D);
                tick.setTextFill(Color.WHEAT);
                tick.getStyleClass().add("numtick");
                switch (i) {
                    case 0:
                        tick.setTranslateY(-clockRadius * 0.9D);
                        tick.setTranslateX(-9.0D);
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 7:
                    case 8:
                    default:
                        break;
                    case 3:
                        tick.setTranslateX(clockRadius * 0.9D - 4.0D);
                        tick.setTranslateY(-5.0D);
                        break;
                    case 6:
                        tick.setTranslateY(clockRadius * 0.9D - 10.0D);
                        tick.setTranslateX(-3.0D);
                        break;
                    case 9:
                        tick.setTranslateX(-clockRadius * 0.9D);
                        tick.setTranslateY(-5.0D);
                }

                ticks.getChildren().add(tick);
            }
        }

        return ticks;
    }

    private Circle createSpindle(double clockRadius) {
        Circle spindle = new Circle(clockRadius, clockRadius, 5.0D);
        spindle.setId("spindle");
        return spindle;
    }

    private Circle createClockFace(double clockRadius) {
        Circle face = new Circle(clockRadius, clockRadius, clockRadius);
        face.setId("face");
        return face;
    }

    private Line createHand(String handId, double clockRadius, double handOffsetLength, double handLength) {
        Line secondHand = new Line(0.0D, handOffsetLength, 0.0D, -handLength);
        secondHand.setLayoutX(clockRadius);
        secondHand.setLayoutY(clockRadius);
        secondHand.setId(handId);
        return secondHand;
    }

    private Label createBrand(Circle face, String brandName) {
        Label brand = new Label(brandName);
        brand.setId("brand");
        brand.layoutXProperty().bind(face.centerXProperty().subtract(brand.widthProperty().divide(2)));
        brand.layoutYProperty().bind(face.centerYProperty().add(face.radiusProperty().divide(3)));
        brand.setTextFill(Color.WHEAT);
        return brand;
    }

    private Label createDigitalClock(Circle face) {
        DigitalClock digi = new DigitalClock();
        digi.setId("brand");
        digi.layoutXProperty().bind(face.centerXProperty().subtract(digi.widthProperty().divide(2)));
        digi.layoutYProperty().bind(face.centerYProperty().add(face.radiusProperty().divide(4)));
        digi.setTextFill(Color.WHEAT);
        return digi;
    }

    private Label createBDayLabel(Circle face) {
        Label greet = new Label();
        greet.setId("brand");
        greet.layoutXProperty().bind(face.centerXProperty().subtract(greet.widthProperty().divide(2)));
        greet.layoutYProperty().bind(face.centerYProperty().subtract(face.radiusProperty().divide(3)));
        greet.setTextFill(Color.WHEAT);
        greet.setTextAlignment(TextAlignment.CENTER);
        this.startBDayCheck();
        return greet;
    }

    private void bindClockHandsToTime(Line hourHand, Line minuteHand, Line secondHand) {
        LocalTime time = LocalTime.now();
        double initialHourhandDegrees = this.calculateHourHandDegrees(time);
        double initialMinuteHandDegrees = this.calculateMinuteHandDegrees(time);
        double initialSecondHandDegrees = this.calculateSecondHandDegrees(time);
        this.createRotationTimeline(this.createRotate(hourHand, initialHourhandDegrees).angleProperty(), Duration.hours(12.0D), initialHourhandDegrees);
        this.createRotationTimeline(this.createRotate(minuteHand, initialMinuteHandDegrees).angleProperty(), Duration.minutes(60.0D), initialMinuteHandDegrees);
        this.createRotationTimeline(this.createRotate(secondHand, initialSecondHandDegrees).angleProperty(), Duration.seconds(60.0D), initialSecondHandDegrees);
    }

    private Rotate createRotate(Line hand, double initialHandDegrees) {
        Rotate hourRotate = new Rotate(initialHandDegrees);
        hand.getTransforms().add(hourRotate);
        return hourRotate;
    }

    private void createRotationTimeline(DoubleProperty angleProperty, Duration duration, double initialRotation) {
        Timeline timeline = new Timeline(new KeyFrame[]{new KeyFrame(duration, new KeyValue[]{new KeyValue(angleProperty, 360.0D + initialRotation, Interpolator.LINEAR)})});
        timeline.setCycleCount(-1);
        timeline.play();
    }

    private int calculateSecondHandDegrees(LocalTime time) {
        return time.get(ChronoField.SECOND_OF_MINUTE) * 6;
    }

    private double calculateMinuteHandDegrees(LocalTime time) {
        return (time.get(ChronoField.MINUTE_OF_HOUR) + this.calculateSecondHandDegrees(time) / 360.0D) * 6.0D;
    }

    private double calculateHourHandDegrees(LocalTime time) {
        return (time.get(ChronoField.CLOCK_HOUR_OF_AMPM) + this.calculateMinuteHandDegrees(time) / 360.0D) * 30.0D;
    }

    private double percentOf(double percent, double clockRadius) {
        return percent / 100.0D * clockRadius;
    }

    public void glowOnHover() {
        Glow glow = new Glow(0.6D);
        this.digiClock.setEffect(glow);
        this.ticks.setEffect(glow);
        if (!"".equals(this.birthday.getText())) {
            this.birthday.setEffect(glow);
        }

    }

    public void exitGlowOnHover() {
        this.digiClock.setEffect(null);
        this.ticks.setEffect(null);
        if (!"".equals(this.birthday.getText())) {
            this.birthday.setEffect(null);
        }

    }

    private boolean isBirthday() {
        return LocalDate.now().equals(LocalDate.of(LocalDate.now().getYear(), 1, 12));
    }

    private void checkBirthday() {
        if (this.isBirthday()) {
            this.birthday.setText("HAPPY BIRTHDAY\nMICHAEL!");
        } else {
            this.birthday.setText("");
        }

    }

    private void startBDayCheck() {
        Timeline days = new Timeline(new KeyFrame[]{new KeyFrame(Duration.seconds(1.0D), (ae) -> {
            this.checkBirthday();
        }, new KeyValue[0])});
        days.setCycleCount(Animation.INDEFINITE);
        days.play();
    }
    
    public void toggleSVisibility(){
        if(secondHand.isVisible()) secondHand.setVisible(false);
        else secondHand.setVisible(true);
    }
    
    public void toggleMVisibility(){
        if(minuteHand.isVisible()) minuteHand.setVisible(false);
        else minuteHand.setVisible(true);
    }
    
    public void toggleHVisibility(){
        if(hourHand.isVisible()) hourHand.setVisible(false);
        else hourHand.setVisible(true);
    }
}
