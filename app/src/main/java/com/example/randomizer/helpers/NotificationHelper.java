package com.example.randomizer.helpers;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.randomizer.R;
import com.example.randomizer.activities.UserConfirmationDialog;


public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private String name;
    private int qty;

    private NotificationManager notificationManager;

    //keep medication name discrete instead
    public NotificationHelper(Context base, String name) {

        //OLDER versions hasn't been tested
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
            this.name = name;
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getChannelNotification() {

        Intent intent = new Intent(this, UserConfirmationDialog.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle("ALERT!")
                .setContentText("It's time to take your medication!")
                .setSmallIcon(R.drawable.ic_one)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);
    }

    public void setName(String name){
        this.name = name;
    }

}