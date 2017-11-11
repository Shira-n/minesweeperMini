package sample;

import java.util.Arrays;

public class MineField {
    private static int DEFUALT_ROW = 16;
    private static int DEFUALT_COL = 30;
    private static int DEFUALT_MINES = 99;

    private int _row;
    private int _col;
    private int _mineNum;

    private int[][] _map;


    /**
     * Generate a mine field using defualt data
     */
    public MineField(){
        _row = DEFUALT_ROW;
        _col = DEFUALT_COL;
        _mineNum = DEFUALT_MINES;
        generateField();
    }

    /**
     * Allow custom mine fields
     */
    public MineField(int row, int col, int mineNum){
        _row = row;
        _col = col;
        _mineNum = mineNum;
        generateField();
    }

    private void generateField(){
        _map = new int[_row][_col];
        for (int[] row : _map) {
            Arrays.fill(row, 0);
        }
        plantMines();
        calcMap();

        //Print out the field
        for (int i = 0; i < _row; i++) {
            for (int j = 0; j < _col; j++) {
                if (_map[i][j] == -1){
                    System.out.print("*" + "\t");
                }else{
                    System.out.print(_map[i][j] + "\t");
                }
            }
            System.out.print("\n");
        }
    }

    /**
     * Randomly set 'mineNum' mines in the map
     */
    private void plantMines(){
        int i = 0;
        while (i < _mineNum) {
            int row = (int) (Math.random() * _row);
            int col = (int) (Math.random() * _col);
            if (_map[row][col] == 0) {
                _map[row][col] = -1;
                i++;
            }
        }
    }

    /**
     * Generate the number on every grid
     */
    private void calcMap(){
        for (int i = 0; i < _row; i++){
            for (int j = 0; j < _col; j++) {
                if (_map[i][j] == -1){
                    if (checkout(i-1, j-1)){
                        _map[i-1][j-1] ++;
                    }
                    if (checkout(i-1, j)){
                        _map[i-1][j] ++;
                    }
                    if (checkout(i-1, j+1)){
                        _map[i-1][j+1] ++;
                    }
                    if (checkout(i, j-1)){
                        _map[i][j-1] ++;
                    }
                    if (checkout(i, j+1)){
                        _map[i][j+1] ++;
                    }
                    if (checkout(i+1, j-1)){
                        _map[i+1][j-1] ++;
                    }
                    if (checkout(i+1, j)){
                        _map[i+1][j] ++;
                    }
                    if (checkout(i+1, j+1)){
                        _map[i+1][j+1] ++;
                    }
                }
            }
        }
    }

    /**
     * Returns true when the input grid is within the field && is not a mine. False otherwise
     */
    private boolean checkout(int row, int col){
        if ((row >= 0) && (row < _row) && (col >= 0) && (col < _col)){
            return _map[row][col] != -1;
        }
        return false;
    }

    /*
        Getters
     */
    public int getNum(int col, int row){
        return _map[row][col];
    }

    public int getRow(){
        return _row;
    }

    public int getCol(){
        return _col;
    }

    public int getMineNum(){
        return _mineNum;
    }
}
