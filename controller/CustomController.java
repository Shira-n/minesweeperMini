package sample.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.awt.*;

public class CustomController {
    @FXML
    private TextField _row;

    @FXML
    private TextField _col;

    @FXML
    private TextField _mine;

    @FXML
    public void handlePressCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlePressCreate(ActionEvent event) {

    }
}
