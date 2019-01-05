package com.mapuna.com.succotash.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.mapuna.com.succotash.MyService;
import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.adapters.ViewPageAdapter;
import com.mapuna.com.succotash.fragments.fragmentalbum;
import com.mapuna.com.succotash.fragments.fragmentartist;
import com.mapuna.com.succotash.fragments.fragmentmusiclist;
import com.mapuna.com.succotash.importantElements;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class musiclist_activity extends AppCompatActivity implements fragmentmusiclist.gotinput , fragmentalbum.gotinput2{


    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    TextView musicName;
    Button play_pause;
    fragmentmusiclist fragMusicList;
    fragmentalbum fragAlbum;
    ImageView art;
    RelativeLayout music;
    MediaMetadataRetriever metadataRetriever;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist_activity);


        stopService(new Intent(this,MyService.class));


        tabLayout= findViewById(R.id.tablayout_id);
        appBarLayout=findViewById(R.id.appbarid);
        viewPager=findViewById(R.id.viewpager_id);
        musicName =findViewById(R.id.musicname_id);
        play_pause=findViewById(R.id.play_pause_id);
        music=findViewById(R.id.upplayer);
        art=findViewById(R.id.listart);

        music.setVisibility(View.INVISIBLE);

        Typeface musicfont=Typeface.createFromAsset(getAssets(),"fonts/Raleway-Light.ttf");
        musicName.setTypeface(musicfont);

        fragMusicList =new fragmentmusiclist();
        fragAlbum =new fragmentalbum();
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragMusicList,"music list");
        adapter.AddFragment(fragAlbum,"recently played");
        adapter.AddFragment(new fragmentartist(),"artist list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        /* if(ie.mp!=null) {
            musicName.setText(ie.mysongs.get(ie.currentpos).getName().replace(".mp3", ""));
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
                    update(ie.currentpos);
                }
            });

        } */

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(importantElements.mp ==null){
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                }
                else if(importantElements.mp.isPlaying()){
                    importantElements.mp.pause();
                    final Drawable myDrawable;
                    Resources res = getResources();
                    try {
                        myDrawable = Drawable.createFromXml(res, res.getXml(R.xml.play));
                        play_pause.setBackground(myDrawable);
                    } catch (Exception ex) {
                        Log.e("Error", "Exception loading drawable");
                    }

                }
                else if(!importantElements.mp.isPlaying() && importantElements.mp !=null){
                    importantElements.mp.start();
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
                if(importantElements.mp ==null)
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                else
                     startActivity(new Intent(getApplicationContext(),musicplayer.class));
            }
        });


    }


    void update(int i){
        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(importantElements.mysongs.get(i).getAbsolutePath());

        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            art.setImageBitmap(songImage);
        }
        else{
            art.setImageDrawable(this.getResources().getDrawable(R.drawable.headphones));
        }

        importantElements.currentpos =i;
        if(importantElements.mp !=null){
            importantElements.mp.stop();
            importantElements.mp.release();
            importantElements.mp =null;
        }
        importantElements.mp =MediaPlayer.create(this,Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
        importantElements.mp.start();
        musicName.setText(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3", ""));
        if(importantElements.mp.isPlaying()) {
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

        importantElements.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(importantElements.currentpos != importantElements.mysongs.size()-1){
                    importantElements.currentpos = importantElements.currentpos +1;
                    importantElements.mp.stop();
                    importantElements.mp.release();
                    importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                    importantElements.recently.add(0, importantElements.currentpos);
                    importantElements.recently =removeDuplicates(importantElements.recently);
                    importantElements.mp.start();
                }
                else{
                    importantElements.currentpos =0;
                    importantElements.mp.stop();
                    importantElements.mp.release();
                    importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                    importantElements.mp.start();
                    importantElements.recently.add(0, importantElements.currentpos);
                    importantElements.recently =removeDuplicates(importantElements.recently);
                }
                update(importantElements.currentpos);
                fragMusicList.adapter.notifyDataSetChanged();

            }
        });
    }

    public static <T> List<T> removeDuplicates(List<T> list)
    {
        // Create a new LinkedHashSet
        Set<T> set = new LinkedHashSet<>();

        // Add the elements to set
        set.addAll(list);

        // Clear the list
        list.clear();

        // add the elements of set
        // with no duplicates to the list
        list.addAll(set);

        // return the list
        return list;
    }



    @Override
    public void getupdate(int i) {
        music.setVisibility(View.VISIBLE);
        update(i);
        importantElements.recently.add(0,i);
        importantElements.recently =removeDuplicates(importantElements.recently);


        fragAlbum.adapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(), "No Music Selected"+i, Toast.LENGTH_LONG).show();
    }

    @Override
    public void scrollup() {
        music.setVisibility(View.VISIBLE);
    }

    @Override
    public void scrolldown() {
        music.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        fragAlbum.adapter.notifyDataSetChanged();
        if(importantElements.mp !=null){
            metadataRetriever =new MediaMetadataRetriever();
            metadataRetriever.setDataSource(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath());

            if(metadataRetriever.getEmbeddedPicture()!=null){
                Bitmap songImage = BitmapFactory
                        .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
                art.setImageBitmap(songImage);
            }
            else{
                art.setImageDrawable(this.getResources().getDrawable(R.drawable.headphones));
            }

            musicName.setText(importantElements.mysongs.get(importantElements.currentpos).getName().replace(".mp3", ""));
            if(importantElements.mp.isPlaying()) {
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
            fragMusicList.adapter.notifyDataSetChanged();

            importantElements.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(importantElements.currentpos != importantElements.mysongs.size()-1){
                        importantElements.currentpos = importantElements.currentpos +1;
                        importantElements.mp.stop();
                        importantElements.mp.release();
                        importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                        importantElements.mp.start();
                    }
                    else{
                        importantElements.currentpos =0;
                        importantElements.mp.stop();
                        importantElements.mp.release();
                        importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                        importantElements.mp.start();
                    }
                    update(importantElements.currentpos);
                    fragMusicList.adapter.notifyDataSetChanged();

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(this,MyService.class));
    }





}
