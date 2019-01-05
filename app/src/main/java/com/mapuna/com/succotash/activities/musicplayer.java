package com.mapuna.com.succotash.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.importantElements;
import com.mapuna.com.succotash.musicController;

import java.util.Random;


public class musicplayer extends AppCompatActivity implements musicController.MediaPlayerControl {
    private musicController mMediaController;
    private Handler mHandler = new Handler();
    MediaMetadataRetriever metadataRetriever =new MediaMetadataRetriever();
    ImageView art;
    TextView musicname;
    TextView artist;
    Button shuffle;
    Button repeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        musicname= findViewById(R.id.songname);
        artist= findViewById(R.id.artistname);
        art = findViewById(R.id.musicart);
        shuffle= findViewById(R.id.shuffle);
        repeat= findViewById(R.id.repeat);

        setsmallicon();

        //43-56(TO SET THE MUSIC NAME FROM FILE SOURCE AND SET MUSI ART BY METADATA RETERIEVER CLASS)
        metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());
        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        musicname.setText(importantElements.mysongs.get(importantElements.currentpos).getName());
        if( metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null){
            artist.setText("<unknown>");
        }
        else {
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }


        //60-109(TO SET NEW CUSTOM MEDIA CONTROLLER(SEE CLASS musicController) AND ENABLED NEXT AND PREVIOUS BUTTONS TO WORK)
        mMediaController = new musicController(this);
        mMediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NEXT", "onClick:next");
                //Handle next click here
                getnextsong();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle previous click here
                if(importantElements.currentpos !=0){
                    importantElements.currentpos = importantElements.currentpos -1;
                    importantElements.mp.stop();
                    importantElements.mp.release();
                    importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                    importantElements.mp.start();
                    importantElements.recently.add(0, importantElements.currentpos);
                update();
                }
                else{
                    importantElements.currentpos = importantElements.mysongs.size()-1;
                    importantElements.mp.stop();
                    importantElements.mp.release();
                    importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                    importantElements.mp.start();
                    importantElements.recently.add(0, importantElements.currentpos);
                update();
                }
            }
        });
        mMediaController.setMediaPlayer(musicplayer.this);
        mMediaController.setAnchorView((ViewGroup) findViewById(R.id.audioView));
        mMediaController.show(10000);

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantElements.looping =0;
                if(importantElements.shuffle ==0){
                    importantElements.shuffle =1;
                }
                else if(importantElements.shuffle ==1){
                    importantElements.shuffle =0;
                }
                setsmallicon();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantElements.shuffle =0;
                if(importantElements.looping ==0){
                    importantElements.looping =1;
                }
                else if(importantElements.looping ==1){
                    importantElements.looping =0;
                }
                setsmallicon();
            }
        });






        importantElements.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mMediaController.show(10000);
                        importantElements.mp.start();
                    }
                });
            }
        });


        //TO AUTO START NEW SONG WHEN PREVIOUS SONG IS COMPLETED
        importantElements.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                getnextsong();
            }
        });
    }


    public void getnextsong(){
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
                importantElements.recently.add(0, importantElements.currentpos);
            }
            else{
                importantElements.currentpos =0;
                importantElements.mp.stop();
                importantElements.mp.release();
                importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                importantElements.mp.start();
                importantElements.recently.add(0, importantElements.currentpos);
            }
        }
        update();
    }

    public void setsmallicon(){
        if(importantElements.shuffle ==1 && importantElements.looping ==0){
            shuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_black_24dp));
            repeat.setBackground(getResources().getDrawable(R.drawable.repeat));
        }
        else if(importantElements.shuffle ==0 && importantElements.looping ==0){
            shuffle.setBackground(getResources().getDrawable(R.drawable.shuffle));
            repeat.setBackground(getResources().getDrawable(R.drawable.repeat));
        }
        else if(importantElements.shuffle ==0 && importantElements.looping ==1){
            shuffle.setBackground(getResources().getDrawable(R.drawable.shuffle));
            repeat.setBackground(getResources().getDrawable(R.drawable.ic_repeat_black_24dp));
        }


    }


    public void update(){
        metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());
        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        else{
            art.setImageDrawable(getDrawable(R.drawable.headphones));
        }

        musicname.setText(importantElements.mysongs.get(importantElements.currentpos).getName());

        if( metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null){
            artist.setText("<unknown>");
        }
        else {
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }

        importantElements.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
               getnextsong();
            }
        });


    }


    //IMPLEMENTED METHODS FROM musicController CLASS

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }


    @Override
    public int getBufferPercentage() {
        int percentage = (importantElements.mp.getCurrentPosition() * 100) / importantElements.mp.getDuration();

        return percentage;
    }

    @Override
    public int getCurrentPosition() {
        return importantElements.mp.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return importantElements.mp.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return importantElements.mp.isPlaying();
    }

    @Override
    public void pause() {
        if(importantElements.mp.isPlaying())
            importantElements.mp.pause();
    }

    @Override
    public void seekTo(int pos) {
        importantElements.mp.seekTo(pos);
    }

    @Override
    public void start() {
        importantElements.mp.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.show();

        return false;
    }





}
