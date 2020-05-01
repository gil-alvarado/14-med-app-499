package com.example.randomizer.activities;

import androidx.appcompat.app.AlertDialog;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.randomizer.data.MedicationDataHelper;

import java.util.ArrayList;
import java.util.Calendar;

//TODO: access DB, decrement qty if user confirms intake
public class UserConfirmationDialog extends Activity {

    private MedicationDataHelper dbHelper;
    private Intent intent;
    private String name, time, id;
    private int qty;
    private Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = new Intent(this, MainActivity.class);
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final Ringtone ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
//        ----------------------------------------------------------------------------
//        ----------------------------------------------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("VALIDATION");
        builder.setMessage("did you take your medication?");
//        ----------------------------------------------------------------------------
//        getData
//        ----------------------------------------------------------------------------

        dbHelper = new MedicationDataHelper(this);//UserCon.this
        cursor = dbHelper.getAllData();

        if(cursor.equals(null))
            Toast.makeText(this,"this should not print",Toast.LENGTH_LONG).show();
        else
        {
            Calendar calendar = Calendar.getInstance();
            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
            String day = Integer.toString(dayNum);

            int system_hour = calendar.get(Calendar.HOUR_OF_DAY);
            int system_minute = calendar.get(Calendar.MINUTE);

            cursor.moveToFirst();
            //loop table
            for(int i = 0; i < cursor.getCount(); i++){//each med/row

                //    ID 1 | NAME 2| RSX 3| DOSE 4| QUANTITY 5|
                //    REFILLS 6| DATE 7| TAKEN 8| INFO 9 | CODES
                String []tokens = cursor.getString(9).split("\\s");
                for (String token : tokens) {//check each "code"
                    if (token.equals("8")&& cursor.getString(6).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {
                        name = cursor.getString(1);
                        id = cursor.getString(0);
                        time = cursor.getString(6);
                        qty = Integer.parseInt(cursor.getString(4));
                        break;

                    } else if (token.equals(day) && cursor.getString(6).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {
                        name = cursor.getString(1);
                        id = cursor.getString(0);
                        time = cursor.getString(6);
                        qty = Integer.parseInt(cursor.getString(4));
                        break;
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }

//        ----------------------------------------------------------------------------
//        decrement, update "TAKEN" = Y
//        update file?
//        ---------------------------------------------------------------------------
        builder.setPositiveButton("YES",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
            if(dbHelper.updateData(id,name,time,"Y", qty) == 1){
                cursor.close();
                    Toast.makeText(UserConfirmationDialog.this, id + " " + name + " " + time, Toast.LENGTH_SHORT).show();//this prints
                    ringtone.stop();
                    startActivity(intent);
                    UserConfirmationDialog.this.finish();
            }
                }
            });
        //-------------------------------------------------------------------------
//        ----------------------------------------------------------------------------
//        SNOOZE
//        ----------------------------------------------------------------------------
        builder.setNegativeButton("NO",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(UserConfirmationDialog.this, "Leave QTY", Toast.LENGTH_SHORT).show();
                    UserConfirmationDialog.this.finish();
//                    intents.remove(0);
                    if(dbHelper.updateData(id,name,time,"N" ,qty) == 1){
                        ringtone.stop();
                        startActivity(intent);
                    }
                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
