package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Hardness;


public class CustomController {
    @FXML
    private TextField row;

    @FXML
    private TextField col;

    @FXML
    private TextField mine;

    private int _row;

    private int _col;

    private int _mine;

    @FXML
    public void handlePressCancel(MouseEvent event) {
        closeWindow(event);
    }

    @FXML
    public void handlePressCreate(MouseEvent event) {
        try {
            _row = Integer.parseInt(row.getText());
            _col = Integer.parseInt(col.getText());
            _mine = Integer.parseInt(mine.getText());

            checkInput(event);
        }
        catch (NumberFormatException e) {
            alert("The inputs must be only numbers");
        }
    }


    private void alert(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Inputs");
        alert.setContentText(info);
        alert.showAndWait();
    }

    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        System.out.println("Close");
        try {
            stage.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Closed");
    }

    private void checkInput(MouseEvent event) {
        if (_row < 4 || _col < 4 || _mine <0) {
            alert("Input out of range");
        }
        else if (_row*_col-9 < _mine) {
            alert("too many mines");
        }
        else if (_row > 30 || _col > 30) {
            alert("Too big");
        }
        else {
            System.out.println("Create");
            Hardness.setCustom(_row,_col,_mine);
            Hardness.setHardness(Hardness.CUSTOM);

            closeWindow(event);
        }
    }

}
