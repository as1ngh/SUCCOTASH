package com.mapuna.com.succotash;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;
import static android.content.Intent.FLAG_ACTIVITY_NO_USER_ACTION;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
import static android.content.Intent.FLAG_ACTIVITY_RETAIN_IN_RECENTS;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class fragmentmusiclist extends Fragment  {
    View view;
    RecyclerView musicnames;
    importantelements ie=new importantelements();
    gotinput got;
    RecyclerViewAdapter adapter;

    public fragmentmusiclist() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.musiclist_fragment,container,false);

        musicnames= (RecyclerView) view.findViewById(R.id.musicl);

        ie.mysongs=findsong(Environment.getExternalStorageDirectory());
        adapter=new RecyclerViewAdapter(getActivity(), ie.mysongs, new RecyclerViewAdapter.CustomItemClickListener() {
            @Override
            public void onItemClick(int position) {
                got.getupdate(position);

            }
        });
        musicnames.setAdapter(adapter);
        musicnames.setLayoutManager(new LinearLayoutManager(getActivity()));



       /* musicnames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(ie.mp!=null){
                    ie.mp.stop();
                    ie.mp=null;
                }
                File file=ie.mysongs.get(position);
                ie.mp = MediaPlayer.create(getActivity(), Uri.parse(file.getAbsolutePath()));
                ie.currentpos=position;
                ie.mp.start();
                startActivity(new Intent(getActivity(),musiclist_activity.class).putExtra("filename",file).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });*/
        Log.d("adapter", "onCreateView:setteled ");

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof gotinput) {
            got = (gotinput) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        got = null;
    }

    public interface gotinput{
        public void getupdate(int i);
    }



}
