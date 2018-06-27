package sample.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SettingController {


    private final static int DEFAULT_VALUE = 30;

    @FXML
    private Slider _slider;

    private int _value;

    /*
    Initialise the slide bar for setting square size
     */
    @FXML
    public void initialize() {
        _slider.setMin(10);
        _slider.setMax(50);
        _slider.setValue(DEFAULT_VALUE);
        _slider.setBlockIncrement(1);

        _slider.valueProperty().addListener((
                ObservableValue<? extends Number> ov, Number old_val,
                Number new_val) -> {
                _value = new_val.intValue();
        });
    }


    /*
    Called when user confirms their square size
     */
    @FXML
    public void handlePressOk(MouseEvent event) {

        Stage mainStage = MainPageController.changeSquareSize(_value);
        //load game with new square size
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/sample/view/MainPage.fxml"));
            mainStage.setScene(new Scene(root));
            mainStage.setResizable(false);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
