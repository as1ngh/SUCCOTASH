package com.mapuna.com.succotash.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.adapters.playlistadapter;
import com.mapuna.com.succotash.importantElements;

import java.util.ArrayList;

public class fragmentplaylist extends Fragment {
    View view;
    FloatingActionButton addplay;
    RecyclerView playls;
    playlistadapter adapter;
    gotinput3 got;
    public fragmentplaylist() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.artist_fragment,container,false);
        addplay=view.findViewById(R.id.addplaylist);
        playls=view.findViewById(R.id.playlist_id);

        ArrayList<String>getsongs=new ArrayList<>();
        for(int i=0;i<importantElements.mysongs.size();i++){
            getsongs.add(importantElements.mysongs.get(i).getName());
        }
        final String[]getsong=new String[getsongs.size()];
        for(int i=0;i<getsongs.size();i++){
            getsong[i]=importantElements.mysongs.get(i).getName();
        }
        final boolean[]checkeditems=new boolean[getsongs.size()];

        addplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<Integer>toadd=new ArrayList<>();
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                mBuilder.setTitle("Add Music");
                mBuilder.setIcon(getResources().getDrawable(R.drawable.headphones));
                mBuilder.setMultiChoiceItems(getsong, checkeditems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            //mUserItems.add(position);
                            toadd.add(position);

                        }else{
                            toadd.remove((Integer.valueOf(position)));
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        importantElements.playlist.add(toadd);
                        for (int pos = 0; pos < checkeditems.length; pos++) {
                            checkeditems[pos] = false;
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

                mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int pos = 0; pos < checkeditems.length; pos++) {
                            checkeditems[pos] = false;
                        }
                        dialogInterface.dismiss();
                    }
                });

                mBuilder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        for (int i = 0; i < checkeditems.length; i++) {
                            checkeditems[i] = false;
                        }
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();




            }
        });


        adapter=new playlistadapter(getActivity(), new playlistadapter.CustomItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose a song from playlist");
                builder.setIcon(getResources().getDrawable(R.drawable.headphones));
                String[]songs=new String[importantElements.playlist.get(position).size()];
                for(int i=0;i<importantElements.playlist.get(position).size();i++){
                    songs[i]=importantElements.mysongs.get(importantElements.playlist.get(position)
                            .get(i)).getName().replace(".mp3","");
                }
                builder.setItems(songs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(importantElements.mp!=null){
                            importantElements.mp.stop();
                            importantElements.mp.release();
                            importantElements.mp=null;
                        }
                        importantElements.currentpos=importantElements.playlist.get(position).get(which);
                        importantElements.mp =MediaPlayer.create(getContext(),Uri.parse(importantElements.mysongs.get(importantElements.currentpos).getAbsolutePath()));
                        importantElements.mp.start();
                        got.getupdate(importantElements.currentpos);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();




            }
        });
        playls.setAdapter(adapter);
        playls.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof gotinput3) {
            got = (gotinput3) context;
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

    public interface gotinput3{
        void getupdate(int i);
    }

}
