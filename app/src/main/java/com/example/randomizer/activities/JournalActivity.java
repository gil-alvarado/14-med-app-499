package com.example.randomizer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.randomizer.R;
import com.example.randomizer.adapters.ItemArrayListAdapter;
import com.example.randomizer.data.MedicationDataHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

//TODO: decrement qty in DB, change QTY datatype to int

public class JournalActivity extends AppCompatActivity {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//-----------------------------------------------------
    private ArrayList<String> NAME = null;
    private ArrayList<String> DOSE = null;//name
    private ArrayList<String> QTY = null;//qty
    private ArrayList<String> REFILLS =null;//refills
    private ArrayList<String> INFO = null;//rsx
    private ArrayList<String> RSX = null;//rsx
    private ArrayList<String> DATE = null;//rsx
    private ArrayList<String> TIME = null;//rsx
    private ArrayList<String> TAKEN = null;//rsx

    private LinkedHashMap<String, ArrayList<String>> map;

    private ListView data_ListView;

    private Spinner spinner;
    String selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_listed);
        data_ListView = (ListView) findViewById(R.id.journalListView_);

        spinner =findViewById(R.id.medicationSpinner);


//        GET FILE NAMES: access local directory
        String []fileNames = getApplicationContext().fileList();
        List<String> list = new ArrayList<String>();

        for(int i = 0; i < fileNames.length; i++){
            Log.d("Filename: " , fileNames[i]);
            list.add(fileNames[i].substring(0,fileNames[i].lastIndexOf('.')));

        }

        //spinner looking for array adapter

        ArrayAdapter<String> fileNameAdapter =
                new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,list);

        spinner.setAdapter(fileNameAdapter);

        MedicationDataHelper DB = new MedicationDataHelper(this);

        //dialog activates/pops IF no data has been collected
        if(DB.getAllData().getCount() == 0){
            final TextView cusTitle = new TextView(this);
            cusTitle.setText("ALERT!");
            cusTitle.setBackgroundColor(Color.parseColor("#d31141"));
            cusTitle.setPadding(10, 20, 10, 20);
            cusTitle.setGravity(Gravity.CENTER);
            cusTitle.setTextColor(Color.WHITE);
            cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

            final AlertDialog.Builder builder = new AlertDialog.Builder(JournalActivity.this);
            builder.setCancelable(false);
//            builder.setTitle("Alert!");
            builder.setCustomTitle(cusTitle);
            builder.setMessage("\nNo medication has been entered yet!\nWould you like to add one?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(JournalActivity.this, enterDataActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(JournalActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            //show instructions to operate journal
            SharedPreferences prefs = getSharedPreferences("journalIns", MODE_PRIVATE);
            boolean firstStart = prefs.getBoolean("firstStart", true);

            if (firstStart) {
                showStartDialog();
            }
        }

        //------------------------------------------------------------------------------
        //load data onto screen by calling methods
        //------------------------------------------------------------------------------
        Button load = findViewById(R.id.journalHomeButton);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadContent();
                displayData();
            }
        });
    }
    //------------------------------------------------------------------------------
    //          LOAD DATA and DISPLAY via CSV files
    //------------------------------------------------------------------------------

    public void loadContent() {
        selectedFile = String.valueOf(spinner.getSelectedItem());
        openFile(selectedFile);
    }
    private void openFile(String selectedFile) {

        ArrayList<List<String>> medicationData = new ArrayList<>();
        ArrayList<String> header = null;
        String value = "";
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = openFileInput(selectedFile+".csv");
            InputStreamReader reader = new InputStreamReader((fileInputStream));
            BufferedReader csvReader = new BufferedReader(reader);

            String row = "";
            String[] line = csvReader.readLine().split(",");//read first line(omit)

            header = new ArrayList<>(Arrays.asList(line));

            //reading entire file
            while ((row = csvReader.readLine()) != null) {
                //                row =row.toUpperCase();
                String[] data = row.split(",", -1);//line/row/med details
                medicationData.add(new ArrayList<>(Arrays.asList(data)));//rows of med data
            }
            fileInputStream.close();
            //POPULATE DATA
            map = new LinkedHashMap<>();
            assert header != null;
            for (int col = 0; col < header.size(); col++) {

                ArrayList<String> obj = new ArrayList<>();
                for (int r = 0; r < medicationData.size(); r++) {
                    obj.add(medicationData.get(r).get(col));
                }
                map.put(header.get(col), obj);
            }

            NAME = new ArrayList<>(map.get("NAME"));
            RSX = new ArrayList<>(map.get("RSX"));
            DOSE = new ArrayList<>(map.get("DOSE"));
            QTY = new ArrayList<>(map.get("QUANTITY"));
            REFILLS = new ArrayList<>(map.get("REFILLS"));
            INFO = new ArrayList<>(map.get("INFO"));
            DATE = new ArrayList<>(map.get("DATE"));
            TIME = new ArrayList<>(map.get("TIME"));
            TAKEN = new ArrayList<>(map.get("TAKEN"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//    ------------------------------------------------------------------------
//    set permissions in manifest files  AND  this method
//    ------------------------------------------------------------------------
    public void export(View view){
        //generate data

//        StringBuilder data = new StringBuilder();
//        //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
//        //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//        data.append("NAME, RSX, DOSE, QUANTITY,REFILLS,DATE,TAKEN,INFO");
//        MedicationDataHelper dBHelper = new MedicationDataHelper(this);
//        Cursor cursor = dBHelper.getAllData();
//
//        if (cursor.moveToFirst()) {
//            do {
//                //data
//                data.append("\n"+ cursor.getString(cursor.getColumnIndex("NAME"))
//                        + "," + cursor.getString(cursor.getColumnIndex("RSX"))
//                        + "," + cursor.getString(cursor.getColumnIndex("DOSE"))
//                        +"," + cursor.getString(cursor.getColumnIndex("QUANTITY"))
//                        + "," + cursor.getString(cursor.getColumnIndex("REFILLS"))
//                        + "," + cursor.getString(cursor.getColumnIndex("DATE"))
//                        + "," + cursor.getString(cursor.getColumnIndex("TAKEN"))
//                        + "," + cursor.getString(cursor.getColumnIndex("INFO")));
//
//            } while (cursor.moveToNext());
//        }

        try{
            Context context = getApplicationContext();
            File fileLocation = new File(getFilesDir(), selectedFile+".csv");//"medication-data.csv"

            Uri path = FileProvider.getUriForFile(context, "com.example.randomizer.activities.JournalActivity", fileLocation);

            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, selectedFile + " Medication Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            context.startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
//        cursor.close();

    }
    //------------------------------------------------------------------------------
    //------------------------------------------------------------------------------
//    @Override
//    protected void onResume() {
//        displayData();
//        super.onResume();
//    }
    //------------------------------------------------------------------------------
    //adapter to display list items
    //------------------------------------------------------------------------------
    private void displayData() {

        ItemArrayListAdapter adapter = new ItemArrayListAdapter(JournalActivity.this, NAME,RSX,DOSE,QTY,REFILLS,INFO,DATE,TIME, TAKEN);
        data_ListView.setAdapter(adapter);

    }

    public void goHomeJournal(View view) {


//        Toast.makeText(this,"# data entries: " + count,Toast.LENGTH_LONG).show();
        Intent intent = new Intent (this, MainActivity.class);
        startActivity(intent);
    }
    //------------------------------------------------------------------------------
    //------------------------------------------------------------------------------
    private void showStartDialog() {
        TextView cusTitle = new TextView(this);
        cusTitle.setText("Instructions");
        cusTitle.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle.setPadding(10, 20, 10, 20);
        cusTitle.setGravity(Gravity.CENTER);
        cusTitle.setTextColor(Color.WHITE);
        cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        TextView welcome = new TextView(this);
        welcome.setText("\n1)Select a medication from the dropdown list\n2)press the load button to view your journal\n\n**OPTIONAL**\nShare your journal journal with your contacts by pressing the SHARE button.");
        welcome.setGravity(Gravity.CENTER_HORIZONTAL);
        welcome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        new android.app.AlertDialog.Builder(this)
                .setView(welcome)
                .setCustomTitle(cusTitle)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("journalIns", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
