package sample.controller;

import com.sun.prism.impl.Disposer;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.RecordManager;

public class BestScoreController {

    @FXML
    private Label _easyName;

    @FXML
    private Label _medName;

    @FXML
    private Label _expertName;

    @FXML
    private Label _easyTime;

    @FXML
    private Label _medTime;

    @FXML
    private Label _expertTime;

    private RecordManager mgr;

    @FXML
    public void initialize() {
        mgr = MainPageController.getRecorder();
        setUpPage();
    }
    @FXML
    public void handlePressOk(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void handlePressReset(MouseEvent event) {
        mgr.resetRecord();
        setUpPage();
    }

    private void setText(Label name, Label time, int record, String keeper) {
        if (record < 0) {
            name.setText("--");
            time.setText("--");
        }
        else {
            name.setText(keeper);
            time.setText(""+record);
        }
    }

    private void setUpPage() {
        setText(_easyName, _easyTime, mgr.getEazRec(),mgr.getEazKeeper());
        setText(_medName, _medTime, mgr.getMedRec(), mgr.getMedKeeper());
        setText(_expertName,_expertTime,mgr.getExpRec(), mgr.getExpKeeper());
    }
}

