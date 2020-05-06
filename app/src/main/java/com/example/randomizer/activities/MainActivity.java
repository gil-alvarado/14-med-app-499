package com.example.randomizer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;

import com.example.randomizer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button addButton = (Button) findViewById(R.id.LoginButton);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        }

        Button addMedButton = (Button)findViewById(R.id.addMedButton);
        Button alarmButton = (Button)findViewById(R.id.alarmButton);
        Button journalButton = (Button)findViewById(R.id.journalButton);
        Button communityButton = (Button)findViewById(R.id.communityButton);

//----------------------------------------------------------------------------------
        //go to second activity
//----------------------------------------------------------------------------------
        addMedButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                goToAddMedicationActivity();
            }
        });
//----------------------------------------------------------------------------------
        alarmButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToViewAlarms();
            }
        });
//----------------------------------------------------------------------------------
        journalButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToJournalActivity();
            }
        });
//----------------------------------------------------------------------------------
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCommunityActivity();
            }
        });
//----------------------------------------------------------------------------------
    }

//----------------------------------------------------------------------------------
//ACTIVITY Functions
//----------------------------------------------------------------------------------
    private void goToAddMedicationActivity(){
        Intent intent = new Intent(MainActivity.this, enterDataActivity.class);
        startActivity(intent);
    }
    private void goToViewAlarms(){
        Intent intent = new Intent(MainActivity.this, ViewAlarms_activity.class);
        startActivity(intent);
    }
    private void goToJournalActivity(){
        Intent intent = new Intent(MainActivity.this, JournalActivity.class);
        startActivity(intent);
    }
    private void goToCommunityActivity(){
        Intent intent = new Intent (MainActivity.this, CommunityActivity.class);
        startActivity(intent);
    }



    private void showStartDialog() {
        TextView cusTitle = new TextView(this);
        cusTitle.setText("Welcome!");
        cusTitle.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle.setPadding(10, 20, 10, 20);
        cusTitle.setGravity(Gravity.CENTER);
        cusTitle.setTextColor(Color.WHITE);
        cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);

        TextView welcome = new TextView(this);
        welcome.setText("\nThe goal of the application is to remind you to take your medication!Start off by touching the\n\n\"ADD MEDICATION\" button");
        welcome.setGravity(Gravity.CENTER_HORIZONTAL);
        welcome.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        new AlertDialog.Builder(this)
//                .setTitle("Welcome!")
                .setView(welcome)
                .setCustomTitle(cusTitle)
//                .setMessage("The goal of the application is to help you to take your medication! Start off by touching the\"ADD MEDICATION\" button")
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }


}
