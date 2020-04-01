package com.example.randomizer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



public class AddMedication extends AppCompatActivity {


    //two buttons: OCR button, enter data Button
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        Button enterDataButton = (Button) findViewById(R.id.enterDataButton);

        enterDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEnterDataActivity();
            }
        });

    }

    private void goToEnterDataActivity(){
        Intent intent = new Intent(AddMedication.this, enterDataActivity.class);
        startActivity(intent);
    }
}
