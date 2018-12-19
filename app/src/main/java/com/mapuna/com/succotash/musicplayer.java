package com.mapuna.com.succotash;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class musicplayer extends AppCompatActivity {

    importantelements ie=new importantelements();

    TextView musicname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musicplayer);

        musicname=(TextView)findViewById(R.id.musicname_id);

        musicname.setText(ie.mysongs.get(ie.currentpos).getName().replace(".mp3",""));





    }
}
