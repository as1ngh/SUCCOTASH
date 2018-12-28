package com.mapuna.com.succotash;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;


public class MyService extends Service {

    MediaMetadataRetriever metadataRetriever;


    public static final String CHANNEL_1_ID = "channel1";

    private MediaSessionCompat mediaSession;

    NotificationCompat.Builder builder;
    importantelements ie=new importantelements();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        ie.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ie.currentpos!=ie.mysongs.size()-1){
                    ie.currentpos=ie.currentpos+1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                }
                else{
                    ie.currentpos=0;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                }
            }
        });

        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());

        if(ie.mp!=null)
       {
           mediaSession = new MediaSessionCompat(this, "tag");
           ie.notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
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

           Intent activityIntent = new Intent(this, musicplayer.class);
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


            ie.notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                   .setSmallIcon(R.drawable.headphones)
                   .setContentTitle(ie.mysongs.get(ie.currentpos).getName().replace(".mp3",""))
                   .setContentText("MESSAGE")
                   .setLargeIcon(artwork)
                    .setAutoCancel(true)
                   .setContentIntent(contentIntent)
                   .addAction(R.drawable.previous, "Previous", previous)
                   .addAction(R.drawable.rewind, "rewind", rewind)
                   .addAction(ie.mp.isPlaying()?R.drawable.pause:R.drawable.play, "Pause", playpause)
                   .addAction(R.drawable.forward, "forward", forward)
                   .addAction(R.drawable.next, "Next", next)
                   .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                           .setShowActionsInCompactView(1, 2, 3).setMediaSession(mediaSession.getSessionToken())
                   )
                   .setSubText("Sub Text")
                   .setPriority(NotificationCompat.PRIORITY_LOW)
                   ;

           ie.notificationManager.notify(2, ie.notification.build());
       }
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //notificationManager.cancel(2);
        Log.d("DESTROY", "onDestroy:APPDES ");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
//Toast.makeText(this, “service called: “, Toast.LENGTH_LONG).show();
        super.onTaskRemoved(rootIntent);
        ie.notificationManager.cancelAll();
    }
}