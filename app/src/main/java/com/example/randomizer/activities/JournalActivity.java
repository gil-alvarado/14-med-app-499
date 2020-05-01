package com.example.randomizer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.randomizer.adapters.JournalListAdapter;
import com.example.randomizer.R;
import com.example.randomizer.data.MedicationDataHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

//TODO: decrement qty in DB, change QTY datatype to int

public class JournalActivity extends AppCompatActivity {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//-----------------------------------------------------
    private ArrayList<String> NAME = new ArrayList<String>();
    private ArrayList<String> RSX = new ArrayList<String>();
    private ArrayList<String> DOSE = new ArrayList<String>();
    private ArrayList<String> QTY = new ArrayList<String>();
    private ArrayList<String> REFILLS = new ArrayList<String>();
    private ArrayList<String> DATE = new ArrayList<String>();
    private ArrayList<String> TAKEN = new ArrayList<String>();
    private ArrayList<String> INFO = new ArrayList<String>();


    private MedicationDataHelper dBHelper;
    private Cursor cursor;

    private ListView data_ListView;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        dBHelper = new MedicationDataHelper(this);//load database
        count = dBHelper.getAllData().getCount();
        data_ListView = (ListView) findViewById(R.id.data_listView);

    }
//    ------------------------------------------------------------------------
//    ------------------------------------------------------------------------
    public void export(View view){
        //generate data
        StringBuilder data = new StringBuilder();
        //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
        //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
        data.append("NAME, RSX, DOSE, QUANTITY,REFILLS,DATE,TAKEN,INFO");

        cursor = dBHelper.getAllData();

        if (cursor.moveToFirst()) {
            do {
                //data
                data.append("\n"+cursor.getString(cursor.getColumnIndex("NAME"))
                + "," +cursor.getString(cursor.getColumnIndex("RSX"))
                + "," + cursor.getString(cursor.getColumnIndex("DOSE"))
                +"," + cursor.getString(cursor.getColumnIndex("QUANTITY"))
                + "," + cursor.getString(cursor.getColumnIndex("REFILLS"))
                + "," + cursor.getString(cursor.getColumnIndex("DATE"))
                        + "," + cursor.getString(cursor.getColumnIndex("TAKEN"))
                        + "," + cursor.getString(cursor.getColumnIndex("INFO")));

            } while (cursor.moveToNext());
        }

        try{
            //saving the file into device
            //provide file name

            FileOutputStream out = openFileOutput("medication-data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting/set permissions
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), "medication-data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.randomizer", fileLocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Medication Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
        cursor.close();

    }
    //---------------------------------------------------------
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }
//  ---------------------------------------------------------------------------------------
    private void displayData() {
        cursor = dBHelper.getAllData();//sqLiteDatabase.rawQuery("SELECT * FROM  PRESCRIPTION_DETAILS",null);

        NAME.clear();
        QTY.clear();
        REFILLS.clear();
        RSX.clear();

        if (cursor.moveToFirst()) {
            do {
                //database
                //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
                //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
                NAME.add(cursor.getString(cursor.getColumnIndex("NAME")));
                RSX.add(cursor.getString(cursor.getColumnIndex("RSX")));
                DOSE.add(cursor.getString(cursor.getColumnIndex("DOSE")));
                QTY.add(cursor.getString(cursor.getColumnIndex("QUANTITY")));
                REFILLS.add(cursor.getString(cursor.getColumnIndex("REFILLS")));
                DATE.add(cursor.getString(cursor.getColumnIndex("DATE")));
                TAKEN.add(cursor.getString(cursor.getColumnIndex("TAKEN")));
                INFO.add(cursor.getString(cursor.getColumnIndex("INFO")));

            } while (cursor.moveToNext());
        }


        JournalListAdapter adapter = new JournalListAdapter(JournalActivity.this,NAME,RSX,DOSE,QTY,REFILLS,DATE,TAKEN,INFO);

        data_ListView.setAdapter(adapter);

        cursor.close();
    }

    public void goHomeJournal(View view) {


        Toast.makeText(this,"# data entries: " + count,Toast.LENGTH_LONG).show();
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
}
