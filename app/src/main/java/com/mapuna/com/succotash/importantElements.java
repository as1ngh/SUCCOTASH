package com.mapuna.com.succotash;

import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import java.io.File;
import java.util.ArrayList;

//A COMMON CLASS ACCESSIBLE BY ALL OTHER CLASS
public class importantElements {

    public static MediaPlayer mp;
    public static ArrayList<File>mysongs;
    public static int currentpos = -1;
    //static int recyclerint;
    public static NotificationCompat.Builder notification;
    public static NotificationManager notificationManager;
    public static ArrayList<Integer> recently =new ArrayList<>();
    public static int shuffle=0;
    public static int looping=0;
    public static ArrayList<ArrayList<Integer>>playlist=new ArrayList<>();

}
