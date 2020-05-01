package com.example.randomizer.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.CursorAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MedicationDataHelper extends SQLiteOpenHelper {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
    private static final String DATABASE_NAME_2 = "medAppData.db";

    private static final String DATABASE_NAME = "Prescriptions.db";
    private static final String TABLE_NAME = "PRESCRIPTION_DETAILS";
    public static final String COL_1 = "DRUG_ID";//

    private static final String COL_2 = "NAME";//
    private static final String COL_3 = "RSX";//
    private static final String COL_4 = "DOSE";
    private static final String COL_5 = "QUANTITY";//
    private static final String COL_6 = "REFILLS";//
    private static final String COL_7 = "DATE";
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
        //TODO: QUANTITY TEXT, change METHODS to INT
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (DRUG_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT, RSX TEXT, DOSE TEXT, QUANTITY INT, REFILLS TEXT," +
                " DATE TEXT, TAKEN TEXT, INFO TEXT, CODES TEXT)");


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
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9 | CODES

    public boolean insertData(String name, String rsx, String dose, int qty,
                              String refills, String date, String t, String info, String code){
        SQLiteDatabase db = this.getWritableDatabase();//for checking


        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_2, name);
        contentVal.put(COL_3, rsx);
        contentVal.put(COL_4, dose);
        contentVal.put(COL_5, qty);//TYPE INT
        contentVal.put(COL_6, refills);
        contentVal.put(COL_7, date);
        contentVal.put(COL_8, t);
        contentVal.put(COL_9, info);
        contentVal.put(COL_10, code);

        long result = db.insert(TABLE_NAME, null, contentVal);

        db.close();
        if(result == -1)
            return false;
        else
            return true;
    }
    //----------------------------------------------------------------------
    private Cursor getCertainMed(String id, String name, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        int i = Integer.parseInt(id);

        return db.rawQuery("SELECT * FROM PRESCRIPTION_DETAILS " +
                "WHERE NAME = '" + name +"' AND DRUG_ID = " + i
                + " AND DATE = '" + time+"'",null);
    }
    //----------------------------------------------------------------------
    public Cursor getAllData(){

        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("select * from " + TABLE_NAME, null);
    }

    //----------------------------------------------------------------------
    public int updateData(String ID, String NAME, String TIME, String confirmation, int qty){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentVal = new ContentValues();
        contentVal.put(COL_8, confirmation);
        int result;

        int newQTY = qty - 1;
        if(confirmation.equals("Y") ) {
            contentVal.put(COL_5, newQTY);//decremented value
            result = db.update(TABLE_NAME, contentVal, COL_1 + " = ? AND " + COL_2 + " = ? AND "
                    + COL_7 + " = ?", new String[]{ID, NAME, TIME});
        }
        else {//confirmation == N,
            result = db.update(TABLE_NAME, contentVal, COL_1 + " = ? AND " + COL_2 + " = ? AND "
                    + COL_7 + " = ?", new String[]{ID, NAME, TIME});
        }

        db.close();
        //only ONE should be updated
        return result;

    }
    //----------------------------------------------------------------------
}
