package com.mapuna.com.succotash.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.importantElements;

//RECEIVER FOR ACTIONS ON NOTIFICATION

public class NotificationReciever extends BroadcastReceiver {
    MediaMetadataRetriever metadataRetriever;

    @Override
    public void onReceive(Context context, Intent intent) {

        int i=intent.getIntExtra("buttonno",-1);
         if(i==1)
         {
             if(importantElements.currentpos !=0){
                 importantElements.currentpos = importantElements.currentpos -1;
                 importantElements.mp.stop();
                 importantElements.mp.release();
                 importantElements.mp =MediaPlayer.create(context,Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                 importantElements.mp.start();
             }
             else{
                 importantElements.currentpos = importantElements.mysongs.size()-1;
                 importantElements.mp.stop();
                 importantElements.mp.release();
                 importantElements.mp =MediaPlayer.create(context,Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                 importantElements.mp.start();
             }
             metadataRetriever =new MediaMetadataRetriever();
             metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());

             Bitmap artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable.headphones);
             if(metadataRetriever.getEmbeddedPicture()!=null){
                 artwork = BitmapFactory
                         .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

             }
             importantElements.notification.setContentTitle(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3",""))
                     .setLargeIcon(artwork)
                     .setContentText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null?"<unknown>":metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
         ;
             importantElements.notificationManager.notify(2, importantElements.notification.build());
         }
         else if(i==2)
         {
             if(importantElements.mp.isPlaying())
             {
                 importantElements.mp.pause();
             }
             else
             {
                 importantElements.mp.start();
             }


             importantElements.notificationManager.notify(2, importantElements.notification.build());
         }
         else if(i==3){
             if(importantElements.currentpos != importantElements.mysongs.size()-1){
                 importantElements.currentpos = importantElements.currentpos +1;
                 importantElements.mp.stop();
                 importantElements.mp.release();
                 importantElements.mp =MediaPlayer.create(context,Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                 importantElements.mp.start();
             }
             else{
                 importantElements.currentpos =0;
                 importantElements.mp.stop();
                 importantElements.mp.release();
                 importantElements.mp =MediaPlayer.create(context,Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                 importantElements.mp.start();
             }

             metadataRetriever =new MediaMetadataRetriever();
             metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());

             Bitmap artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable.headphones);
             if(metadataRetriever.getEmbeddedPicture()!=null){
                 artwork = BitmapFactory
                         .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);

             }
             importantElements.notification.setContentTitle(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3",""))
                             .setLargeIcon(artwork)
                             .setContentText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null?"<unknown>":metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
             importantElements.notificationManager.notify(2, importantElements.notification.build());
         }
         else if (i==4){
             int pos= importantElements.mp.getCurrentPosition();
             if(pos+10000> importantElements.mp.getDuration()){
                 pos= importantElements.mp.getDuration();
             }
             else
                 pos=pos+10000;

             importantElements.mp.seekTo(pos);
        }

         else if (i==0){
             int pos= importantElements.mp.getCurrentPosition();
             if(pos-10000<0){
                 pos=0;
             }
             else
                 pos=pos-10000;

             importantElements.mp.seekTo(pos);
         }


    }
}
