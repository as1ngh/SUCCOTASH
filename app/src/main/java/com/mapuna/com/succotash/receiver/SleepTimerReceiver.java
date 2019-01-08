package com.mapuna.com.succotash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mapuna.com.succotash.activities.ExitActivity;
import com.mapuna.com.succotash.importantElements;

public class SleepTimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(importantElements.mp.isPlaying()) importantElements.mp.stop();
        Log.d("ALARM", "onReceive: alarm");
        if(importantElements.mp!=null){
            importantElements.notificationManager.cancelAll();
        }
        ExitActivity.exitApplication(context);
    }
}