package com.mapuna.com.succotash.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
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

import java.util.ArrayList;
import java.util.List;

public class recentrecyclerviewadapter extends RecyclerView.Adapter<recentrecyclerviewadapter.ViewHolder>{
    private Context mctx;
    private ArrayList<Integer>musicfiles;
    private onclick listener;

    public recentrecyclerviewadapter(Context mctx,ArrayList<Integer> musicfiles , onclick listener) {
        this.mctx=mctx;
        this.musicfiles=musicfiles;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycustomlist,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        MediaMetadataRetriever metadataRetriever;
        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(importantElements.mysongs.get(musicfiles.get(i)).getAbsolutePath());

        if( metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)==null){
            viewHolder.artistname.setText("<unknown>");
        }
        else {
            viewHolder.artistname.setText(metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }

        if(metadataRetriever.getEmbeddedPicture()!=null){
            Bitmap songImage = BitmapFactory
                    .decodeByteArray(metadataRetriever.getEmbeddedPicture(), 0, metadataRetriever.getEmbeddedPicture().length);
            viewHolder.art.setImageBitmap(songImage);
        }
        else{
            viewHolder.art.setImageDrawable(mctx.getResources().getDrawable(R.drawable.headphones));
        }


        Typeface musicfont=Typeface.createFromAsset(mctx.getAssets(),"fonts/Quicksand-Regular.ttf");
        viewHolder.musicname.setText(importantElements.mysongs.get(musicfiles.get(i)).getName().replace(".mp3",""));
        viewHolder.musicname.setTypeface(musicfont);

        viewHolder.rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importantElements.currentpos =musicfiles.get(i);
                listener.onClick(importantElements.currentpos);
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return musicfiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicname;
        TextView artistname;
        RelativeLayout rt;
        ImageView art;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicname= itemView.findViewById(R.id.songname_id);
            rt= itemView.findViewById(R.id.list_btn);
            artistname= itemView.findViewById(R.id.artistname);
            art= itemView.findViewById(R.id.art);
        }
    }


    public interface onclick{
        void onClick(int pos);
    }



}
