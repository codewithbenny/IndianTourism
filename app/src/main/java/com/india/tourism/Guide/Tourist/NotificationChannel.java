package com.india.tourism.Guide.Tourist;

import android.app.Application;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationChannel extends Application {
    public static final String CHANNEL_1_ID="channel1";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            android.app.NotificationChannel channel1=new android.app.NotificationChannel(
                    CHANNEL_1_ID,"channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("This is channel 1");

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
        }
    }
    }

