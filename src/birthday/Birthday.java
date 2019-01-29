/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package birthday;

import java.time.LocalDate;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 *
 * @author FarayolaJ
 */
//Create the Add/Remove Birthday Dialog and bind to the instance of Birthday
public class Birthday {

    private Label birthday;

    public Birthday() {
        birthday = null;
    }

    public Birthday(Label l) {
        this();
        birthday = l;
    }

    //<editor-fold defaultstate="collapsed" desc="getInstance() Method">
    /*    public static Birthday getInstance() {
    try {
    return (Birthday) new ObjectInputStream(new FileInputStream("birthdays.txt")).readObject();
    } catch (FileNotFoundException ex) {
    return new Birthday();
    } catch (IOException | ClassNotFoundException ex) {
    Logger.getLogger(Birthday.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex) {
    Logger.getLogger(Birthday.class.getName()).log(Level.SEVERE, null, ex);
    }
    return new Birthday();
    }*/
//</editor-fold>
    public void add(String name, LocalDate bd) {
        BirthdayStorage.put(name, bd);
    }

    public void remove(String name) {
        BirthdayStorage.remove(name);
    }

    private boolean isBirthday(LocalDate bd) {
        return LocalDate.now().equals(LocalDate.of(LocalDate.now().getYear(), bd.getMonthValue(), bd.getDayOfMonth()));
    }

    private void checkBirthday() {
        BirthdayStorage.keySet().forEach((bd) -> {
            if (isBirthday(BirthdayStorage.get(bd))) {
//                setText() will later be changed to speech...
                birthday.setText("HAPPY BIRTHDAY\n" + bd + "!");
            } else {
                birthday.setText("");
            }
        });
    }

    public ObservableList<String> getNames() {
        ArrayList<String> list = new ArrayList<>();
        BirthdayStorage.keySet().forEach((each) -> list.add(each));
        return FXCollections.observableArrayList(list);
    }

    public void startBDayCheck() {
        Timeline days = new Timeline(new KeyFrame(Duration.minutes(1), ae -> checkBirthday()));
        days.setCycleCount(Animation.INDEFINITE);
        days.play();
    }
}
