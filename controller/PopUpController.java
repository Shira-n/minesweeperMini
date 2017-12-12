package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.RecordManager;


public class PopUpController {

    @FXML
    private TextField _nameField;

    private static String _name;


    @FXML
    public void handlePressOk(MouseEvent event) {
        _name = _nameField.getText();
        closeWindow(event);
    }

    @FXML
    public void handlePressAnon(MouseEvent event) {
        _name = "";
        closeWindow(event);
    }


    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public static String getName() {
        return _name;
    }
}
