package sample.controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Hardness;
import sample.MineField;
import sample.MineNumExceedException;

import java.util.ArrayList;

public class MainPageController {
    @FXML
    private GridPane _pane;

    @FXML
    private SplitPane _splitPane;

    @FXML
    private Label _gameOver;

    @FXML
    private Label _leftNum;

    private boolean _firstClick;
    private MineField _field;

    private int _row;
    private int _col;
    private int _mineNum;

    private ArrayList<int[]> _premarked = new ArrayList<>();
    private int _left;

    @FXML
    public void initialize() {

        _gameOver.setVisible(false);

        //get user customised parameters
        _row = Hardness.getRow();
        _col = Hardness.getCol();
        _mineNum = Hardness.getMine();
        _left = _mineNum;
        _leftNum.setText(""+_left);

        //Set up blank field, waiting for the first click
        _firstClick = true;

        //create the field with selected size
        initialiseField();
        //add squares into field
        setUpSquares();
    }


    /**
     * create the field with size according to hardness level
     */
    private void initialiseField() {
        //Set up cols and rows of grid pane
        for (int i = 0; i < _row; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(20);
            _pane.getRowConstraints().add(row);
        }
        for (int i = 0; i < _col; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(20);
            _pane.getColumnConstraints().add(col);
        }
        _pane.setHgap(2.0);
        _pane.setVgap(2.0);
    }

    /**
     * add and set up squares into the grid pane
     */
    private void setUpSquares() {
        //Add buttons
        for (int i = 0; i <_col; i++) {
            for (int j = 0; j< _row; j++) {
                Rectangle rect = new Rectangle(20,20, Color.LIGHTGREY);
                _pane.add(rect,i,j);
                //add event handler for each square
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //if the square senses a right click action
                        if (event.getButton() == MouseButton.SECONDARY) {
                            rightClick((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                        //when the square senses a left click action
                        else {
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
     */
    public void rightClick(Rectangle selected, int[] index) {
        //if the square has been flagged before, un-flag it
        if (!_firstClick) {
            _field.mark(index[0], index[1]);
        }else{
            if (_premarked.contains(index)){
                _premarked.remove(index);
            }else{
                _premarked.add(index);
            }
        }

        if (selected.getFill().equals(Color.RED)) {
            selected.setFill(Color.LIGHTGREY);
            _left++;
            _leftNum.setText(""+_left);
        }
        //if the sqaure has not been flagged, then flag
        else {
            selected.setFill(Color.RED);
            _left--;
            _leftNum.setText(""+_left);
        }
    }

    /**
     * Method is called when the user perform left click on a certain square. If the square is a mine,
     * then game over. Otherwise, it clears the square and check the surrounding squares.
     */
    public void leftClicked(Rectangle selected, int[] index) {
        if (_firstClick){               //Generate a new field that the first click block must be 0
            _firstClick = false;
            try {
                _field = new MineField(_row, _col, _mineNum, index[0], index[1]);
                for (int[] i : _premarked){
                    _field.mark(i[0], i[1]);
                }
                ArrayList<int[]> pos = _field.ripple(index[0], index[1]);
                revealNodes(pos);
            }catch (MineNumExceedException e){
                //TODO popup or sth to indicate # of mines out of limit
            }
        }else {
            if (!selected.getFill().equals(Color.RED)) {
                // if the user clicks on a mine, then game over
                if (_field.isMine(index[0], index[1])) {
                    gameOver(selected);
                } else {
                    //find all the exposed squares generated from the click
                    ArrayList<int[]> pos = _field.ripple(index[0], index[1]);
                    revealNodes(pos);
                }
            }
        }
        checkWon();
    }

    /**
     * reveal a list of nodes at once
     */
    private void revealNodes(ArrayList<int[]> list) {
        //for each of the exposed square, clear the square
        for (int[] clear : list) {
            int row = clear[0];
            int col = clear[1];
            Rectangle node = (Rectangle) getNode(row, col);
            clearSquare (node, row, col);
        }
    }

    /**
     * reveal the value of a particular square
     */
    private void clearSquare(Rectangle selected, int row, int col){
        //change the sqaure background to grey colour
        selected.setFill(Color.GRAY);

        //get the value to be dsiplayed on the square
        int value = _field.getNum(row, col);
        if (value == -1) {
            gameOver(selected);
        }
        //if the value is not 0, then display the number, otherwise leave it as a blank square
        else if (value != 0) {
            //set square text to the value
            Label label = new Label("" + value);
            //format the sqaure
            label.setPrefSize(20, 20);
            label.setAlignment(Pos.CENTER);
            //change text colour to white
            label.setTextFill(Color.WHITE);
            _pane.add(label, col, row);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    revealNodes(_field.sweep(row,col));
                    checkWon();
                }
            });
        }
    }

    /**
     * get the square at a particular position
     * @param row
     * @param column
     * @return
     */
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


    /**
     * shows game over gui
     * @param selected
     */
    private void gameOver(Rectangle selected) {
        selected.setFill(Color.BLUE);
        System.out.println("Game Over");
        _gameOver.setVisible(true);
        _pane.setDisable(true);

    }

    private void checkWon() {
        if (_field.hasWon()){
            System.out.println("won");
            _gameOver.setText("won");
            _gameOver.setVisible(true);
            _pane.setDisable(true);
        }
    }


    /**
     * find position of a practicular node
     * @param node
     * @return
     */
    private int[] getIndex(Object node){
        int row = _pane.getRowIndex((Node)node);
        int col = _pane.getColumnIndex((Node)node);
        int[] index = {row,col};
        return index;
    }


    @FXML
    public void handlePressEasy(ActionEvent event) {
        System.out.println("easy");
        Hardness.setHardness(Hardness.EASY);
        newGame();
    }

    @FXML
    public void handlePressIntermediate(){
        System.out.println("intermediate");
        Hardness.setHardness(Hardness.INTERMEDIATE);
        newGame();
    }

    @FXML
    public void handlePressExpert() {
        System.out.println("expert");
        Hardness.setHardness(Hardness.EXPERT);
        newGame();
    }

    @FXML
    public void handlePressCustom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample/view/Custom.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(_pane.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);

            stage.showAndWait();
        }
        catch (Exception e) {

        }


    }

    @FXML
    public void handlePressRestart() {
        newGame();
    }


    /*
    create a new game with selected hardness setting
     */
    private void newGame() {
        try {
            _pane.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/sample/view/MainPage.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Minesweeper mini");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception ex) {
        }
    }

}
