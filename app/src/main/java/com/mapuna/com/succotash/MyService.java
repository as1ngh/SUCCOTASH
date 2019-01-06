package com.mapuna.com.succotash;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.mapuna.com.succotash.activities.musiclist_activity;
import com.mapuna.com.succotash.receiver.NotificationReciever;

import java.util.Random;


public class MyService extends Service {

    MediaMetadataRetriever metadataRetriever;


    public static final String CHANNEL_1_ID = "channel1";


    NotificationCompat.Builder builder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(importantElements.mp !=null)
       {
           //SET PROPERTIES OF NEXT SONG ON SERVICE CLASS
           metadataRetriever =new MediaMetadataRetriever();
           metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());
           MediaSessionCompat mediaSession;
           mediaSession = new MediaSessionCompat(this, "tag");
           importantElements.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

           importantElements.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
               @Override
               public void onCompletion(MediaPlayer mp) {
                   if(importantElements.looping ==1){
                       importantElements.mp.stop();
                       importantElements.mp.release();
                       importantElements.mp =null;
                       importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                       importantElements.mp.start();
                   }
                   else if(importantElements.shuffle ==1){
                       importantElements.currentpos =new Random().nextInt(importantElements.mysongs.size());
                       importantElements.mp.stop();
                       importantElements.mp.release();
                       importantElements.mp =null;
                       importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                       importantElements.mp.start();
                   }
                   else if(importantElements.shuffle==0 && importantElements.looping==0){
                       if(importantElements.currentpos != importantElements.mysongs.size()-1){
                           importantElements.currentpos=importantElements.currentpos+1;
                           importantElements.mp.stop();
                           importantElements.mp.release();
                           importantElements.mp =null;
                           importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                           importantElements.mp.start();
                       }
                       else{
                           importantElements.currentpos =0;
                           importantElements.mp.stop();
                           importantElements.mp.release();
                           importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                           importantElements.mp.start();
                       }
                   }
                   metadataRetriever =new MediaMetadataRetriever();
                   metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());

                   Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.headphones);
                   if(metadataRetriever.getEmbeddedPicture()!=null){
                       artwork = BitmapFactory
                               .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

                   }
                   importantElements.notification.setContentTitle(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3",""))
                           .setLargeIcon(artwork);
                   importantElements.notificationManager.notify(2, importantElements.notification.build());
               }
           });






          //BUILDING NOTIFICATION CHANNEL AND NOTIFICATION
           builder=new NotificationCompat.Builder(this,CHANNEL_1_ID);

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               NotificationChannel channel1 = new NotificationChannel(
                       CHANNEL_1_ID,
                       "Channel 1",
                       NotificationManager.IMPORTANCE_LOW
               );
               channel1.setDescription("This is Channel 1");
               NotificationManager manager = getSystemService(NotificationManager.class);
               manager.createNotificationChannel(channel1);
           }

           Bitmap artwork = BitmapFactory.decodeResource(getResources(), R.drawable.headphones);
           if(metadataRetriever.getEmbeddedPicture()!=null){
               artwork = BitmapFactory
                       .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

           }

           Intent activityIntent = new Intent(this, musiclist_activity.class);
           PendingIntent contentIntent = PendingIntent.getActivity(this,
                   6, activityIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

           Intent broadcastIntent0 = new Intent(this, NotificationReciever.class);
           broadcastIntent0.putExtra("buttonno", 0);
           PendingIntent rewind = PendingIntent.getBroadcast(this,
                   4, broadcastIntent0, PendingIntent.FLAG_UPDATE_CURRENT);

           Intent broadcastIntent1 = new Intent(this, NotificationReciever.class);
           broadcastIntent1.putExtra("buttonno", 1);
           PendingIntent previous = PendingIntent.getBroadcast(this,
                   0, broadcastIntent1, PendingIntent.FLAG_UPDATE_CURRENT);

           Intent broadcastIntent2 = new Intent(this, NotificationReciever.class);
           broadcastIntent2.putExtra("buttonno", 2);
           PendingIntent playpause = PendingIntent.getBroadcast(this,
                   1, broadcastIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

           Intent broadcastIntent3 = new Intent(this, NotificationReciever.class);
           broadcastIntent3.putExtra("buttonno", 3);
           PendingIntent next = PendingIntent.getBroadcast(this,
                   2, broadcastIntent3, PendingIntent.FLAG_UPDATE_CURRENT);

           Intent broadcastIntent4 = new Intent(this, NotificationReciever.class);
           broadcastIntent4.putExtra("buttonno", 4);
           PendingIntent forward = PendingIntent.getBroadcast(this,
                   5, broadcastIntent4, PendingIntent.FLAG_UPDATE_CURRENT);


            importantElements.notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                   .setSmallIcon(R.drawable.headphones)
                   .setContentTitle(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3",""))
                   .setContentText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
                   .setLargeIcon(artwork)
                    .setAutoCancel(true)
                   .setContentIntent(contentIntent)
                   .addAction(R.drawable.previous, "Previous", previous)
                   .addAction(R.drawable.rewind, "rewind", rewind)
                   .addAction(R.drawable.power, "Pause", playpause)
                   .addAction(R.drawable.forward, "forward", forward)
                   .addAction(R.drawable.next, "Next", next)
                   .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                           .setShowActionsInCompactView(1, 2, 3).setMediaSession(mediaSession.getSessionToken())
                   )
                   .setSubText("Sub Text")
                   .setPriority(NotificationCompat.PRIORITY_LOW)
                   ;

           importantElements.notificationManager.notify(2, importantElements.notification.build());
       }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //notificationManager.cancel(2);
        if(importantElements.mp!=null){
            importantElements.notificationManager.cancelAll();
        }
        Log.d("DESTROY", "onDestroy:APPDES ");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//Toast.makeText(this, “service called: “, Toast.LENGTH_LONG).show();
        super.onTaskRemoved(rootIntent);
        if(importantElements.mp!=null){
            importantElements.notificationManager.cancelAll();
        }
    }
}