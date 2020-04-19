package com.example.randomizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataHelper extends SQLiteOpenHelper {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
    public static final String DATABASE_NAME = "Prescriptions.db";
    public static final String TABLE_NAME = "PRESCRIPTION_DETAILS";
    public static final String COL_1 = "DRUG_ID";//

    public static final String COL_2 = "NAME";//
    public static final String COL_3 = "RSX";//
    public static final String COL_4 = "DOSE";
    public static final String COL_5 = "QUANTITY";//
    public static final String COL_6 = "REFILLS";//
    public static final String COL_7 = "DATE";
    public static final String COL_8 = "TAKEN";
    public static final String COL_9 = "INFO";//

    //creating the database
    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    //------------------------------------------------------------------------------

    //DB:
    //    ID | NAME | RSX | DOSE | QUANTITY | REFILLS | DATE | TAKEN | INFO
    //TV:

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (DRUG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
//                " NAME TEXT, QUANTITY TEXT, REFILLS TEXT, RSX TEXT, INFO TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (DRUG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, RSX TEXT, DOSE TEXT, QUANTITY TEXT, REFILLS TEXT," +
                " DATE TEXT, TAKEN TEXT, INFO TEXT)");
    }
    //------------------------------------------------------------------------------
    //validate DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    //----------------------------------------------------------------------
    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//    public boolean insertData(String name, String quantity,
//                              String refills, String rsx, String info){
    public boolean insertData(String name, String rsx, String dose, String qty,
                              String refills, String date, String t, String info){
        SQLiteDatabase db = this.getWritableDatabase();//for checking


        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_2, name);
        contentVal.put(COL_3, rsx);
        contentVal.put(COL_4, dose);
        contentVal.put(COL_5, qty);
        contentVal.put(COL_6, refills);
        contentVal.put(COL_7, date);
        contentVal.put(COL_8, t);
        contentVal.put(COL_9, info);

        long result = db.insert(TABLE_NAME, null, contentVal);

        if(result == -1)
            return false;
        else
            return true;
    }
    //----------------------------------------------------------------------
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);

        return result;
    }

    //----------------------------------------------------------------------

    //NOT PERFECT
    public boolean updateData(String name, String quantity, String refills, String rsx){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_2, name);
        contentVal.put(COL_3, quantity);
        contentVal.put(COL_4, refills);
        contentVal.put(COL_5, rsx);
//        contentVal.put(COL_6, info);

        db.update(TABLE_NAME, contentVal, "RSX = "+rsx, null);

        return true;
    }
    //----------------------------------------------------------------------
}
