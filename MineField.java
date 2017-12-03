package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MineField {
    private static int DEFAULT_ROW = 16;
    private static int DEFAULT_COL = 30;
    private static int DEFAULT_MINES = 99;
    private static int SAFEZONE = 1;

    private int _row;
    private int _col;
    private int _mineNum;

    private int[][] _map;
    private boolean[][] _clearArea;
    private boolean[][] _marked;

    /**
     * Generate a mine field using default data
     */
    public MineField(){
        _row = DEFAULT_ROW;
        _col = DEFAULT_COL;
        _mineNum = DEFAULT_MINES;
        generateField();
    }

    /**
     * Allow custom mine fields
     */
    public MineField(int row, int col, int mineNum)throws MineNumExceedException{
        _row = row;
        _col = col;
        _mineNum = mineNum;
        if(mineNum > _row *_col){
            throw new MineNumExceedException();
        }else {
            generateField();
        }
    }

    private void generateField(){
        int realRow = _row + 2 * SAFEZONE;
        int realCol = _col + 2 * SAFEZONE;
        _map = new int[realRow][realCol];
        _clearArea = new boolean[realRow][realCol];
        _marked = new boolean[realRow][realCol];
        for (int i = 0; i < realRow; i++){
            Arrays.fill(_map[i], 0);
            Arrays.fill(_clearArea[i], false);
            Arrays.fill(_marked[i], false);
        }
        plantMines();


        //Print out the field
        for (int i = 0; i < _map.length; i++) {
            for (int col = 0; col < _map[0].length; col++) {
                if (_map[i][col] == -1){
                    System.out.print("*" + "\t");
                }else{
                    System.out.print(_map[i][col] + "\t");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Randomly set mines in the map
     */
    private void plantMines(){
        ArrayList<Integer> field = new ArrayList<>();
        for (int i = 0; i < _row * _col; i++){
            field.add(i);
        }
        Collections.shuffle(field);
        List<Integer> mineList = field.subList(0, _mineNum);
        for (int m : mineList){
            int row = m / _col + SAFEZONE;
            int col = m % _col  + SAFEZONE;
            _map[row][col] = -1;
            calcMap(row, col);
        }
    }

    /**
     * Generate the number on every grid
     */
    private void calcMap(int row, int col){
        if (checkout(row-1, col-1)){ _map[row-1][col-1] ++; }
        if (checkout(row-1, col)){ _map[row-1][col] ++; }
        if (checkout(row-1, col+1)){ _map[row-1][col+1] ++; }
        if (checkout(row, col-1)){ _map[row][col-1] ++; }
        if (checkout(row, col+1)){ _map[row][col+1] ++; }
        if (checkout(row+1, col-1)){ _map[row+1][col-1] ++; }
        if (checkout(row+1, col)){ _map[row+1][col] ++; }
        if (checkout(row+1, col+1)){ _map[row+1][col+1] ++; }
    }

    /**
     * Returns true when the input grid is within the field && is not a mine. False otherwise
     */
    private boolean checkout(int row, int col){
        if ((row > 0) && (row < _map.length - 1) && (col > 0) && (col < _map[0].length - 1)){
            return _map[row][col] != -1;
        }
        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Return a list of points that would be revealed by clicking on this rectangle
     */
    public ArrayList<int[]> ripple(int row, int col) {
        int fullRow = row + SAFEZONE;
        int fullCol = col + SAFEZONE;
        if ( !checkout(fullRow, fullCol) || isClicked(row, col) || isMarked(row, col)) {
            return new ArrayList<>();
        }else{
            ArrayList<int[]> rippleList = new ArrayList<>();
            int[] source = {row, col};
            rippleList.add(source);
            _clearArea[fullRow][fullCol] = true;
            if(_map[fullRow][fullCol] == 0){
                rippleList.addAll(ripple(row-1,col-1));
                rippleList.addAll(ripple(row-1,col));
                rippleList.addAll(ripple(row-1,col+1));
                rippleList.addAll(ripple(row,col-1));
                rippleList.addAll(ripple(row,col+1));
                rippleList.addAll(ripple(row+1,col-1));
                rippleList.addAll(ripple(row+1,col));
                rippleList.addAll(ripple(row+1,col+1));
            }
            return rippleList;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<int[]> sweep(int row, int col){
        ArrayList<int[]> sweeplist = new ArrayList<>();
        if (isDecided(row, col)){
            addToSweepList(row-1, col-1, sweeplist);
            addToSweepList(row-1, col, sweeplist);
            addToSweepList(row-1, col+1, sweeplist);
            addToSweepList(row, col-1, sweeplist);
            addToSweepList(row, col+1, sweeplist);
            addToSweepList(row+1, col-1, sweeplist);
            addToSweepList(row+1, col, sweeplist);
            addToSweepList(row+1, col+1, sweeplist);
        }
        return sweeplist;
    }

    private void addToSweepList(int row, int col, ArrayList<int[]> sweeplist){
        if (!isMarked(row,col) && !isClicked(row, col)){
            int[] temp = {row, col};
            sweeplist.add(temp);
            if (getNum(row, col) == 0){
                sweeplist.addAll(ripple(row, col));
            }
        }
    }

    private boolean isDecided(int row, int col){
        int i = 0;
        if (isMarked(row-1,col-1)){ i++; }
        if (isMarked(row-1,col)){ i++; }
        if (isMarked(row-1,col+1)){ i++; }
        if (isMarked(row,col+1)){ i++; }
        if (isMarked(row,col-1)){ i++; }
        if (isMarked(row+1,col-1)){ i++; }
        if (isMarked(row+1,col)){ i++; }
        if (isMarked(row+1,col+1)){ i++; }
        return i == getNum(row, col);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean hasWon(){
        boolean flag = true;
        for (int row = SAFEZONE; row < _row - SAFEZONE; row++) {
            for (int col = SAFEZONE; col < _col - SAFEZONE; col++) {
                if ((_map[row][col] != -1) && _clearArea[row][col]) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
        Setters
     */
    public void mark(int row, int col){
        int fullRow = row + SAFEZONE;
        int fullCol = col + SAFEZONE;
        if(_marked[fullRow][fullCol]){
            _marked[fullRow][fullCol] = false;
        }else{
            _marked[fullRow][fullCol] = true;
        }
    }


    /*
        Getters
     */
    public int getNum(int row, int col){
        return _map[row + SAFEZONE][col + SAFEZONE];
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


    public boolean isMine(int row, int col){ return _map[row + SAFEZONE][col + SAFEZONE] == -1; }

    public boolean isClicked(int row, int col){ return _clearArea[row + SAFEZONE][col + SAFEZONE]; }

    public boolean isMarked(int row, int col){ return _marked[row + SAFEZONE][col + SAFEZONE]; }


}
