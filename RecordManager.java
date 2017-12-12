package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class RecordManager {
    private static String PATH = ".record.txt";
    private static char SPLITER= '#';
    private static String DEFAULT_LINE = "no record";
    private static File _record;

    private String eaz_keeper;
    private int eaz_rec;
    private String med_keeper;
    private int med_rec;
    private String exp_keeper;
    private int exp_rec;

    public RecordManager(){
        _record = new File(PATH);
        if (!_record.exists()){
            createNewRecord();
        }else{
            readRecord();
        }
    }

    private void createNewRecord(){
        try {
            _record.createNewFile();
            setDefaultValue();
            PrintWriter writer = new PrintWriter(_record);
            writer.println(DEFAULT_LINE);
            writer.close();
        } catch (IOException e) { }
    }

    private void setDefaultValue(){
        eaz_keeper = "";
        med_keeper = "";
        exp_keeper = "";
        eaz_rec = -1;
        med_rec = -1;
        exp_rec = -1;
    }

    private void readRecord(){
        String record = "";
        try {
            Scanner sc = new Scanner(_record);
            record = sc.nextLine();
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (record.equals(DEFAULT_LINE)){
            setDefaultValue();
        }else{
            String[] records = record.split("#");
            eaz_keeper = records[0];
            med_keeper = records[0];
            exp_keeper = records[0];
            eaz_rec = Integer.parseInt(records[0]);
            med_rec = Integer.parseInt(records[0]);
            exp_rec = Integer.parseInt(records[0]);
        }
    }

    public void writeRecord(int level, String name, int record){
        String update = "";
        switch (level) {
            case 0:
                update = name + SPLITER + record + SPLITER + med_keeper + SPLITER + med_rec + SPLITER + exp_keeper + SPLITER + exp_rec;
                break;
            case 1:
                update = eaz_keeper + SPLITER + eaz_rec + SPLITER + name + SPLITER + record + SPLITER + exp_keeper + SPLITER + exp_rec;
                break;
            case 2:
                update = eaz_keeper + SPLITER + eaz_rec + SPLITER + med_keeper + SPLITER + med_rec + SPLITER + name + SPLITER + record ;
                break;
        }
        try {
            PrintWriter writer = new PrintWriter(PATH);
            writer.println(update);
            writer.close();
        } catch (FileNotFoundException e) { }
    }

    /*
        Getters
     */

    public String getEazKeeper() {
        return eaz_keeper;
    }

    public int getEazRec() {
        return eaz_rec;
    }

    public String getMedKeeper() {
        return med_keeper;
    }

    public int getMedRec() {
        return med_rec;
    }

    public String getExpKeeper() {
        return exp_keeper;
    }

    public int getExpRec() {
        return exp_rec;
    }
}
