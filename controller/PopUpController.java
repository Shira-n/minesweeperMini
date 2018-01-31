package sample.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PopUpController {

    @FXML
    private Label _alert;

    @FXML
    private TextField _nameField;

    @FXML
    private Button _ok;

    private static String _name;


    @FXML
    public void initialize() {
        _nameField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()== KeyCode.ENTER){
                    readInput();
                }
            }
        });
    }
    @FXML
    public void handlePressOk(MouseEvent event) {
        readInput();
    }

    @FXML
    public void handlePressAnon(MouseEvent event) {
        _name = "";
        closeWindow();
    }

    private void readInput() {
        _name = _nameField.getText();
        if(_name.length() > 14){
            _nameField.clear();
            _alert.setText("No more than 14 letters");
        }
        else if (_name.equals("")){
            _nameField.clear();
            _alert.setText("Please enter a username");
        }
        else if (checkName()){
            closeWindow();
        }
        else {
            _nameField.clear();
            _alert.setText("Only letters,numbers and underscores.");
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) ((Node) _nameField).getScene().getWindow();
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
