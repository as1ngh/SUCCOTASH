package com.mapuna.com.succotash;

import android.app.Notification;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class importantelements {

    static MediaPlayer mp;
    static ArrayList<File>mysongs;
    static int currentpos = -1;
    static int recyclerint;
    static NotificationCompat.Builder notification;
    static NotificationManager notificationManager;
    static List<Integer> recently =new ArrayList<>();
    static int shuffle=0;
    static int looping=0;

}
