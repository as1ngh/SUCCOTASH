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
import com.mapuna.com.succotash.importantelements;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class musiclist_activity extends AppCompatActivity implements fragmentmusiclist.gotinput , fragmentalbum.gotinput2{

    importantelements ie=new importantelements();

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;
    TextView musicname;
    Button play_pause;
    fragmentmusiclist fragmusiclist;
    fragmentalbum fragalbum;
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

        music.setVisibility(View.INVISIBLE);

        Typeface musicfont=Typeface.createFromAsset(getAssets(),"fonts/Raleway-Light.ttf");
        musicname.setTypeface(musicfont);

        fragmusiclist=new fragmentmusiclist();
        fragalbum=new fragmentalbum();
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragmusiclist,"music list");
        adapter.AddFragment(fragalbum,"recently played");
        adapter.AddFragment(new fragmentartist(),"artist list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        Log.d("fragment", "onCreate:implement ");

        /* if(ie.mp!=null) {
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
                    update(ie.currentpos);
                }
            });

        } */

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


    void update(int i){
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

        ie.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(ie.currentpos!=ie.mysongs.size()-1){
                    ie.currentpos=ie.currentpos+1;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.recently.add(0,ie.currentpos);
                    ie.recently=removeDuplicates(ie.recently);
                    ie.mp.start();
                }
                else{
                    ie.currentpos=0;
                    ie.mp.stop();
                    ie.mp.release();
                    ie.mp=MediaPlayer.create(getApplicationContext(),Uri.parse(ie.mysongs.get(ie.currentpos).getAbsolutePath()));
                    ie.mp.start();
                    ie.recently.add(0,ie.currentpos);
                    ie.recently=removeDuplicates(ie.recently);
                }
                update(ie.currentpos);
                fragmusiclist.adapter.notifyDataSetChanged();

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
        ie.recently.add(0,i);
        ie.recently=removeDuplicates(ie.recently);


        fragalbum.adapter.notifyDataSetChanged();
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
        fragalbum.adapter.notifyDataSetChanged();
        if(ie.mp!=null){
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
                    fragmusiclist.adapter.notifyDataSetChanged();

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
