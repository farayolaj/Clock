/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package birthday;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author FarayolaJ
 */
public class BdayDialogController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private DatePicker date;
    @FXML
    private ListView<String> list;

    private final Birthday birthday = new Birthday();

    public void addToBDList(ActionEvent event) {
        String st = name.getText();
        LocalDate d = date.getValue();
        if (!st.equals("") && d != null) {
            birthday.add(st, d);
            list.setItems(birthday.getNames());
            name.setText("");
            date.setValue(null);
        }
    }

    public void deleteFromBDList(ActionEvent event) {
        String selected = list.getSelectionModel().getSelectedItem();
        if (selected != null) {
            list.getSelectionModel().clearSelection();
            birthday.remove(selected);
            list.setItems(birthday.getNames());
        }
    }
    
    public void close(ActionEvent event){  
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list.setItems(birthday.getNames());
    }

}
