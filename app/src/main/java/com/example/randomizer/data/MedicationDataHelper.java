package com.example.randomizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MedicationDataHelper extends SQLiteOpenHelper {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9 | CODES
//    private static final String DATABASE_NAME_2 = "medAppData.db";

    private static String[] headers =
            new  String[]{"DRUG_ID","NAME","RSX","DOSE","QUANTITY","REFILLS","DATE","TIME","TAKEN","INFO","CODES"};

    //
    private static final String DATABASE_NAME = "Prescriptions.db";

    private static final String TABLE_NAME = "PRESCRIPTION_DETAILS";
    private static final String COL_1 = "DRUG_ID";//
    private static final String COL_2 = "NAME";//
    private static final String COL_3 = "RSX";//
    private static final String COL_4 = "DOSE";
    private static final String COL_5 = "QUANTITY";//
    private static final String COL_6 = "REFILLS";//

    private static final String COL_7 = "DATE";
    private static final String COL_7_1 = "TIME";


    private static final String COL_8 = "TAKEN";
    private static final String COL_9 = "INFO";//
    private static final String COL_10 = "CODES";


    //creating the database
    public MedicationDataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    //------------------------------------------------------------------------------

    //DB:
    //    ID | NAME | RSX | DOSE | QUANTITY | REFILLS | DATE | TAKEN | INFO | CODES
    //TV:

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        COL_7 = "DATE";
//  COL_7_1 = "TIME";
        //TODO: QUANTITY TEXT, change METHODS to INT
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (DRUG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, RSX TEXT, DOSE TEXT, QUANTITY INT, REFILLS TEXT," +
                " DATE TEXT, TIME TEXT, TAKEN TEXT, INFO TEXT, CODES TEXT)");


    }
    //------------------    ------------------------------------------------------------
    //validate DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    //----------------------------------------------------------------------
    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE --- TIME | 7| TAKEN 8| INFO 9 | CODES

    public boolean insertData(String name, String rsx, String dose, int qty,
                              String refills, String date, String time, String t, String info, String code,Context context){
        SQLiteDatabase db = this.getWritableDatabase();//for checking


        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_2, name);
        contentVal.put(COL_3, rsx);
        contentVal.put(COL_4, dose);
        contentVal.put(COL_5, qty);//TYPE INT
        contentVal.put(COL_6, refills);
        contentVal.put(COL_7, date);
        contentVal.put(COL_7_1, time);
        contentVal.put(COL_8, t);
        contentVal.put(COL_9, info);
        contentVal.put(COL_10, code);

        long result = db.insert(TABLE_NAME, null, contentVal);

        // case 1: create files, include headers
        fileHandler(name, context, getAllData().getCount(), 1);
        db.close();
        if(result == -1)
            return false;
        else
            return true;
    }

//private void fileHandler(String name,Context context){
    private void fileHandler(String name, Context context, int id, int action){
        StringBuilder data = new StringBuilder();

        Cursor cursor;

        cursor = (getAllData().getCount() == id)
                ? this.getMed(getAllData().getCount()) : this.getMed(id);
        if (cursor.moveToFirst()) {
            do {
                //data
                data.append("\n"+cursor.getString(cursor.getColumnIndex("DRUG_ID"))
                        + "," + cursor.getString(cursor.getColumnIndex("NAME"))
                        + "," +cursor.getString(cursor.getColumnIndex("RSX"))
                        + "," + cursor.getString(cursor.getColumnIndex("DOSE"))
                        +"," + cursor.getString(cursor.getColumnIndex("QUANTITY"))
                        + "," + cursor.getString(cursor.getColumnIndex("REFILLS"))
                        + "," + cursor.getString(cursor.getColumnIndex("DATE"))
                        + "," + cursor.getString(cursor.getColumnIndex("TIME"))
                        + "," + cursor.getString(cursor.getColumnIndex("TAKEN"))
                        + "," + cursor.getString(cursor.getColumnIndex("INFO"))
                        + "," + cursor.getString(cursor.getColumnIndex("CODES")) );

            } while (cursor.moveToNext());
        }
        try{

            if (action == 1) {
                prepFile(name, context);
            }

            FileOutputStream out = context.openFileOutput
                    (name+".csv", Context.MODE_PRIVATE | Context.MODE_APPEND );
            out.write((data.toString()).getBytes());
            out.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
//      CREATE NEW FILE, ADD HEADERS
        private void prepFile(String name, Context context) {
        StringBuilder data = new StringBuilder();
        //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
        //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
        for(String h : headers)
            data.append(h).append(",");
        try {
            FileOutputStream file = context.openFileOutput(name + ".csv", Context.MODE_PRIVATE );
            file.write(data.toString().getBytes());
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //----------------------------------------------------------------------

    public Cursor getMed(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM PRESCRIPTION_DETAILS " +
                "WHERE DRUG_ID = " + id ,null);
    }
    //----------------------------------------------------------------------
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    //----------------------------------------------------------------------
    //              UPDATE med.csv + DATABASE!
    //----------------------------------------------------------------------
    public int updateData(String ID, String NAME, String TIME, String DATE, String confirmation, int qty, Context context, String comment){
        //    ID 0 | NAME 1| RSX 2| DOSE 3| QUANTITY 4|
        //    REFILLS 5| DATE 6-------TIME7 | TAKEN 8| INFO 9 | CODES 10
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();
        contentVal.put("TAKEN", confirmation);//either yes or no
//        contentVal.put("DATE",DATE);
        contentVal.put("INFO", comment);
        int result;

        int newQTY = qty - 1;
        if(confirmation.equals("Y") ) {
            contentVal.put("QUANTITY", newQTY);//decremented value
            result = db.update(TABLE_NAME, contentVal, COL_1 + " = ? AND " + COL_2 + " = ? AND "
                    + "TIME = ?", new String[]{ID, NAME, TIME});
        }
        else if(confirmation.equals("N") ) {//confirmation == N,
            result = db.update(TABLE_NAME, contentVal, COL_1 + " = ? AND " + COL_2 + " = ? AND "
                    + "TIME = ?", new String[]{ID, NAME, TIME});
        }
        else
            result = 0;

        db.close();

        updateFile(NAME, DATE, context, Integer.parseInt(ID));

        return result;
    }

    private void updateFile(String name, String DATE, Context context, int id) {
        StringBuilder data = new StringBuilder();

        Cursor cursor;
//      get updated data info, then update file
        cursor = (getAllData().getCount() == id)
                ? this.getMed(getAllData().getCount()) : this.getMed(id);
        if (cursor.moveToFirst()) {
            do {
                //data
                data.append("\n"+cursor.getString(cursor.getColumnIndex("DRUG_ID"))
                        + "," + cursor.getString(cursor.getColumnIndex("NAME"))
                        + "," +cursor.getString(cursor.getColumnIndex("RSX"))
                        + "," + cursor.getString(cursor.getColumnIndex("DOSE"))
                        +"," + cursor.getString(cursor.getColumnIndex("QUANTITY"))
                        + "," + cursor.getString(cursor.getColumnIndex("REFILLS"))
                        + "," + DATE
                        + "," + cursor.getString(cursor.getColumnIndex("TIME"))
                        + "," + cursor.getString(cursor.getColumnIndex("TAKEN"))
                        + "," + cursor.getString(cursor.getColumnIndex("INFO"))
                        + "," + cursor.getString(cursor.getColumnIndex("CODES")) );

            } while (cursor.moveToNext());
            cursor.close();
        }
        try{
            FileOutputStream out = context.openFileOutput
                    (name+".csv", Context.MODE_PRIVATE | Context.MODE_APPEND );
            out.write((data.toString()).getBytes());
            out.close();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------
}
