package sample.controller;

import com.sun.corba.se.impl.orbutil.concurrent.SyncUtil;
import com.sun.deploy.net.proxy.RemoveCommentReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import sample.MineField;

import java.util.ArrayList;

public class MainPageController {
    @FXML
    private GridPane _pane;

    @FXML
    private AnchorPane _anchorPane;

    private MineField _field;
    private boolean[][] _clearArea;

    @FXML
    public void initialize() {
        //get user customed size here

        _field = new MineField();
        int rowNum = _field.getRow();
        int colNum = _field.getCol();
        _clearArea = new boolean[rowNum][colNum];

        //set up cols and rows of grid pane
        for (int i = 0; i < rowNum; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(20);
            _pane.getRowConstraints().add(row);
        }
        for (int i = 0; i < colNum; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(20);
            _pane.getColumnConstraints().add(col);
        }
        _pane.setHgap(2.0);
        _pane.setVgap(2.0);

        //add buttons
        for (int i = 0; i <colNum; i++) {
            for (int j = 0; j < rowNum; j++) {
                Rectangle button = new Rectangle(20,20, Color.LIGHTGREY);
                _pane.add(button, i, j);
                button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if (event.getButton() == MouseButton.SECONDARY) {
                            System.out.println("Right");
                            rightClick((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                        else {
                            System.out.println("Left");
                            leftClicked((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                    }
                });
            }
        }
    }

    public void rightClick(Rectangle selected, int[] index) {
        selected.setFill(Color.RED);
    }

    public void leftClicked(Rectangle selected, int[] index) {
        System.out.println(_field.getNum(index[0],index[1]));
        System.out.println(index[0]+" " + index[1]);

        ArrayList<int []> pos = _field.sweep(index[0], index[1]);
        if (pos == null){
            gameOver();
        }else{
            for (int[] clear : pos) {
                int row = clear[0];
                int col = clear[1];

               // _pane.getChildren().remove(col, row);
                Label label = new Label("" + _field.getNum(row, col));
                label.setPrefSize(20, 20);
                label.setAlignment(Pos.CENTER);
                _pane.add(label, col, row);
            }
        }

/*
        System.out.println(index[0]+" "+ index[1]);
       int value = _field.getNum(index[0],index[1]);
        System.out.println(value);
        if (value==-1) {
            gameOver();
        }
        else {
            selected.setVisible(false);
            Label label = new Label ("" + value);
            label.setPrefSize(20,20);
            label.setAlignment(Pos.CENTER);
            _pane.add(label,index[1], index[0]);
        }
*/
    }

    private void gameOver() {
        System.out.println("Game Over");
    }

    private int[] getIndex(Object node){
        int row = _pane.getRowIndex((Node)node);
        int col = _pane.getColumnIndex((Node)node);
        int[] index = {row,col};
        return index;
    }
}
