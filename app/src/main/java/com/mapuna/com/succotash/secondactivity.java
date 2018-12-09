package com.mapuna.com.succotash;

import android.Manifest;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class secondactivity extends AppCompatActivity {
    TextView head;
    ListView songname;
    String[] songl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);

        head=(TextView)findViewById(R.id.head);
        Typeface headf=Typeface.createFromAsset(getAssets(),"fonts/Reckoner_Bold.ttf");
        head.setTypeface(headf);

        songname=(ListView)findViewById(R.id.songn);

        permission();


    }

    //-->FOR PERMISSION
    public void permission () {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
    //-->FINDING SONGS AND STORING IN ARRAY LIST
    public ArrayList<File> songf(File file){
        ArrayList<File> songl=new ArrayList<>();
        File afiles[]=file.listFiles();
        for(File singleFile:afiles){
            if(singleFile.isDirectory() && !singleFile.isHidden()){
                songl.addAll(songf(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp4") || singleFile.getName().endsWith(".wav")){
                    songl.add(singleFile);
                }
            }
        }
        return songl;
    }

    public void display(){
        final ArrayList<File> songs = songf(Environment.getExternalStorageDirectory());
        mycustomadapter adapter=new mycustomadapter(this,R.layout.customlayout,songs);
        songname.setAdapter(adapter);
    }



}
