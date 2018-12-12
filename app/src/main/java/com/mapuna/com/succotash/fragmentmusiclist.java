package com.mapuna.com.succotash;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class fragmentmusiclist extends Fragment  {
    View view;

    public fragmentmusiclist() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.musiclist_fragment,container,false);
        ListView musicnames;
        musicnames=(ListView) view.findViewById(R.id.musicl);

        final ArrayList<File> mysongs=findsong(Environment.getExternalStorageDirectory());
        customlistadapter adapter=new customlistadapter(getActivity(),R.layout.mycustomlist,mysongs);

        musicnames.setAdapter(adapter);
        return view;
    }


    public ArrayList<File>findsong(File file){
        ArrayList<File> arrayList=new ArrayList<>();

        File[] files=file.listFiles();
        for(File singlefile:files){
            if(singlefile.isDirectory() && !singlefile.isHidden())
            arrayList.addAll(findsong(singlefile));
            else {
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav"))
                    arrayList.add(singlefile);
            }
        }
        return arrayList;
    }

    //String[]items;




}
