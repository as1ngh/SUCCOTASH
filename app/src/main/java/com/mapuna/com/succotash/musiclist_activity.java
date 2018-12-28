package com.mapuna.com.succotash;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class musiclist_activity extends AppCompatActivity implements fragmentmusiclist.gotinput{

    importantelements ie=new importantelements();

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    TextView musicname;
    Button play_pause;
    fragmentmusiclist fragmusiclist;
    ImageView art;
    RelativeLayout music;
    MediaMetadataRetriever metadataRetriever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist_activity);


        stopService(new Intent(this,MyService.class));


        tabLayout=(TabLayout)findViewById(R.id.tablayout_id);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);
        musicname=(TextView)findViewById(R.id.musicname_id);
        play_pause=(Button)findViewById(R.id.play_pause_id);
        music=(RelativeLayout)findViewById(R.id.upplayer);
        art=(ImageView)findViewById(R.id.listart);

        Typeface musicfont=Typeface.createFromAsset(getAssets(),"fonts/Raleway-Light.ttf");
        musicname.setTypeface(musicfont);

        fragmusiclist=new fragmentmusiclist();
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmusiclist,"music list");
        adapter.AddFragment(new fragmentalbum(),"album list");
        adapter.AddFragment(new fragmentartist(),"artist list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.d("fragment", "onCreate:implement ");

        if(ie.mp!=null) {
            musicname.setText(ie.mysongs.get(ie.currentpos).getName().replace(".mp3", ""));
            if(ie.mp.isPlaying()) {
                final Drawable myDrawable;
                Resources res = getResources();
                try {
                    myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.pause));
                    play_pause.setBackground(myDrawable);
                } catch (Exception ex) {
                    Log.e("Error", "Exception loading drawable");
                }
            }
            else{
                final Drawable myDrawable;
                Resources res = getResources();
                try {
                    myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                    play_pause.setBackground(myDrawable);
                } catch (Exception ex) {
                    Log.e("Error", "Exception loading drawable");
                }
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
                    }
                    else{
                        ie.currentpos=0;
                        ie.mp.stop();
                        ie.mp.release();
                        ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                        ie.mp.start();
                    }
                    startActivity(new Intent(musiclist_activity.this,musiclist_activity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });

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



        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ie.mp==null)
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                else
                     startActivity(new Intent(getApplicationContext(),musicplayer.class));
            }
        });


    }




    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
    }


    @Override
    public void getupdate(int i) {
        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(ie.mysongs.get(i).getAbsolutePath());

        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        else{
            art.setImageDrawable(this.getResources().getDrawable(R.drawable.headphones));
        }

        ie.currentpos=i;
        if(ie.mp!=null){
            ie.mp.stop();
            ie.mp.release();
            ie.mp=null;
        }
        ie.mp=MediaPlayer.create(this,Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
        ie.mp.start();
        musicname.setText(ie.mysongs.get(ie.currentpos).getName().replace(".mp3", ""));
        if(ie.mp.isPlaying()) {
            final Drawable myDrawable;
            Resources res = getResources();
            try {
                myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.pause));
                play_pause.setBackground(myDrawable);
            } catch (Exception ex) {
                Log.e("Error", "Exception loading drawable");
            }
        }
        else{
            final Drawable myDrawable;
            Resources res = getResources();
            try {
                myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                play_pause.setBackground(myDrawable);
            } catch (Exception ex) {
                Log.e("Error", "Exception loading drawable");
            }
        }
        //Toast.makeText(getApplicationContext(), "No Music Selected"+i, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(ie.mysongs.get(ie.currentpos).getAbsolutePath());

        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        else{
            art.setImageDrawable(this.getResources().getDrawable(R.drawable.headphones));
        }

        musicname.setText(ie.mysongs.get(ie.currentpos).getName().replace(".mp3", ""));
        if(ie.mp.isPlaying()) {
            final Drawable myDrawable;
            Resources res = getResources();
            try {
                myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.pause));
                play_pause.setBackground(myDrawable);
            } catch (Exception ex) {
                Log.e("Error", "Exception loading drawable");
            }
        }
        else{
            final Drawable myDrawable;
            Resources res = getResources();
            try {
                myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                play_pause.setBackground(myDrawable);
            } catch (Exception ex) {
                Log.e("Error", "Exception loading drawable");
            }
        }
        fragmusiclist.adapter.notifyDataSetChanged();
    }
}
