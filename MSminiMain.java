package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MSminiMain extends Application {

    //private static final Hardness DEFAULT = Hardness.getHardness();

    public static int EAZ_RECORD;
    public static String EAZ_KEEPER;
    public static int MED_RECORD;
    public static String MED_KEEPER;
    public static int EXP_RECORD;
    public static String EXP_KEEPER;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainPage.fxml"));
        primaryStage.setTitle("Minesweeper mini");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        //Hardness.setHardness(DEFAULT);
        //if ()
        launch(args);
    }



}
