package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlePressCreate(MouseEvent event) {
        try {
            _row = Integer.parseInt(row.getText());
            _col = Integer.parseInt(col.getText());
            _mine = Integer.parseInt(mine.getText());
        }
        catch (NumberFormatException e) {

        }
        //Hardness.setCustom();
    }
}
