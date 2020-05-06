package com.example.randomizer.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.randomizer.data.MedicationDataHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//TODO: access DB, decrement qty if user confirms intake
public class UserConfirmationDialog extends Activity {

    private MedicationDataHelper dbHelper;
    private Intent intent;
    private String name, time, id;
    private int qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        intent = new Intent(this, JournalActivity.class);
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null)
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//
//        ringtone = RingtoneManager.getRingtone(this, alarmUri);
//        ringtone.play();
//        ----------------------------------------------------------------------------
//        ----------------------------------------------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("ALERT!");
//        builder.setMessage("did you take your medication?");
//        ----------------------------------------------------------------------------
//        getData
//        ----------------------------------------------------------------------------

        dbHelper = new MedicationDataHelper(this);//UserCon.this
        Cursor cursor = dbHelper.getAllData();

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
            //loop table until CURRENT DAY/TIME matches USER INPUT DATA
            for(int i = 0; i < cursor.getCount(); i++){//each med/row

                //    ID 0 | NAME 1| RSX 2| DOSE 3| QUANTITY 4|
                //    REFILLS 5| DATE 6-------TIME7 | TAKEN 8| INFO 9 | CODES 10
                String []tokens = cursor.getString(10).split("\\s");
                for (String token : tokens) {//check each "code"
                    if (token.equals("8")&& cursor.getString(7).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {//match
                        name = cursor.getString(1);
                        id = cursor.getString(0);
                        time = cursor.getString(7);
                        qty = Integer.parseInt(cursor.getString(4));
                        break;

                    } else if (token.equals(day) && cursor.getString(7).equals(system_hour +":"+system_minute)
                            &&(i == (Integer.parseInt(cursor.getString(0)))-1)) {//match
                        name = cursor.getString(1);
                        id = cursor.getString(0);
                        time = cursor.getString(7);
                        qty = Integer.parseInt(cursor.getString(4));
                        break;
                    }
                }
                cursor.moveToNext();
            }
            cursor.close();
        }
        builder.setMessage("did you take your " +name +" yet?");
//        ----------------------------------------------------------------------------
//        decrement, update "TAKEN" = Y
//        ---------------------------------------------------------------------------
        builder.setPositiveButton("YES",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                                nextDialog("Y");
                }
            });
//        -------------------------------------------------------------------------
        builder.setNegativeButton("NO",
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(UserConfirmationDialog.this, "Leave QTY", Toast.LENGTH_SHORT).show();
                                nextDialog("N");
                }
            });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
//        ----------------------------------------------------------------------------
//          dialog to update data
//        ----------------------------------------------------------------------------
    private void nextDialog(final String confirmation){

        final StringBuilder buffer = new StringBuilder();

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText comment = new EditText(this);
        comment.setHint("any comments to add to your journal?");
        comment.setInputType(InputType.TYPE_CLASS_TEXT);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Journal Entry");
        builder.setCancelable(false);

        layout.addView(comment);
        builder.setView(layout);
        builder.setPositiveButton("ADD to journal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buffer.append(comment.getText());

                //making sure ONLY ONE row is changed(update returns number of rows affected)
                if (dbHelper.updateData(id, name, time, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                confirmation, qty, UserConfirmationDialog.this, buffer.toString()) == 1) {
//                    ringtone.stop();
                    startActivity(intent);
                    finish();
                }
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buffer.append("no comment");

                //making sure ONLY ONE row is changed(update returns number of rows affected)
                if (dbHelper.updateData(id, name, time, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                        confirmation, qty, UserConfirmationDialog.this, buffer.toString()) == 1) {
//                    ringtone.stop();
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.show();
    }

}
