package utilities;

import clock.Clock;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Various utilities for applying different effects to nodes.
 */
public class EffectUtilities {

    /**
     * configures the node to fade when it is clicked on performed the
     * onFinished handler when the fade is complete
     * @param node
     * @param onFinished
     */
    public static void fadeOnDoubleClick(final Node node, final EventHandler<ActionEvent>... onFinished) {
        node.setOnMouseClicked((MouseEvent mouseEvent) -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                node.setMouseTransparent(true);
                FadeTransition fade = new FadeTransition(Duration.seconds(1.2), node);
                fade.setOnFinished(onFinished[0]);
                fade.setFromValue(1);
                fade.setToValue(0);
                fade.play();
            }
            else if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                Clock.toggleOptions(node, false);
            }
            else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                Clock.toggleOptions(node, true);
            }
        });
    }

    /* adds a glow effect to a node when the mouse is hovered over the node */
    public static void addGlowOnHover(final Node node) {
        final Glow glow = new Glow(0.6);
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(glow);
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setEffect(null);
            }
        });
    }
    
    public static void highlightOnHover(final Pane node) {
        node.setOnMouseEntered(
                me -> node.setBackground(
                        new Background(
                                new BackgroundFill(
                                        javafx.scene.paint.Color.WHEAT, CornerRadii.EMPTY, Insets.EMPTY
                                )
                        )
                )
        );
        
        node.setOnMouseExited(
                me -> node.setBackground(
                        new Background(
                                new BackgroundFill(
                                        javafx.scene.paint.Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY
                                )
                        )
                )
        );
    }

    /**
     * makes a stage draggable using a given node
     */
    public static void makeDraggable(final Stage stage, final Node byNode) {
        final Delta dragDelta = new Delta();
        byNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = stage.getX() - mouseEvent.getScreenX();
                dragDelta.y = stage.getY() - mouseEvent.getScreenY();
                byNode.setCursor(Cursor.MOVE);
            }
        });
        byNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                byNode.setCursor(Cursor.HAND);
            }
        });
        byNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage.setX(mouseEvent.getScreenX() + dragDelta.x);
                stage.setY(mouseEvent.getScreenY() + dragDelta.y);
            }
        });
        byNode.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    byNode.setCursor(Cursor.HAND);
                }
            }
        });
        byNode.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    byNode.setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    /**
     * records relative x and y co-ordinates.
     */
    private static class Delta {

        double x, y;
    }
}
