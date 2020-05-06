package com.example.randomizer.activities;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.randomizer.R;
import com.example.randomizer.data.MedicationDataHelper;
import com.example.randomizer.dialogs.NumberPickerDialog;
import com.example.randomizer.fragments.TimePickerFragment;
import com.example.randomizer.receivers.AlarmReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


public class enterDataActivity extends AppCompatActivity
        implements NumberPicker.OnValueChangeListener, TimePickerDialog.OnTimeSetListener {

    private static final int CAMERA_PIC_REQUEST = 1337;

    private AlarmManager alarmManager;
    private MedicationDataHelper myDB;
    private LinkedHashMap<String, String> map;

    private EditText drugName, drugQuantity, drugRefills, drugRSX, drugInfo;
    private String refills, rsx, info;
    private List<EditText> editTexts;

    private TextView drugTime, displayDays, displayPillsPerDay, drugDosage;
    private String perDay, dose;
    private List<TextView> textViews;

    private Button addData_Button;

    private Calendar userPickedTime;

    ArrayList<String> codes;

    private int count;//TODO: use as requestCode
    private int pillsPerDAY;

    private String userTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);


        SharedPreferences prefs = getSharedPreferences("enterDataPref", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        }
        myDB = new MedicationDataHelper(this);//calls dataHelper constructor (creating DB)

        drugName = (EditText) findViewById(R.id.medName_editText);
        drugQuantity = (EditText) findViewById(R.id.quantityCount_editText);
        drugRefills = (EditText) findViewById(R.id.refillCount_editText);
        drugRSX = (EditText) findViewById(R.id.RSX_editText);
        drugInfo = (EditText) findViewById(R.id.Description_editText);

        drugName.addTextChangedListener(userInputWatcher);
        drugQuantity.addTextChangedListener(userInputWatcher);

        drugDosage = (TextView) findViewById(R.id.setDosage_textView);
        drugTime = (TextView) findViewById(R.id.setAlarm);
        displayDays = (TextView) findViewById(R.id.setDays_textView);
        displayPillsPerDay = (TextView) findViewById(R.id.pillPerDay_textView);

        drugTime.addTextChangedListener(userInputWatcher);
        displayDays.addTextChangedListener(userInputWatcher);
        displayPillsPerDay.addTextChangedListener(userInputWatcher);

        map = new LinkedHashMap<>();
        //------------------------------------------------------------
        count = myDB.getAllData().getCount();//TODO: requestCode
        //---------------------------------
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //display time picker
        drugTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        addData_Button = (Button) findViewById(R.id.nextButton);
        Button cancel_Button = findViewById(R.id.cancelButton);
        cancel_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (enterDataActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        addData();
    }
//    ------------------------------------------------------------------------------------
//    Enables ADD button until the fields are NOT empty
//    ------------------------------------------------------------------------------------
    private TextWatcher userInputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

//    ------------------------------------------------------------------------------------
//    Enable buttons until certain textFields are filled
//    ------------------------------------------------------------------------------------
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = drugName.getText().toString().trim();
                String qty = drugQuantity.getText().toString().trim();
                String time = drugTime.getText().toString().trim();
                String days = displayDays.getText().toString().trim();
                String numPills = displayPillsPerDay.getText().toString().trim();
                addData_Button.setEnabled(!name.isEmpty() && !qty.isEmpty()
                        && !time.isEmpty() && !days.isEmpty() && !numPills.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
//      ------------------------------------------------------------
//      Retrieve data from text fields
//    -------------------------------------------------------------
    private void addData(){
        addData_Button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                refills = drugRefills.getText().toString().trim();
//                refills.trim();
                rsx = drugRSX.getText().toString().trim();
                info = drugInfo.getText().toString().trim();

                perDay = displayPillsPerDay.getText().toString().trim();
                dose = drugDosage.getText().toString().trim();

                //confirm data dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(enterDataActivity.this);
                builder.setCancelable(true);
                builder.setTitle("The information you entered: ");
//                ----------------------------------------------------------------------------------
//                Display user input, add to DB if user confirms the data is correct
//                (TODO: display NON-empty fields ONLY)
//                ----------------------------------------------------------------------------------
                StringBuilder buffer = new StringBuilder();

                buffer.append("is the following information correct?\n");
                buffer.append("\nNAME: ").append(drugName.getText().toString()).append("\n");
                buffer.append("----------------------\n");
                if(!drugRSX.getText().toString().isEmpty()) {
                    buffer.append("RSX#: ").append(drugRSX.getText().toString()).append("\n");
                    buffer.append("----------------------\n");
                }else
                    rsx = "--";
                buffer.append("QUANTITY: ").append(drugQuantity.getText().toString()).append("\n");
                buffer.append("----------------------\n");

                if(!drugRefills.getText().toString().isEmpty()){
                buffer.append("REFILLS: ").append(drugRefills.getText().toString()).append("\n");
                buffer.append("----------------------\n");
                }else
                    refills = "--";
                if(!drugDosage.getText().toString().isEmpty()) {
                    buffer.append("DOSAGE: ").append(drugDosage.getText().toString()).append("\n");
                    buffer.append("----------------------\n");
                }
                else
                    dose = "--";
                buffer.append("PILL PER DAY: ").append(displayPillsPerDay.getText().toString()).append("\n");
                buffer.append("----------------------\n");
                buffer.append("PICKED DAYS: ").append(displayDays.getText().toString()).append("\n");
                buffer.append("----------------------\n");
                buffer.append("TIME SELECTED: ").append(DateFormat.getTimeInstance(DateFormat.SHORT)
                        .format(userPickedTime.getTime())).append("\n");
                buffer.append("----------------------\n");
                if(!drugInfo.getText().toString().isEmpty()) {
                    buffer.append("INFO: ").append(drugInfo.getText().toString()).append("\n");
                    buffer.append("----------------------\n\n");
                }
                else
                    info = "--";


                builder.setMessage(buffer.toString());

                final String code = String.join(" ", codes);
                //if true, proceed to add data to DB
                //REMINDER: add codes
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                boolean inserted = myDB.insertData(drugName.getText().toString(), rsx,
                                        dose, Integer.parseInt(drugQuantity.getText().toString()),refills,
                                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()).toString(),
                                        userTime, "Y/N", info, code,enterDataActivity.this);
//                                DateFormat.getTimeInstance(DateFormat.SHORT).format(userPickedTime.getTime()),//replace with userTime

//                                ----------------------------------------------------------------------------------
//                                ADD data to DB if true
//                                ----------------------------------------------------------------------------------
                                if(inserted) {
                                    count++;
                                    Toast.makeText(enterDataActivity.this,"Data inserted", Toast.LENGTH_LONG).show();
                                    startAlarm();
                                    Toast.makeText(enterDataActivity.this,"Alarm created", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(enterDataActivity.this, ViewAlarms_activity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(enterDataActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(enterDataActivity.this, ViewAlarms_activity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
                //-------------------------------------------------------------------------
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
//-----------------------------------------------------------------------------------------------
//                   ALARM METHODS
//-----------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        userPickedTime = Calendar.getInstance();
        userPickedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        userPickedTime.set(Calendar.MINUTE, minute);
        userPickedTime.set(Calendar.SECOND, 0);//start alarm at 0 seconds

        userTime = userPickedTime.get(Calendar.HOUR_OF_DAY)
                +":" + userPickedTime.get(Calendar.MINUTE);//TODO: had minute
        String timeText = "Alarm set for: ";
        timeText+= DateFormat.getTimeInstance(DateFormat.SHORT).format(userPickedTime.getTime());

        drugTime.setText(timeText);
    }

    //    ----------------------------------------------------------------------------------
//                  give each medication their own "requestCode" to prevent alarms
//                  from overriding each other
//    ----------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, count , intent, 0);

        if (userPickedTime.before(Calendar.getInstance())) {
            userPickedTime.add(Calendar.DATE, 1);
        }

        assert alarmManager != null;

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, userPickedTime.getTimeInMillis(), pendingIntent);
    }
//    ----------------------------------------------------------------------------------
//    set days
//    ----------------------------------------------------------------------------------
    public void showDayPicker(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select days:");
        final ArrayList<String> selectedDays = new ArrayList<>();
        final String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        boolean[] checkedItems = {false, false, false, false, false, false , false};

        builder.setMultiChoiceItems(days, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // The user checked or unchecked a box
                if(isChecked)
                    selectedDays.add(days[which]);
                else if (selectedDays.contains(which))
                    selectedDays.remove(String.valueOf(which));
            }
        });
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                displayDays.setText("");
                codes = new ArrayList<>();
                String dayDisplay = null;

//----------------------------------------------------------------------------------
//                1=Sunday, 2 = Monday, ..., 7 = Saturday, 8 = DAILY
//----------------------------------------------------------------------------------
                for (String day : selectedDays) {
                    switch (day){
                        case "Sunday":
                            codes.add("1");displayDays.append("Su, ");break;
                        case "Monday":
                            codes.add("2");displayDays.append("M, ");break;
                        case "Tuesday":
                            codes.add("3");displayDays.append("Tu, ");break;
                        case "Wednesday":
                            codes.add("4");displayDays.append("We, ");break;
                        case "Thursday":
                            codes.add("5");displayDays.append("Th, ");break;
                        case "Friday":
                            codes.add("6");displayDays.append("Fr, ");break;
                        case "Saturday":
                            codes.add("7");displayDays.append("Sa, ");break;
                        default:
                            Toast.makeText(enterDataActivity.this, "Day not selected.", Toast.LENGTH_SHORT).show();
                    }

                }

                String str = displayDays.getText().toString();
                if(codes.size() == 7){
                    codes.clear();
                    codes.add("8");//ALL CHECKED
//                    Toast.makeText(enterDataActivity.this,"everyday", Toast.LENGTH_LONG).show();
                    displayDays.setText("daily");
                }
                else{
                    Toast.makeText(enterDataActivity.this,str, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                displayDays.setText("");
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
//-----------------------------------------------------------------------------------------------
//    NUMBER PICKER methods
//-----------------------------------------------------------------------------------------------
    @Override
    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
        displayPillsPerDay.setText(numberPicker.getValue() + " per day");
//        setPillsPerDay(numberPicker.getValue());
    }
    public void showNumberPicker(View view){
        NumberPickerDialog newFragment = new NumberPickerDialog();
        newFragment.setValueChangeListener(this);
        newFragment.show(getSupportFragmentManager(), "time picker");
    }
//    private void setPillsPerDay(int num){
//        pillsPerDAY = num;
//    }
//--------------------------------------------------------------------------------------
//    Custom alert dialog to get dosage
//    TODO (optional): add dropdown list (g/mg/IU units)
//--------------------------------------------------------------------------------------
    public void showDosageDialog(View view){

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText dose = new EditText(this);
        final EditText measurement = new EditText(this);
        dose.setHint("dosage");
        measurement.setHint("g/mg");
        dose.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        dose.setInputType(InputType.TYPE_CLASS_TEXT);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dosage");
//        builder.setMessage("Set # mg/ui/g");

        layout.addView(dose);
        layout.addView(measurement);
        builder.setView(layout);


        builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drugDosage.setText(dose.getText().toString() + " " + measurement.getText().toString());
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
//    ----------------------------------------------------------------------------------
//    these permissions are needed for my phone.
//    Samsung Galaxy s7 edge (Android 8 / OREO)
//    ----------------------------------------------------------------------------------
    public void openCamera(View view) {

        int permissionCheck = ContextCompat.checkSelfPermission(enterDataActivity.this, Manifest.permission.CAMERA);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, 0);
        }
        else {

            // we don't have it, request camera permission from system
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PIC_REQUEST);
        }
    }
    //------------------------------------------------------------
    //              dialog activates for every new installation
    //              data is saved in preferences files
    //------------------------------------------------------------
    private void showStartDialog() {
        TextView cusTitle = new TextView(this);
        cusTitle.setText("Getting Started");
        cusTitle.setBackgroundColor(Color.parseColor("#12a3eb"));
        cusTitle.setPadding(10, 20, 10, 20);
        cusTitle.setGravity(Gravity.CENTER);
        cusTitle.setTextColor(Color.WHITE);
        cusTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);


        new android.app.AlertDialog.Builder(this)
//                .setTitle("Getting Started")
                .setCustomTitle(cusTitle)
                .setMessage("\nFill out the form, but just know that you must fill out:" +
                        "\n\nMedication Name\nQuantity\nPills Per Day\nSet Days\nSet Alarm Time\n")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        SharedPreferences prefs = getSharedPreferences("enterDataPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
}
