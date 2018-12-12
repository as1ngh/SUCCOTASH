package com.mapuna.com.succotash;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class musiclist_activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musiclist_activity);

        tabLayout=(TabLayout)findViewById(R.id.tablayout_id);
        appBarLayout=(AppBarLayout)findViewById(R.id.appbarid);
        viewPager=(ViewPager)findViewById(R.id.viewpager_id);

        ViewPageAdapter adapter=new ViewPageAdapter(getSupportFragmentManager());
        adapter.AddFragment(new fragmentmusiclist(),"music_list");
        adapter.AddFragment(new fragmentalbum(),"album_list");
        adapter.AddFragment(new fragmentartist(),"artist_list");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
