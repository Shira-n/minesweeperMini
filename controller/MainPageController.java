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

    private MineField _field;

    private ArrayList<Node> checkedSquare = new ArrayList<Node>();

    private int _row;

    private int _col;


    @FXML
    public void initialize() {
        //get user customed size here

        _field = new MineField();
        int rowNum = _field.getRow();
        int colNum = _field.getCol();

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
            for (int j = 0; j< rowNum; j++) {
                Rectangle rect = new Rectangle(20,20, Color.LIGHTGREY);
                _pane.add(rect,i,j);
                //add event handler for each square
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //if the square senses a right click action
                        if (event.getButton() == MouseButton.SECONDARY) {
                            System.out.println("Right");
                            rightClick((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                        //when the square senses a left click action
                        else {
                            System.out.println("Left");
                            leftClicked((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                    }
                });
            }
        }
    }

    /**
     * Method handles when user performs a right click on a certain square. If the square is already
     * flagged (red color) then change it back to un-flagged (grey), and vice versa.
     * @param selected
     * @param index
     */
    public void rightClick(Rectangle selected, int[] index) {
        //if the square has been flagged before, un-flag it
        if (selected.getFill().equals(Color.RED)) {
            selected.setFill(Color.LIGHTGREY);
        }
        //if the sqaure has not been flagged, then flag
        else {
            selected.setFill(Color.RED);
        }
    }

    /**
     * Method is called when the user perform left click on a certain square. If the square is a mine,
     * then game over. Otherwise, it clears the square and check the surrounding squares.
     * @param selected
     * @param index
     */
    public void leftClicked(Rectangle selected, int[] index) {
        if (!selected.getFill().equals(Color.RED)){
            // if the user clicks on a mine, then game over
            if (_field.isMine(index[0],index[1])) {
                gameOver();
            } else {
                //find all the exposed squares generated from the click
                ArrayList<int[]> pos = _field.sweep(index[0], index[1]);
                //for each of the exposed square, clear the square
                for (int[] clear : pos) {
                    int row = clear[0];
                    int col = clear[1];
                    Rectangle node = (Rectangle) getNode(row, col);
                    //change the sqaure background to grey colour
                    node.setFill(Color.GRAY);

                    //get the value to be dsiplayed on the square
                    int value = _field.getNum(row, col);

                    //if the value is not 0, then display the number, otherwise leave it as a blank square
                    if (value != 0) {
                        //set square text to the value
                        Label label = new Label(""+ value);
                        //format the sqaure
                        label.setPrefSize(20, 20);
                        label.setAlignment(Pos.CENTER);
                        //change text colour to white
                        label.setTextFill(Color.WHITE);
                        _pane.add(label, col, row);
                    }
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
