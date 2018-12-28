package com.mapuna.com.succotash;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fragmentalbum extends Fragment {
    View view;
    RecyclerView musicnames;
    recentrecyclerviewadapter adapter;
    importantelements ie;
    gotinput2 got;

    public fragmentalbum() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.album_fragment,container,false);

        musicnames= (RecyclerView) view.findViewById(R.id.recent);

        adapter=new recentrecyclerviewadapter(getActivity(), ie.recently, new recentrecyclerviewadapter.onclick() {
            @Override
            public void onclick(int pos) {
                got.getupdate(pos);

            }
        });
        musicnames.setAdapter(adapter);
        musicnames.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof gotinput2) {
            got = (gotinput2) context;
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

    public interface gotinput2{
        public void getupdate(int i);
    }




}
