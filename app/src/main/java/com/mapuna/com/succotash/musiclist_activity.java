package com.mapuna.com.succotash;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class musiclist_activity extends AppCompatActivity{

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    TextView musicname;
    Button play_pause;
    Button stop;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist_activity);


        tabLayout=(TabLayout)findViewById(R.id.tablayout_id);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        musicname=(TextView)findViewById(R.id.musicname_id);
        play_pause=(Button)findViewById(R.id.play_pause_id);
        stop=(Button)findViewById(R.id.stop_id);




        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragmentmusiclist(),"music_list");
        adapter.AddFragment(new fragmentalbum(),"album_list");
        adapter.AddFragment(new fragmentartist(),"artist_list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(getIntent().getExtras().get("filename")!=null) {
            File file = (File) getIntent().getExtras().get("filename");
            mp=MediaPlayer.create(this,Uri.parse(file.getAbsolutePath()));
            musicname.setText(file.getName().replace(".mp3",""));
            mp.start();
            final Drawable myDrawable;
            Resources res = getResources();
            try {
                myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.pause));
                play_pause.setBackground(myDrawable);
            } catch (Exception ex) {
                Log.e("Error", "Exception loading drawable");
            }
        }

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp.isPlaying()){
                    mp.pause();
                    final Drawable myDrawable;
                    Resources res = getResources();
                    try {
                        myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                        play_pause.setBackground(myDrawable);
                    } catch (Exception ex) {
                        Log.e("Error", "Exception loading drawable");
                    }

                }
                else {
                    mp.start();
                    final Drawable myDrawable;
                    Resources res = getResources();
                    try {
                        myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.pause));
                        play_pause.setBackground(myDrawable);
                    } catch (Exception ex) {
                        Log.e("Error", "Exception loading drawable");
                    }
                }
            }
        });



    }
}
