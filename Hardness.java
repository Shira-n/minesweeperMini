package sample;

public enum Hardness {
    EASY(8, 8, 10),
    INTERMEDIATE(16,16,40),
    EXPERT(16,32,99),
    CUSTOM(0,0,0);

    private int _row;
    private int _col;
    private int _mine;

    private static Hardness _hardness;

    Hardness(int row, int col, int mine) {
        _row = row;
        _col = col;
        _mine = mine;
    }

    public static void setHardness(Hardness hardness) {
        _hardness = hardness;
    }


    public static void setCustom(int row, int col, int mine) {
        CUSTOM._row = row;
        CUSTOM._col = col;
        CUSTOM._mine = mine;
    }

    public static int getRow() {
        return _hardness._row;
    }

    public static int getCol() {
        return _hardness._col;
    }

    public static int getMine() {
        return _hardness._mine;
    }

    public static Hardness getHardness() {return _hardness; }

    public static void renewHardness(){
        if (_hardness == null){
            _hardness = Hardness.EASY;
        }
    }
}
