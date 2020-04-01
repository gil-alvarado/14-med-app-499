package com.example.randomizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Button addButton = (Button) findViewById(R.id.LoginButton);

        Button addMedButton = (Button)findViewById(R.id.addMedButton);
        Button alarmButton = (Button)findViewById(R.id.alarmButton);
        Button journalButton = (Button)findViewById(R.id.journalButton);
        Button communityButton = (Button)findViewById(R.id.communityButton);

        /*
        //First Test
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //BEGIN ON CLICK
                EditText userName = (EditText) findViewById(R.id.usernameEditText);
                EditText password = (EditText) findViewById(R.id.passwordEditText);

            }
        });
 */
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
                //BEGIN ON CLICK
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
        Intent intent = new Intent(MainActivity.this, AddMedication.class);
        startActivity(intent);
    }
    private void goToViewAlarms(){
        Intent intent = new Intent(MainActivity.this, AddMedication.class);
        startActivity(intent);
    }
    private void goToJournalActivity(){
        Intent intent = new Intent(MainActivity.this, JournalActivity.class);
        startActivity(intent);
    }
    private void goToCommunityActivity(){
        Intent intent = new Intent (MainActivity.this, AddMedication.class);
    }
}
