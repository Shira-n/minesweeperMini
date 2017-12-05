package sample.controller;

import com.sun.glass.ui.Window;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
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
        }
        catch (NumberFormatException e) {
            alert("The inputs must be only numbers");
        }
        Hardness.setCustom(_row,_col,_mine);
        Hardness.setHardness(Hardness.CUSTOM);

        closeWindow(event);
    }


    private void alert(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Invalid Inputs");
        alert.setContentText(info);
        alert.showAndWait();
    }

    private void closeWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
