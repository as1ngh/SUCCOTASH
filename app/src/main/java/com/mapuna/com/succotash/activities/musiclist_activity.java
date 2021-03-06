package com.mapuna.com.succotash.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mapuna.com.succotash.MyService;
import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.receiver.SleepTimerReceiver;
import com.mapuna.com.succotash.adapters.ViewPageAdapter;
import com.mapuna.com.succotash.fragments.TimePickerFragment;
import com.mapuna.com.succotash.fragments.fragmentrecent;
import com.mapuna.com.succotash.fragments.fragmentplaylist;
import com.mapuna.com.succotash.fragments.fragmentmusiclist;
import com.mapuna.com.succotash.importantElements;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class musiclist_activity extends AppCompatActivity implements fragmentmusiclist.gotinput,fragmentrecent.gotinput2,fragmentplaylist.gotinput3,TimePickerDialog.OnTimeSetListener{


    TabLayout tabLayout;
    AppBarLayout appBarLayout;
    ViewPager viewPager;
    TextView musicName;
    Button play_pause;
    Button timer;
    fragmentmusiclist fragMusicList;
    fragmentrecent fragAlbum;
    fragmentplaylist fragPlaylist;
    ImageView art;
    RelativeLayout music;
    MediaMetadataRetriever metadataRetriever;
    TextView time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist_activity);

        loadDate();

        stopService(new Intent(this,MyService.class));
        if(importantElements.mp!=null){
            importantElements.notificationManager.cancelAll();
        }

        tabLayout= findViewById(R.id.tablayout_id);
        appBarLayout=findViewById(R.id.appbarid);
        viewPager=findViewById(R.id.viewpager_id);
        musicName =findViewById(R.id.musicname_id);
        play_pause=findViewById(R.id.play_pause_id);
        music=findViewById(R.id.upplayer);
        art=findViewById(R.id.listart);
        timer=findViewById(R.id.setTimer);
        time=findViewById(R.id.time);

        music.setVisibility(View.INVISIBLE);
        Typeface musicfont=Typeface.createFromAsset(getAssets(),"fonts/Raleway-Light.ttf");
        musicName.setTypeface(musicfont);

        //Set viewPager adapter with fragments
        fragMusicList =new fragmentmusiclist();
        fragAlbum =new fragmentrecent();
        fragPlaylist=new fragmentplaylist();
        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(fragMusicList,"music list");
        adapter.AddFragment(fragAlbum,"recently played");
        adapter.AddFragment(fragPlaylist," Playlist");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //To place music data on activity on first run if mediaplayer is not null
         if(importantElements.mp!=null) {
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
                   getnextsong();
                }
            });

        }

        //PLAY/PAUSE BUTTON
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


         //CLICK TO START MUSIC PLAYER ACTIVITY
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(importantElements.mp ==null)
                    Toast.makeText(getApplicationContext(), "No Music Selected", Toast.LENGTH_LONG).show();
                else
                     startActivity(new Intent(getApplicationContext(),musicplayer.class));
            }
        });

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });

    }


    //Function for getting next song with some conditions like shuffle and looping
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
            }
            else{
                importantElements.currentpos =0;
                importantElements.mp.stop();
                importantElements.mp.release();
                importantElements.mp =MediaPlayer.create(getApplicationContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                importantElements.mp.start();
            }
        }
        update(importantElements.currentpos);
        importantElements.recently.add(0, importantElements.currentpos);
        importantElements.recently =removeDuplicates(importantElements.recently);
        fragAlbum.adapter.notifyDataSetChanged();
        fragMusicList.adapter.notifyDataSetChanged();
    }


    //To update the activity according to current music playing
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
                getnextsong();
            }
        });
    }

    //To update Recently listened list of song so that no one is repeated
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
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



    //To get update from various fragments
    @Override
    public void getupdate(int i) {
        music.setVisibility(View.VISIBLE);
        update(i);
        importantElements.recently.add(0,i);
        importantElements.recently =removeDuplicates(importantElements.recently);
        fragAlbum.adapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(), "No Music Selected"+i, Toast.LENGTH_LONG).show();
    }

    //To set action when recyclerView scrolled up
    @Override
    public void scrollup() {
        if(importantElements.mp!=null)
        music.setVisibility(View.VISIBLE);
    }

    //To set action when recyclerView scrolled down
    @Override
    public void scrolldown() {
        if(importantElements.mp!=null)
        music.setVisibility(View.INVISIBLE);

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if(importantElements.mp!=null){
            importantElements.notificationManager.cancelAll();
        }
        importantElements.recently =removeDuplicates(importantElements.recently);
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
                    getnextsong();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        savedata(importantElements.playlist);
        //saverecent(importantElements.recently);
        startService(new Intent(this,MyService.class));
    }

    @Override
    protected void onStop() {
        savedata(importantElements.playlist);
        //saverecent(importantElements.recently);
        startService(new Intent(this,MyService.class));
        super.onStop();
    }


    //called when time is set
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c=Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        c.set(Calendar.SECOND,0);
        startalarm(c);
        setTimer(c);
    }


    //To save the updated playlist from this activity
    public void savedata(ArrayList<ArrayList<Integer>> playlist){
        String aString;
        aString = "";
        int column;
        int row;

        for (row = 0; row < playlist.size(); row++) {
            for (column = 0; column < playlist.get(row).size(); column++ ) {
                if(column==playlist.get(row).size()-1){
                    aString = aString + playlist.get(row).get(column) ;
                }
                else{
                    aString = aString + playlist.get(row).get(column) + ",";
                }
            }
            if(row==playlist.size()-1){
                aString = aString + "";
            }
            else {
                aString = aString + ";";
            }
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("key_name", aString); // Storing string
        editor.commit();
    }

    //To load playlist from shared preference
    public void loadDate(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        String found=pref.getString("key_name", null); // getting String
       if(found==null ||found==""){

       }
       else{
           importantElements.playlist=new ArrayList<>();
           String row[]=found.split(";");
           int width=row.length;
           for(int i=0;i<width;i++){
               ArrayList<Integer>oneplay=new ArrayList<>();
               String cells[]=row[i].split(",");
               int hieght=cells.length;
               for(int j=0;j<hieght;j++){
                   int test =Integer.valueOf(cells[j]);
                   oneplay.add(test);

               }
               importantElements.playlist.add(oneplay);
           }
       }
    }



    //To set time to close the app after few interval
    private void startalarm(Calendar c){
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,SleepTimerReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,7,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }

    private void setTimer(Calendar c){
        time.setVisibility(View.VISIBLE);
        String timestr="";
        timestr+=DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        time.setText(timestr);

    }
}
