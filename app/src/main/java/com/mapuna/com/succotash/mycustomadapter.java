package com.mapuna.com.succotash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class mycustomadapter extends ArrayAdapter<File> {

    Context mctx;
    int resource;
    ArrayList<File>songl;
    public mycustomadapter(Context mctx, int resource, ArrayList<File>songl){
        super(mctx,resource,songl);

        this.mctx=mctx;
        this.resource=resource;
        this.songl=songl;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(mctx);
        View view =inflater.inflate(R.layout.customlayout,null);

        TextView songname=view.findViewById(R.id.songn);

        File song=songl.get(position);
        songname.setText(song.getName());

        return view;

    }
}
