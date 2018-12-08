package com.mapuna.com.succotash;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class secondactivity extends AppCompatActivity {
    TextView head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondactivity);

        head=(TextView)findViewById(R.id.head);
        Typeface headf=Typeface.createFromAsset(getAssets(),"fonts/Reckoner_Bold.ttf");
        head.setTypeface(headf);
    }
}
