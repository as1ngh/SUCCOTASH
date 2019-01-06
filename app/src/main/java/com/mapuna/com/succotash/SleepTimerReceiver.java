package com.mapuna.com.succotash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mapuna.com.succotash.activities.ExitActivity;

public class SleepTimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(importantElements.mp.isPlaying()) importantElements.mp.stop();
        Log.d("ALARM", "onReceive: alarm");
        ExitActivity.exitApplication(context);
    }
}