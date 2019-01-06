package com.mapuna.com.succotash.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapuna.com.succotash.R;
import com.mapuna.com.succotash.importantElements;

import java.io.File;
import java.util.ArrayList;

public class playlistadapter  extends RecyclerView.Adapter<playlistadapter.ViewHolder> {
    private Context mctx;
    CustomItemClickListener listener;

    public playlistadapter(Context mctx , CustomItemClickListener listener) {
        this.mctx=mctx;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.playlistlist,viewGroup,false);
        return new playlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.playlistNum.setText("Playlist "+(i+1));
        viewHolder.rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return importantElements.playlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView playlistNum;
        RelativeLayout rt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            playlistNum=itemView.findViewById(R.id.playlistnumber);
            rt=itemView.findViewById(R.id.selectplaylist);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(int position);
    }
}
