package com.mapuna.com.succotash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

public class NotificationReciever extends BroadcastReceiver {
    MediaMetadataRetriever metadataRetriever;
    importantelements ie=new importantelements();

    @Override
    public void onReceive(Context context, Intent intent) {

        int i=intent.getIntExtra("buttonno",-1);
         if(i==1)
         {
             if(ie.currentpos!=0){
                 ie.currentpos=ie.currentpos-1;
                 ie.mp.stop();
                 ie.mp.release();
                 ie.mp=MediaPlayer.create(context,Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                 ie.mp.start();
             }
             else{
                 ie.currentpos=ie.mysongs.size()-1;
                 ie.mp.stop();
                 ie.mp.release();
                 ie.mp=MediaPlayer.create(context,Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                 ie.mp.start();
             }
             metadataRetriever =new MediaMetadataRetriever();
             metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());

             Bitmap artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable.headphones);
             if(metadataRetriever.getEmbeddedPicture()!=null){
                 artwork = BitmapFactory
                         .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

             }
             ie.notification.setContentTitle(ie.mysongs.get(ie.currentpos).getName().replace(".mp3",""))
                     .setLargeIcon(artwork);
             ie.notificationManager.notify(2, ie.notification.build());
         }
         else if(i==2)
         {
             if(ie.mp.isPlaying())
             {ie.mp.pause();
             }
             else
             {ie.mp.start();
             }
             ie.notificationManager.notify(2, ie.notification.build());
         }
         else if(i==3){
             if(ie.currentpos!=ie.mysongs.size()-1){
                 ie.currentpos=ie.currentpos+1;
                 ie.mp.stop();
                 ie.mp.release();
                 ie.mp=MediaPlayer.create(context,Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                 ie.mp.start();
             }
             else{
                 ie.currentpos=0;
                 ie.mp.stop();
                 ie.mp.release();
                 ie.mp=MediaPlayer.create(context,Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                 ie.mp.start();
             }

             metadataRetriever =new MediaMetadataRetriever();
             metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());

             Bitmap artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable.headphones);
             if(metadataRetriever.getEmbeddedPicture()!=null){
                 artwork = BitmapFactory
                         .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

             }
             ie.notification.setContentTitle(ie.mysongs.get(ie.currentpos).getName().replace(".mp3",""))
                             .setLargeIcon(artwork);
             ie.notificationManager.notify(2, ie.notification.build());
         }
         else if (i==4){
             int pos=ie.mp.getCurrentPosition();
             if(pos+10000>ie.mp.getDuration()){
                 pos=ie.mp.getDuration();
             }
             else
                 pos=pos+10000;

             ie.mp.seekTo(pos);
        }

         else if (i==0){
             int pos=ie.mp.getCurrentPosition();
             if(pos-10000<0){
                 pos=0;
             }
             else
                 pos=pos-10000;

             ie.mp.seekTo(pos);
         }


    }
}
