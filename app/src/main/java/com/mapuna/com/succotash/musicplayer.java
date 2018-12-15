package com.mapuna.com.succotash;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;

public class musicplayer extends AppCompatActivity {

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        File file = (File) getIntent().getExtras().get("filename");
        mp=MediaPlayer.create(this,Uri.parse(file.getAbsolutePath()));
        mp.start();
    }
}
