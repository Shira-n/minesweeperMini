package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.RecordManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PopUpController {

    @FXML
    private Label _alert;

    @FXML
    private TextField _nameField;

    private static String _name;


    @FXML
    public void handlePressOk(MouseEvent event) {
        _name = _nameField.getText();
        if (checkName()&&!(_name.equals(""))){
            closeWindow(event);
        }
        else if (_name.equals("")){
            _nameField.clear();
            _alert.setText("please enter a username");
        }
        else {
            _nameField.clear();
            _alert.setText("only letters,numbers and underscores.");
        }

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

    private boolean checkName() {
        Pattern p = Pattern.compile("^[a-zA-Z0-9_]*$");
        Matcher m = p.matcher(_name);
        return m.matches();
    }

}
