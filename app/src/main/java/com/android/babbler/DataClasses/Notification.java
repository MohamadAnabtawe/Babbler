package com.android.babbler.DataClasses;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.android.babbler.LoginRegister.LoginActivity;
import com.android.babbler.R;

/**
 * Created by MKA95 on 12/25/2017.
 */

public class Notification {
    private String title;
    private String message;
    private Context context;

    //the function gets the notification context,title and a message to show
    public void notifyMessage(String title , String message,Context context){
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(context)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.logo_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.logo_icon))
                .setContentTitle(title)
                .setContentText(message);
        NotificationManager notificationManager=(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notificationBuilder.build());
    }
}
