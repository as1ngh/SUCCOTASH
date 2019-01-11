package com.mapuna.com.succotash.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.adapters.RecyclerViewAdapter;
import com.mapuna.com.succotash.importantElements;

import java.io.File;
import java.util.ArrayList;

public class fragmentmusiclist extends Fragment  {
    View view;
    RecyclerView musicnames;
    gotinput got;
    public RecyclerViewAdapter adapter;
    ProgressDialog dialog;

    public fragmentmusiclist() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.musiclist_fragment,container,false);

        musicnames= view.findViewById(R.id.musicl);
        musicnames.addOnScrollListener(new CustomScrollListener());
        Asynctask task =new Asynctask();
        dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        task.execute();

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




    //TO FIND ALL SONG FILES
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

    //INTERFACE TO DETECT INPUT FROM RECYCLERVIEW
    public interface gotinput{
        void getupdate(int i);
        void scrollup();
        void scrolldown();
    }

    //ASYNCTASK TO PERFORM SEARCHING IN DIFFERENT THREAD
    public class Asynctask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            importantElements.mysongs =findsong(Environment.getExternalStorageDirectory());
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

            adapter=new RecyclerViewAdapter(getActivity(), importantElements.mysongs, new RecyclerViewAdapter.CustomItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    got.getupdate(position);
                }
            });
            musicnames.setAdapter(adapter);
            musicnames.setLayoutManager(new LinearLayoutManager(getActivity()));
            dialog.hide();
        }
    }

    //TO GET INFO ABOUT SCROLLING OF RECYCLER VIEW
    public class CustomScrollListener extends RecyclerView.OnScrollListener {
        public CustomScrollListener() {
        }

        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    System.out.println("The RecyclerView is not scrolling");
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    System.out.println("Scrolling now");
                    break;
                case RecyclerView.SCROLL_STATE_SETTLING:
                    System.out.println("Scroll Settling");
                    break;

            }

        }

        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dx > 0) {
                System.out.println("Scrolled Right");
            } else if (dx < 0) {
                System.out.println("Scrolled Left");
            } else {
                System.out.println("No Horizontal Scrolled");
            }

            if (dy > 0) {
                got.scrolldown();
            } else if (dy < 0) {
                got.scrollup();
            } else {
                System.out.println("No Vertical Scrolled");
            }
        }
    }





}
