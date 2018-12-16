package com.mapuna.com.succotash;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class musiclist_activity extends AppCompatActivity{

    importantelements ie=new importantelements();

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    TextView musicname;
    Button play_pause;
    Button stop;

    RelativeLayout music;

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
        music=(RelativeLayout)findViewById(R.id.upplayer);

        Typeface musicfont=Typeface.createFromAsset(getAssets(),"fonts/Raleway-Light.ttf");
        musicname.setTypeface(musicfont);


        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragmentmusiclist(),"music list");
        adapter.AddFragment(new fragmentalbum(),"album list");
        adapter.AddFragment(new fragmentartist(),"artist list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(ie.mp!=null && ie.mp.isPlaying()){
            ie.mp.stop();
            ie.mp=null;
        }

        if(getIntent().getExtras().get("filename")!=null) {
            File file = (File) getIntent().getExtras().get("filename");
            ie.mp = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
            musicname.setText(file.getName().replace(".mp3", ""));
            ie.mp.start();
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
                if(ie.mp==null){
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                }
                else if(ie.mp.isPlaying()){
                    ie.mp.pause();
                    final Drawable myDrawable;
                    Resources res = getResources();
                    try {
                        myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                        play_pause.setBackground(myDrawable);
                    } catch (Exception ex) {
                        Log.e("Error", "Exception loading drawable");
                    }

                }
                else if(!ie.mp.isPlaying() && ie.mp!=null){
                    ie.mp.start();
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


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ie.mp==null){
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                }
                else{
                    ie.mp.stop();
                    ie.mp = null;
                    musicname.setText("MUSIC_NAME");
                    final Drawable myDrawable;
                    Resources res = getResources();
                    try {
                        myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                        play_pause.setBackground(myDrawable);
                    } catch (Exception ex) {
                        Log.e("Error", "Exception loading drawable");
                    }
                }

            }
        });


        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(musiclist_activity.this,musicplayer.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
