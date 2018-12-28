package com.mapuna.com.succotash;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    RelativeLayout rt;
    File file=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rt=(RelativeLayout)findViewById(R.id.homelay);

        permission();

        stopService(new Intent(this,MyService.class));
    }

    public void permission () {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                        Intent i3 = new Intent(MainActivity.this, musiclist_activity.class);
                        startActivity(i3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }, 1000);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startService(new Intent(this,MyService.class));
    }


}
