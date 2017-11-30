package sample.controller;

import javafx.collections.ObservableList;
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
import sample.Cell;
import sample.MineField;

import java.util.ArrayList;

public class MainPageController {


    @FXML
    private GridPane _pane;

    @FXML
    private AnchorPane _anchorPane;

    private MineField _field = new MineField();

    private ArrayList<Node> checkedSquare = new ArrayList<Node>();

    private int _row;

    private int _col;

    @FXML
    public void initialize() {

        _row = _field.getRow();
        _col = _field.getCol();

        //set up cols and rows of grid pane
        for (int i = 0; i < _col; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(20);
            _pane.getColumnConstraints().add(col);
        }
        for (int i = 0; i < _row; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(20);
            _pane.getRowConstraints().add(row);
        }
        _pane.setHgap(2.0);
        _pane.setVgap(2.0);

        //add buttons
        for (int i = 0; i <_col; i++) {
            for (int j = 0; j< _row; j++) {
                Rectangle rect = new Rectangle(20,20, Color.LIGHTGREY);
                _pane.add(rect,i,j);
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
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
        if (selected.getFill().equals(Color.RED)) {
            selected.setFill(Color.LIGHTGREY);
        }
        else {
            selected.setFill(Color.RED);
        }
    }

    public void leftClicked(Rectangle selected, int[] index) {
        System.out.println(index[0]+" "+ index[1]);
       int value = _field.getNum(index[0],index[1]);
        System.out.println(value);
        if (value==-1) {
            gameOver();
        }
        else{
            clearSquare(selected, value);
        }

    }

    private void clearSquare(Rectangle selected, int value) {
        selected.setVisible(false);
        if (value == 0) {
            recursiveClear(selected);
        }
        else {
            Label label = new Label("" + value);
            label.setPrefSize(20, 20);
            label.setAlignment(Pos.CENTER);

            if (value == 1) {
                label.setTextFill(Color.BLUE);
            } else if (value == 2) {
                label.setTextFill(Color.GREEN);
            } else if (value == 3) {
                label.setTextFill(Color.PINK);
            } else if (value == 4) {
                label.setTextFill(Color.PURPLE);
            }

            _pane.add(label, _pane.getColumnIndex(selected),_pane.getRowIndex(selected));
        }
    }

    private void recursiveClear(Rectangle origin) {
        //check middle left square
        if ((_pane.getColumnIndex(origin) != 0)) {
            Node node = getNode(_pane.getRowIndex(origin), _pane.getColumnIndex(origin) - 1);
            if (_field.getNum(_pane.getRowIndex(origin), _pane.getColumnIndex(origin) - 1) == 0) {
                if (!checkedSquare.contains(node)) {
                    checkedSquare.add((node));
                    node.setVisible(false);
                    recursiveClear((Rectangle) node);
                }
            }
            else {

            }
        }
            //check top center square
            if (_pane.getRowIndex(origin) != 0) {
                if (_field.getNum(_pane.getRowIndex(origin) - 1, _pane.getColumnIndex(origin)) == 0) {
                    Node node = getNode(_pane.getRowIndex(origin) - 1, _pane.getColumnIndex(origin));
                    if (!checkedSquare.contains(node)) {
                        checkedSquare.add((node));
                        node.setVisible(false);
                        recursiveClear((Rectangle) node);
                    }
                }
            }
            //check middle right square
            if (_pane.getColumnIndex(origin) < (_col - 1)) {
                if (_field.getNum(_pane.getRowIndex(origin), _pane.getColumnIndex(origin) + 1) == 0) {
                    Node node = getNode(_pane.getRowIndex(origin), _pane.getColumnIndex(origin) + 1);
                    if (!checkedSquare.contains(node)) {
                        checkedSquare.add((node));
                        node.setVisible(false);
                        recursiveClear((Rectangle) node);
                    }
                }
            }
            //check middle left square
            if (_pane.getRowIndex(origin) < (_row - 1)) {
                if (_field.getNum(_pane.getRowIndex(origin) + 1, _pane.getColumnIndex(origin)) == 0) {
                    Node node = getNode(_pane.getRowIndex(origin) + 1, _pane.getColumnIndex(origin));
                    if (!checkedSquare.contains(node)) {
                        checkedSquare.add((node));
                        node.setVisible(false);
                        recursiveClear((Rectangle) node);
                    }
                }
            }
        }


    private Node getNode (int row, int column) {
        Node result = null;
        ObservableList<Node> childrens = _pane.getChildren();

        for (Node node : childrens) {
            if(_pane.getRowIndex(node) == row && _pane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }


    private void revealValue() {

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
