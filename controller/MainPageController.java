package sample.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MainPageController {

    private static final int NUMCOLS = 30;
    private static final int NUMROWS = 30;

    @FXML
    private GridPane _pane;

    @FXML
    private AnchorPane _anchorPane;

    @FXML
    public void initialize() {

        //set up cols and rows of grid pane
        for (int i = 0; i < NUMCOLS; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(20);
            _pane.getColumnConstraints().add(col);
        }
        for (int i = 0; i < NUMROWS; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(20);
            _pane.getRowConstraints().add(row);
        }
        _pane.setHgap(2.0);
        _pane.setVgap(2.0);

        //add buttons
        for (int i = 0; i <NUMCOLS; i++) {
            for (int j = 0; j< NUMROWS; j++) {
                Rectangle button = new Rectangle(20,20, Color.LIGHTGREY);
                _pane.add(button, i, j);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            System.out.println("Right");
                            rightClick(getIndex(event.getSource()));
                        }
                        else {
                            System.out.println("Left");
                            leftClicked(getIndex(event.getSource()));
                        }
                    }
                });
            }
        }
    }

    public void rightClick(int[] index) {

    }

    public void leftClicked(int[] index) {

    }

    private int[] getIndex(Object node){
        int row = _pane.getRowIndex((Node)node);
        int col = _pane.getColumnIndex((Node)node);
        int[] index = {row,col};
        return index;
    }
}
