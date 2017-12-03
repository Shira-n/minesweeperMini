package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MSminiMain extends Application {

    private static final Hardness DEFAULT = Hardness.EASY;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainPage.fxml"));
        primaryStage.setTitle("Minesweeper mini");
        primaryStage.setScene(new Scene(root, 1000,1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Hardness.setHardness(DEFAULT);
        //MineField f = new MineField();
        launch(args);
    }
}
