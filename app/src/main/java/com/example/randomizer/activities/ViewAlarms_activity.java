package com.example.randomizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
    private ArrayList<String> NAME = new ArrayList<String>();
    private ArrayList<String> DATE = new ArrayList<String>();
    private ArrayList<String> INFO = new ArrayList<String>();


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
        data_ListView.setOnItemClickListener(selectedAlarm);

    }
    private AdapterView.OnItemClickListener selectedAlarm = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent intent = new Intent (ViewAlarms_activity.this, editAlarm_ItemActivity.class);
            startActivity(intent);
        }
    };
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

        NAME.clear();
        DATE.clear();
        INFO.clear();

        if (cursor.moveToFirst()) {
            do {
//                database
//                    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
//                    REFILLS 6| DATE 7| TAKEN 8| INFO 9
//                is this where I fucked up?
                NAME.add(cursor.getString(cursor.getColumnIndex("NAME")));
                DATE.add(cursor.getString(cursor.getColumnIndex("DATE")));
                INFO.add(cursor.getString(cursor.getColumnIndex("INFO")));

            } while (cursor.moveToNext());
        }

        AlarmListAdapter adapter = new AlarmListAdapter(ViewAlarms_activity.this, NAME,DATE, INFO);
        data_ListView.setAdapter(adapter);
        cursor.close();
    }
}
