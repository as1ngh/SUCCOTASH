package com.mapuna.com.succotash;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import com.sdsmdg.harjot.rotatingtext.RotatingTextWrapper;
import com.sdsmdg.harjot.rotatingtext.models.Rotatable;
import com.sdsmdg.tastytoast.TastyToast;

public class MainActivity extends AppCompatActivity {

    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b=(Button)findViewById(R.id.getst);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TastyToast.makeText(getApplicationContext(), "UNDER BUILD!", TastyToast.LENGTH_LONG, TastyToast.WARNING);
            }
        });

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
