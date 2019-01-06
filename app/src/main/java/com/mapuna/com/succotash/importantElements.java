package com.mapuna.com.succotash;

//import android.app.Notification;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class importantElements {

    public static MediaPlayer mp;
    public static ArrayList<File>mysongs;
    public static int currentpos = -1;
    //static int recyclerint;
    static NotificationCompat.Builder notification;
    static NotificationManager notificationManager;
    public static List<Integer> recently =new ArrayList<>();
    public static int shuffle=0;
    public static int looping=0;
    public static ArrayList<List<Integer>>playlist=new ArrayList<>();

}
