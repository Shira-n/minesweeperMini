package sample;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell {
    private boolean _check;
    private int _value;

    public Cell (int value, int row, int column, GridPane pane) {
        _value = value;

    }
}
