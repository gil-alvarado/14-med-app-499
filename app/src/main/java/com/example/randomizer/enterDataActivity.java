package com.example.randomizer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.NumberPicker;

import java.util.Calendar;


/*
UPDATE DATABASE FOR DOSE AND DATE
 */

public class enterDataActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{

    DataHelper myDB;
    private EditText drugName, drugQuantity, drugRefills, drugRSX, drugInfo;
    private Button next, viewData, updateButton;
    private TextView mDisplayDate, displayDosage, displayDays;
    //dosage: times per day
    //days per week
    //time
    // IDEA: disable textView until input is set
    //OR CHANGE COLOR
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final String TAG = "enterDataActivity";
    private NumberPicker picker1;
    private String[] pickerVals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);

        myDB = new DataHelper(this);//calls dataHelper constructor (creating DB)

        drugName = (EditText) findViewById(R.id.medName_editText);
        drugQuantity = (EditText) findViewById(R.id.quantityCount_editText);
        drugRefills = (EditText) findViewById(R.id.refillCount_editText);
        drugRSX = (EditText) findViewById(R.id.RSX_editText);
        drugInfo = (EditText) findViewById(R.id.Description_editText);

        mDisplayDate = (TextView) findViewById(R.id.setAlarm);
        displayDosage = (TextView) findViewById(R.id.setDosage);
        displayDays = (TextView) findViewById(R.id.setDays);
        //------------------------------------------------------------
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        enterDataActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        next = (Button) findViewById(R.id.nextButton);
        viewData = (Button) findViewById(R.id.viewDataButton);
        updateButton = (Button) findViewById(R.id.updateButton);

        addData();
        viewData();
    }
    //------------------------------------------------------------
    //NumberPicker "listener"
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        Toast.makeText(this,
                "selected number " + numberPicker.getValue(), Toast.LENGTH_SHORT).show();
        displayDosage.setText(String.valueOf(numberPicker.getValue()) + " per day");
    }

    public void showNumberPicker(View view){
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }

    //------------------------------------------------------------
    public void updatDate(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean updated = myDB.updateData(drugName.getText().toString(),
                        drugQuantity.getText().toString(), drugRefills.getText().toString(),
                        drugRSX.getText().toString());
            }
        });
    }
    //------------------------------------------------------------
    public void addData(){
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean inserted = myDB.insertData(drugName.getText().toString(),
                        drugQuantity.getText().toString(), drugRefills.getText().toString(),
                        drugRSX.getText().toString(), drugInfo.getText().toString());

                if(inserted)
                    Toast.makeText(enterDataActivity.this,  "Data Inserted", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(enterDataActivity.this,  "Something went wrong", Toast.LENGTH_LONG).show();

                //NOTE: STILL DIDN'T ADD TIME!!!
            }
        });

    }
    //------------------------------------------------------------
    public void viewData(){
            viewData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor result =  myDB.getAllData();

                    if(result.getCount() == 0)
                    {
                        showMessage("ERROR", "NO DATA FOUND");
                        return;
                    }

                    StringBuffer buffer = new StringBuffer();
                    while(result.moveToNext()){
                        buffer.append("ID: " + result.getString(0) + "\n");
                        buffer.append("NAME: " + result.getString(1) + "\n");
                        buffer.append("QUANTITY: " + result.getString(2) + "\n");
                        buffer.append("REFILLS: " + result.getString(3) + "\n");
                        buffer.append("RSX: " + result.getString(4) + "\n\n");
                    }
                    //SHOW ALL DATA
                    showMessage("Data", buffer.toString());
                }
            });
    }
    //------------------------------------------------------------
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.show();
    }
    //------------------------------------------------------------
    private void goToSetTimeActivity(){
        Intent intent = new Intent(enterDataActivity.this, dosageActivity.class);

        startActivity(intent);
    }
}
