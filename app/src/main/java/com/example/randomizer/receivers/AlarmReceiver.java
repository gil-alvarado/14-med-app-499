package com.example.randomizer.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.randomizer.data.MedicationDataHelper;
import com.example.randomizer.helpers.NotificationHelper;
import com.example.randomizer.activities.UserConfirmationDialog;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
//ID | NAME | RSX | DOSE | QUANTITY | REFILLS | DATE | TAKEN | INFO | CODES

        MedicationDataHelper dbHelper = new MedicationDataHelper(context);

        Cursor cursor = dbHelper.getAllData();

        if(cursor.equals(null))
            Toast.makeText(context,"this should not print",Toast.LENGTH_LONG).show();
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

                //get each medItem code
                String []tokens = cursor.getString(9).split("\\s");
                for (String token : tokens) {//check each "code"
                    //get alarm to ring on particular day
                    //compare token/code, ID, time
                    //daily
                    if (token.equals("8")&& cursor.getString(6).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {
                        Intent it = new Intent(context, UserConfirmationDialog.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                        break;

                    } else if (token.equals(day) && cursor.getString(6).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {

                        Intent it = new Intent(context, UserConfirmationDialog.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(it);
                        break;
                    }
                }
                cursor.moveToNext();
            }
        }
        cursor.close();
//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
//        notificationHelper.getManager().notify(1, nb.build());
    }


}
