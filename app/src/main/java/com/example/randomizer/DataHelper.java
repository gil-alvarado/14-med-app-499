package com.example.randomizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Prescriptions.db";
    public static final String TABLE_NAME = "PRESCRIPTION_DETAILS";
    public static final String COL_1 = "DRUG_ID";//
    public static final String COL_2 = "NAME";//TEXT
    public static final String COL_3 = "QUANTITY";//
    public static final String COL_4 = "REFILLS";//
    public static final String COL_5 = "RSX";//
    public static final String COL_6 = "INFO";//

    public static final String DOSE = "DOSE";//STRING
    public static final String DATE = "DATE";//DATETIME or NUMERIC

    //creating the database
    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    //------------------------------------------------------------------------------

    //    ID | NAME | QUANTITY | REFILLS | INFO
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (DRUG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, QUANTITY TEXT, REFILLS TEXT, RSX TEXT, INFO TEXT)");
    }
    //------------------------------------------------------------------------------
    //validate DB
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    //----------------------------------------------------------------------
    public boolean insertData(String name, String quantity,
                              String refills, String rsx, String info){

        SQLiteDatabase db = this.getWritableDatabase();//for checking

        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_2, name);
        contentVal.put(COL_3, quantity);
        contentVal.put(COL_4, refills);
        contentVal.put(COL_5, rsx);
        contentVal.put(COL_6, info);
        long result = db.insert(TABLE_NAME, null, contentVal);

        if(result == -1)
            return false;
        else
            return true;
    }
    //----------------------------------------------------------------------
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select *  from " + TABLE_NAME, null);

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
