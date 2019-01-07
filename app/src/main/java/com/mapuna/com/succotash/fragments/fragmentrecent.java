package com.mapuna.com.succotash.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.importantElements;
import com.mapuna.com.succotash.adapters.recentrecyclerviewadapter;

public class fragmentrecent extends Fragment {
    View view;
    RecyclerView musicnames;
    public recentrecyclerviewadapter adapter;
    gotinput2 got;

    public fragmentrecent() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.album_fragment,container,false);

        musicnames= view.findViewById(R.id.recent);
        musicnames.addOnScrollListener(new CustomScrollListener());

        adapter=new recentrecyclerviewadapter(getActivity(), importantElements.recently, new recentrecyclerviewadapter.onclick() {
            @Override
            public void onClick(int pos) {
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
        void getupdate(int i);
        void scrollup();
        void scrolldown();
    }

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
