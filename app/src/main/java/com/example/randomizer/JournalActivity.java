package com.example.randomizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//DATA LIST

public class JournalActivity extends AppCompatActivity {

    private static final String FILE_NAME = "example.txt";
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


    DataHelper dBHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;

    ListView data_ListView;
    Button shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        dBHelper = new DataHelper(this);//load database
//        shareButton = (Button)findViewById(R.id.shareButton);
        data_ListView = (ListView) findViewById(R.id.data_listView);

//        shareButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(JournalActivity.this, "WORK IN PROGRESS!", Toast.LENGTH_LONG).show();
//            }
//        });
    }
    //---------------------------------------------

//------------------------------------------------------------------------
    //button "onclick"
    public void save() throws IOException {
        //get Data
        cursor = dBHelper.getAllData();
//        String text = mEditText.getText().toString();
        FileOutputStream fos = null;


        NAME.clear();
        QTY.clear();
        REFILLS.clear();
        RSX.clear();


            cursor.moveToFirst();
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
//            fos.write(text.getBytes());
            fos.write(Integer.parseInt(cursor.getString(cursor.getColumnIndex("NAME"))));
//            fos.write(Integer.parseInt(cursor.getString(cursor.getColumnIndex("QUANTITY"))));
//            fos.write(Integer.parseInt(cursor.getString(cursor.getColumnIndex("REFILLS"))));
//            fos.write(Integer.parseInt(cursor.getString(cursor.getColumnIndex("RSX"))));


//            mEditText.getText().clear();


            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();

    }
    //-------------------------------------------------------
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

            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.randomizer", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
    //---------------------------------------------------------
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }

    private void displayData() {
//        sqLiteDatabase = dBHelper.getReadableDatabase();
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


//        ListAdapter adapter = new ListAdapter(JournalActivity.this,NAME, QTY,REFILLS,RSX);
        ListAdapter adapter = new ListAdapter(JournalActivity.this,NAME,RSX,DOSE,QTY,REFILLS,DATE,TAKEN,INFO);

        data_ListView.setAdapter(adapter);

        cursor.close();
    }
}
