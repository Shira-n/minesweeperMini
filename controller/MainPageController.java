package sample.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.Hardness;
import sample.MineField;
import sample.RecordManager;
import sample.MSminiMain;

import java.util.ArrayList;

public class MainPageController {

    private static final Integer FINISHTIME = 999;
    private static final int DEFAULT_SQUARE_SIZE = 30;

    @FXML
    private GridPane _pane;

    @FXML
    private Label timerLabel;

    @FXML
    private Label _leftNum;

    @FXML
    private Button _restart;

    @FXML
    private ToolBar _bar;

    @FXML
    private MenuItem _easy;

    @FXML
    private MenuItem _intermediate;

    @FXML
    private MenuItem _expert;

    @FXML
    private MenuItem _custom;

    @FXML
    private MenuItem _leaderBoard;

    private MineField _field;
    private boolean _firstClick;
    private static RecordManager _record;

    private static Timeline _timeline;
    private int timeSeconds;

    private int _row;
    private int _col;
    private int _mineNum;

    private double xOffset = 0;
    private double yOffset = 0;

    private static Stage _stage;

    private ArrayList<int[]> _premarked = new ArrayList<>();
    private int _left;

    private static int _square_size = DEFAULT_SQUARE_SIZE;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
            Initialization
     */

    @FXML
    public void initialize() {
        _restart.setText("('w')");

        _timeline = null;
        timeSeconds = 0;
        timerLabel.setText(timeSeconds + "");

        _record = new RecordManager();

        Hardness.renewHardness();

        _row = Hardness.getRow();
        _col = Hardness.getCol();
        _mineNum = Hardness.getMine();
        _left = _mineNum;
        _leftNum.setText(""+_left);

        _firstClick = true;

        _restart.setText("('w')");
        initialiseField();

        setUpSquares();
        setDraggable();
        setUpMenu();
    }

    /**
     * Create the field with size according to hardness level
     */
    private void initialiseField() {
        //Set up cols and rows of grid pane
        for (int i = 0; i < _row; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(_square_size);
            _pane.getRowConstraints().add(row);
        }
        for (int i = 0; i < _col; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(_square_size);
            _pane.getColumnConstraints().add(col);
        }
        _pane.setHgap(1.0);
        _pane.setVgap(1.0);
    }

    /**
     * add and set up squares into the grid pane
     */
    private void setUpSquares() {
        //Add buttons
        for (int i = 0; i <_col; i++) {
            for (int j = 0; j< _row; j++) {
                Rectangle rect = new Rectangle(_square_size,_square_size, Color.LIGHTGREY);
                _pane.add(rect,i,j);

                //add event handler for each square
                rect.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //if the square senses a right click action
                        if ((event.getButton() == MouseButton.SECONDARY) && !(((Rectangle)event.getSource()).getFill().equals(Color.GREY))) {
                            rightClick((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                        //when the square senses a left click action
                        else {
                            setUpTimeline();
                            leftClicked((Rectangle)event.getSource(), getIndex(event.getSource()));
                        }
                    }
                });
            }
        }
    }

    /**
     * Enable the user to drag the window.
     */
    private void setDraggable(){
        _bar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset= event.getSceneY();
        });
        _bar.setOnMouseDragged(event -> {
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
            Left and Right Click
     */

    /**
     * Method handles when user performs a right click on a certain square. If the square is already
     * flagged (red color) then change it back to un-flagged (grey), and vice versa.
     */
    public void rightClick(Rectangle selected, int[] index) {
        //Mark the block
        if (!_firstClick) {
            _field.mark(index[0], index[1]);
        }else{
            if (_premarked.contains(index)){
                _premarked.remove(index);
            }else{
                _premarked.add(index);
            }
        }
        //Change GUI
        if (selected.getFill().equals(Color.RED)) {         //If the square has been flagged before, un-flag it
            selected.setFill(Color.LIGHTGREY);
            _left++;
            _leftNum.setText(""+_left);
        }
        else {      //If the sqaure has not been flagged, then flag
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
        if (_firstClick){       //Generate a new field that the first click block must be 0
            _firstClick = false;
            _field = new MineField(_row, _col, _mineNum, index[0], index[1]);
            for (int[] i : _premarked){
                _field.mark(i[0], i[1]);
            }
            ArrayList<int[]> pos = _field.ripple(index[0], index[1]);
            revealNodes(pos);
        }else {
            if (!selected.getFill().equals(Color.RED)) {    // if the user clicks on a mine, then game over
                if (_field.isMine(index[0], index[1])) {
                    gameOver(selected);
                } else {    //find all the exposed squares generated from the click
                    ArrayList<int[]> pos = _field.ripple(index[0], index[1]);
                    revealNodes(pos);
                }
            }
        }
        checkWon();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
            Supporting Methods
     */

    /**
     * Find position of a particular node
     */
    private int[] getIndex(Object node){
        int row = _pane.getRowIndex((Node)node);
        int col = _pane.getColumnIndex((Node)node);
        int[] index = {row,col};
        return index;
    }

    /**
     * Get the square at a particular position
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
     * Reveal a list of nodes at once
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
     * Reveal the value of a particular square
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
            label.setPrefSize(_square_size, _square_size);
            label.setFont(new Font("System", 15));
            label.setAlignment(Pos.CENTER);
            //change text colour to white
            label.setTextFill(Color.WHITE);
            _pane.add(label, col, row);
            label.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                revealNodes(_field.sweep(row,col));
                checkWon();
            });
        }
    }

    /**
     * Shows game over GUI
     */
    private void gameOver(Rectangle selected) {
        _restart.setText("(/w\\)");
        _pane.setDisable(true);
        _timeline.stop();
        for (int[] wrongMark : _field.getWrongMarks()){
            Rectangle node = (Rectangle)getNode(wrongMark[0],wrongMark[1]);
            Image img = new Image("sample/x.png");
            node.setFill(new ImagePattern(img));
        }
        for (int[] unmarked : _field.getUnmarkedMines()){
            Rectangle node = (Rectangle)getNode(unmarked[0],unmarked[1]);
            node.setFill(Color.DARKRED);
        }
        selected.setFill(Color.BLACK);
    }

    /**
     * Show won GUI
     */
    private void checkWon() {
        if (_field.hasWon()){
            _restart.setText("(*w*)");
            _pane.setDisable(true);
            _timeline.stop();
            updateRecord();
        }
    }

    private void newGame() {
        try {
            _stage = MSminiMain.getStage();
            _stage.hide();
            _pane.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("/sample/view/MainPage.fxml"));

            _stage.setTitle("Minesweeper mini");
            _stage.setScene(new Scene(root));
            _stage.setResizable(false);
            _stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Renew Timeline
     */
    private void setUpTimeline() {
        if(_timeline == null) {
            timerLabel.setText(timeSeconds + "");
            _timeline = new Timeline();
            _timeline.setCycleCount(Timeline.INDEFINITE);
            _timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), event -> {
                timeSeconds++;
                timerLabel.setText(timeSeconds + "");
                if (timeSeconds > FINISHTIME) {
                    _timeline.stop();
                }
            }));
            _timeline.play();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
            Menu Bar & Buttons
     */

    private void setUpMenu() {
        _easy.setOnAction(event -> {
            Hardness.setHardness(Hardness.EASY);
            newGame();
        });
        _easy.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));

        _intermediate.setOnAction(event -> {
            Hardness.setHardness(Hardness.INTERMEDIATE);
            newGame();
        });
        _intermediate.setAccelerator(new KeyCodeCombination(KeyCode.I, KeyCombination.CONTROL_DOWN));

        _expert.setOnAction(event -> {
            Hardness.setHardness(Hardness.EXPERT);
            newGame();
        });
        _expert.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));

        _custom.setOnAction(event -> {
            windowLoader("/sample/view/Custom.fxml");
            System.out.println("After close");
            try {
                newGame();
                System.out.println("After game");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        });

        _leaderBoard.setOnAction(event -> windowLoader("/sample/view/BestScore.fxml"));
    }

    @FXML
    public void handlePressRestart() {
        _timeline = null;
        newGame();
    }

    @FXML
    public void handlePressAbout(ActionEvent event) {
        windowLoader("/sample/view/About.fxml");
    }

    @FXML
    public void handlePressSetting(ActionEvent event) { popUpWindow("/sample/view/Setting.fxml"); }

    @FXML
    public void handlePressQuit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void windowLoader(String source) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initOwner(_pane.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void popUpWindow(String source) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
            Record
     */

    public static RecordManager getRecorder(){
        return _record;
    }

    private void updateRecord(){
        switch (Hardness.getHardness()){
            case EASY:
                if (_record.getEazRec() < 0 || timeSeconds < _record.getEazRec()){
                    _record.writeRecord(0,enterName(), timeSeconds);
                }
                break;
            case INTERMEDIATE:
                if (_record.getMedRec() < 0 || timeSeconds < _record.getMedRec()){
                    _record.writeRecord(1,enterName(), timeSeconds);
                }
                break;
            case EXPERT:
                if (_record.getExpRec() < 0 || timeSeconds < _record.getExpRec()){
                    _record.writeRecord(2,enterName(), timeSeconds);
                }
                break;
            default:
                break;
        }
    }

    private String enterName(){
        windowLoader("/sample/view/PopUp.fxml");

        String name = PopUpController.getName();
        if (name.length() < 1){
            name = "unknown";
        }
        return name;
    }

    ///////////////////////////////////////

    /*
    Change Square Size
     */
    public static Stage changeSquareSize(int len) {
        _square_size = len;
        return MSminiMain.getStage();
    }

    public static int getSquareSize(){
        return _square_size;
    }
}
