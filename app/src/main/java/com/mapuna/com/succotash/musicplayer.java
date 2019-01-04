package com.mapuna.com.succotash;


import android.content.Intent;
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
import android.widget.MediaController;
import android.widget.TextView;

import java.io.IOException;


public class musicplayer extends AppCompatActivity implements musicController.MediaPlayerControl {
    importantelements ie=new importantelements();
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

        musicname=(TextView)findViewById(R.id.songname);
        artist=(TextView)findViewById(R.id.artistname);
        art =(ImageView)findViewById(R.id.musicart);
        shuffle=(Button)findViewById(R.id.shuffle);
        repeat=(Button)findViewById(R.id.repeat);

        setsmallicon();

        //43-56(TO SET THE MUSIC NAME FROM FILE SOURCE AND SET MUSI ART BY METADATA RETERIEVER CLASS)
        metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());
        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        musicname.setText(ie.mysongs.get(ie.currentpos).getName());
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
                if(ie.currentpos!=ie.mysongs.size()-1){
                    ie.currentpos=ie.currentpos+1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    update();
                    ie.recently.add(0,ie.currentpos);
                }
                else{
                    ie.currentpos=0;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    update();
                    ie.recently.add(0,ie.currentpos);
                }

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle previous click here
                if(ie.currentpos!=0){
                    ie.currentpos=ie.currentpos-1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    ie.recently.add(0,ie.currentpos);
                update();
                }
                else{
                    ie.currentpos=ie.mysongs.size()-1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    ie.recently.add(0,ie.currentpos);
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
                ie.looping=0;
                if(ie.shuffle==0){
                    ie.shuffle=1;
                }
                else if(ie.shuffle==1){
                    ie.shuffle=0;
                }
                setsmallicon();
            }
        });

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ie.shuffle=0;
                if(ie.looping==0){
                    ie.looping=1;
                }
                else if(ie.looping==1){
                    ie.looping=0;
                }
                setsmallicon();
            }
        });






        ie.mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mHandler.post(new Runnable() {
                    public void run() {
                        mMediaController.show(10000);
                        ie.mp.start();
                    }
                });
            }
        });


        //TO AUTO START NEW SONG WHEN PREVIOUS SONG IS COMPLETED
        ie.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ie.currentpos!=ie.mysongs.size()-1){
                    ie.currentpos=ie.currentpos+1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.recently.add(0,ie.currentpos);
                    ie.mp.start();
                }
                else{
                    ie.currentpos=0;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.recently.add(0,ie.currentpos);
                    ie.mp.start();
                }
                update();
            }
        });
    }

    public void setsmallicon(){
        if(ie.shuffle==1 && ie.looping==0){
            shuffle.setBackground(getResources().getDrawable(R.drawable.ic_shuffle_black_24dp));
            repeat.setBackground(getResources().getDrawable(R.drawable.repeat));
        }
        else if(ie.shuffle==0 && ie.looping==0){
            shuffle.setBackground(getResources().getDrawable(R.drawable.shuffle));
            repeat.setBackground(getResources().getDrawable(R.drawable.repeat));
        }
        else if(ie.shuffle==0 && ie.looping==1){
            shuffle.setBackground(getResources().getDrawable(R.drawable.shuffle));
            repeat.setBackground(getResources().getDrawable(R.drawable.ic_repeat_black_24dp));
        }


    }


    public void update(){
        metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());
        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        else{
            art.setImageDrawable(getDrawable(R.drawable.headphones));
        }

        musicname.setText(ie.mysongs.get(ie.currentpos).getName());

        if( metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null){
            artist.setText("<unknown>");
        }
        else {
            artist.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }

        ie.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ie.currentpos!=ie.mysongs.size()-1){
                    ie.currentpos=ie.currentpos+1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    ie.recently.add(0,ie.currentpos);
                }
                else{
                    ie.currentpos=0;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    ie.recently.add(0,ie.currentpos);
                }
                update();
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
        int percentage = (ie.mp.getCurrentPosition() * 100) / ie.mp.getDuration();

        return percentage;
    }

    @Override
    public int getCurrentPosition() {
        return ie.mp.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return ie.mp.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return ie.mp.isPlaying();
    }

    @Override
    public void pause() {
        if(ie.mp.isPlaying())
            ie.mp.pause();
    }

    @Override
    public void seekTo(int pos) {
        ie.mp.seekTo(pos);
    }

    @Override
    public void start() {
        ie.mp.start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMediaController.show();

        return false;
    }





}
