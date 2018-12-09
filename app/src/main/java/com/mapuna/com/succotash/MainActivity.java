package com.mapuna.com.succotash;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends AppCompatActivity {

    Button b;
    ImageView i;
    com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper rt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rt=(com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper)findViewById(R.id.custom_switcher);
        b=(Button)findViewById(R.id.getst);
        i=(ImageView)findViewById(R.id.logo);

        final Pair p[]=new Pair[2];
                p[0]=new Pair<View, String>(rt,"bth");
                p[1]=new Pair<View, String>(i,"log");



                //-->FOR ANIMATION AND ACTIVITY CHANGE
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,p);
                startActivity(new Intent(MainActivity.this,secondactivity.class),options.toBundle());
                //TastyToast.makeText(getApplicationContext(), "UNDER BUILD!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }
        });

        //-->FOR ROTATING TEXT
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Reckoner_Bold.ttf");

        RotatingTextWrapper rotatingTextWrapper = (RotatingTextWrapper) findViewById(R.id.custom_switcher);
        rotatingTextWrapper.setSize(50);

        Rotatable rotatable = new Rotatable(Color.parseColor("#FFA036"), 1000, "SUCCOTASH", "DROID", "WARS");
        rotatable.setSize(55);
        rotatable.setAnimationDuration(500);
        rotatable.setTypeface(typeface);
        rotatable.setInterpolator(new BounceInterpolator());
        rotatable.setCenter(true);

        rotatingTextWrapper.setContent("?", rotatable);
    }
}
