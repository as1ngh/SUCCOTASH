package com.mapuna.com.succotash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SleepTimerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(importantElements.mp.isPlaying()) importantElements.mp.stop();
        Log.d("ALARM", "onReceive: alarm");
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}