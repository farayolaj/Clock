/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clock;

import utilities.EffectUtilities;
import birthday.Birthday;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author FarayolaJ
 */
public class Options extends VBox {

    final private Label addRemove, second, minute, hour;
    final private Pane aPane, sPane, mPane, hPane;
    private final AnalogueClock clock;
    private Birthday bday;

    public Options(AnalogueClock clock) {
        this.clock = clock;

        addRemove = new Label("Add/Remove Birthdays");
        second = new Label("Show/Hide Second Hand");
        minute = new Label("Show/Hide Minute Hand");
        hour = new Label("Show/Hide Hour Hand");
        
        aPane = new Pane(addRemove);
        sPane = new Pane(second);
        mPane = new Pane(minute);
        hPane = new Pane(hour);
        
        addRemove.minWidth(this.getWidth());
        second.minWidth(this.getWidth());
        minute.minWidth(this.getWidth());
        hour.minWidth(this.getWidth());
        
        addRemove.minHeight(aPane.getHeight()+6);
        second.minHeight(aPane.getHeight()+6);
        minute.minHeight(aPane.getHeight()+6);
        hour.minHeight(aPane.getHeight()+6);

        this.setPadding(new Insets(0, 4, 0, 4));
        this.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        this.setEvents();

        this.getChildren().addAll(aPane, sPane, mPane, hPane);
        this.setVisible(false);   
    }

    private void setEvents() {
        setOnMouseEntered((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                setCursor(Cursor.DEFAULT);
            }
        });

        second.setOnMouseClicked(me -> {
            clock.toggleSVisibility();
            this.setVisible(false);
        });
        
        minute.setOnMouseClicked(me -> {
            clock.toggleMVisibility();
            this.setVisible(false);
        });
        
        hour.setOnMouseClicked(me -> {
            clock.toggleHVisibility();
            this.setVisible(false);
        });
        
        EffectUtilities.highlightOnHover(sPane);
        EffectUtilities.highlightOnHover(mPane);
        EffectUtilities.highlightOnHover(hPane);
        EffectUtilities.highlightOnHover(aPane);

        addRemove.setOnMouseClicked(me -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/birthday/fxml/bdayDialog.fxml"));
                AnchorPane pane = loader.load();
                Stage dialog = new Stage();
                this.setVisible(false);
                dialog.setScene(new Scene(pane));
                dialog.sizeToScene();
                dialog.initStyle(StageStyle.UNDECORATED);
                dialog.show();
            } catch (IOException ex) {
                Logger.getLogger(Options.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
