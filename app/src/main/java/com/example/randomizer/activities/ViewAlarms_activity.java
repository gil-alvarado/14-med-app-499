package com.example.randomizer.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.randomizer.R;
import com.example.randomizer.adapters.AlarmListAdapter;
import com.example.randomizer.data.MedicationDataHelper;

import java.util.ArrayList;

//TODO: work on user updating/editing selected alarm
public class ViewAlarms_activity extends AppCompatActivity {

    //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
    //    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//-----------------------------------------------------
    private ArrayList<String> NAME = null;//new ArrayList<String>();
    private ArrayList<String> TIME = null;//new ArrayList<String>();
    private ArrayList<String> INFO = null;//new ArrayList<String>();
    private ArrayList<String> CODES =null;// new ArrayList<String>();


    MedicationDataHelper dBHelper;

    Cursor cursor;

    ListView data_ListView;

//    AlarmListAdapter adapter;
    //    AlarmCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_alarms);
        dBHelper = new MedicationDataHelper(this);//load database

        data_ListView = (ListView) findViewById(R.id.alarmList);
//        data_ListView.setOnItemClickListener(selectedAlarm);

        MedicationDataHelper DB = new MedicationDataHelper(this);
        if(DB.getAllData().getCount() == 0){

            final TextView cusTitle = new TextView(this);
            cusTitle.setText("ALERT!");
            cusTitle.setBackgroundColor(Color.parseColor("#d31141"));
            cusTitle.setPadding(10, 20, 10, 20);
            cusTitle.setGravity(Gravity.CENTER);
            cusTitle.setTextColor(Color.WHITE);
            cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewAlarms_activity.this);
            builder.setCancelable(false);
            builder.setCustomTitle(cusTitle);
            builder.setMessage("\nNo medication has been entered yet!\nWould you like to add one?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ViewAlarms_activity.this, enterDataActivity.class);
                    startActivity(intent);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(ViewAlarms_activity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }
//    private AdapterView.OnItemClickListener selectedAlarm = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            Intent intent = new Intent (ViewAlarms_activity.this, editAlarm_ItemActivity.class);
//            startActivity(intent);
//        }
//    };
//      -------------------------------------------------------------
//      -------------------------------------------------------------

    public void goHome(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void addMed(View view){
        Intent intent = new Intent (this, enterDataActivity.class);
        startActivity(intent);

    }
//    -------------------------------------------------------------
//    -------------------------------------------------------------
    @Override
    protected void onResume() {
        displayData();
        super.onResume();
    }
    //    ToBeDisabled: was for testing
    private void displayData() {
        cursor = dBHelper.getAllData();//sqLiteDatabase.rawQuery("SELECT * FROM  PRESCRIPTION_DETAILS",null);

            NAME =  new ArrayList<>();
            TIME = new ArrayList<>();
            CODES = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
//                database
//                    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
//                    REFILLS 6| DATE 7| TAKEN 8| INFO
                NAME.add(cursor.getString(cursor.getColumnIndex("NAME")));
                TIME.add(cursor.getString(cursor.getColumnIndex("TIME")));
                    CODES.add(cursor.getString(cursor.getColumnIndex("CODES")));

            } while (cursor.moveToNext());
        }

        AlarmListAdapter adapter = new AlarmListAdapter(ViewAlarms_activity.this, NAME,TIME, CODES);
        data_ListView.setAdapter(adapter);
        cursor.close();
    }
}
