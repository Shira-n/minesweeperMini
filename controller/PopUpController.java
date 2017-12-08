package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public class PopUpController {

    @FXML
    private TextField _nameField;

    private static String _name;

    @FXML
    public void handlePressOk(MouseEvent event) {
        _name = _nameField.getText();
    }

    @FXML
    public void handlePressAnon(MouseEvent event) {
        closeWindow(event);
    }


    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
