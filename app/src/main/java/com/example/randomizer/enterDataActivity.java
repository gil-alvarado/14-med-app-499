package com.example.randomizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;

public class enterDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        Button toTime = (Button)findViewById(R.id.nextButton);

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSetTimeActivity();
            }
        });

        //work with database
        //or extract data
    }

    private void goToSetTimeActivity(){
        Intent intent = new Intent(enterDataActivity.this, dosageActivity.class);

        startActivity(intent);
    }
}
