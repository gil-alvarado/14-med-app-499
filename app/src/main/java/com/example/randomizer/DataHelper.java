package com.example.randomizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataHelper extends SQLiteOpenHelper {

    public static final String DATABASE_PRESCRIPTION = "prescriptions.db";
    public static final String PRESCRIPTION_TABLE_NAME = "PRESCRIPTION_DETAILS";
    public static final String PRESCRIPTION_DRUG_NAME = "NAME";
    public static final String PRESCRIPTION_DRUG_QUANTITY = "QUANTITY";
    public static final String PRESCRIPTION_DRUG_REFILLS = "REFILLS";
    public static final String PRESCRIPTION_DOSE = "DOSE";
    public static final String PRESCRIPTION_DATE_INTAKE = "DATE";//time/date

    public static final String DATABASE_INVENTORY = "inventory.db";//
    public static final String INVENTORY_TABLE_NAME = "INVENTORY";
    public static final String INVENTORY_DRUG_NAME = "NAME";

    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_PRESCRIPTION, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
