package com.mapuna.com.succotash;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class customlistadapter extends ArrayAdapter<File> {
    Context context;
    int resource;
    ArrayList<File> musicfile;


    public customlistadapter(Context context, int resource , ArrayList<File> musicfile) {
        super(context, resource,musicfile);
        this.context=context;
        this.resource=resource;
        this.musicfile=musicfile;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.mycustomlist,null);
        TextView musicname=view.findViewById(R.id.songname_id);
        File file=musicfile.get(position);
        musicname.setText(file.getName());
        return view;
    }
}
