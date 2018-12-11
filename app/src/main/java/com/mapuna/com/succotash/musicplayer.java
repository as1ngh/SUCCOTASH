package com.mapuna.com.succotash;


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import java.io.IOException;


public class musicplayer extends AppCompatActivity implements musicController.MediaPlayerControl {
    importantelements ie=new importantelements();
    private musicController mMediaController;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        mMediaController = new musicController(this);
        mMediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle next click here
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Handle previous click here
            }
        });
        mMediaController.setMediaPlayer(musicplayer.this);
        mMediaController.setAnchorView((ViewGroup) findViewById(R.id.audioView));




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
    }





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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,musiclist_activity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }


}
