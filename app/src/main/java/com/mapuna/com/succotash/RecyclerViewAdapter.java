package com.mapuna.com.succotash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import static com.mapuna.com.succotash.R.color.colorPrimaryDark;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    Context mctx;
    ArrayList<File>musicfiles;
    importantelements ie=new importantelements();
    MediaMetadataRetriever metadataRetriever;
    CustomItemClickListener listener;


    public RecyclerViewAdapter(Context mctx,ArrayList<File>musicfiles,CustomItemClickListener listener) {
        this.mctx=mctx;
        this.musicfiles=musicfiles;
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycustomlist,viewGroup,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        metadataRetriever =new MediaMetadataRetriever();
        metadataRetriever.setDataSource(musicfiles.get(i).getAbsolutePath());

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
        viewHolder.musicname.setText(musicfiles.get(i).getName().replace(".mp3",""));
        viewHolder.musicname.setTypeface(musicfont);
        Log.d("onBindcalled", "onBindViewHolder: ok ");

        if(ie.currentpos==i){
            viewHolder.rt.setBackgroundColor(mctx.getResources().getColor(R.color.colorPrimary));
            viewHolder.musicname.setTextColor(mctx.getResources().getColor(R.color.white));
        }
        else{
            viewHolder.rt.setBackgroundColor(mctx.getResources().getColor(R.color.white));
            viewHolder.musicname.setTextColor(mctx.getResources().getColor(R.color.black));
        }
        viewHolder.rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClick(i);
                ie.currentpos=i;
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
            musicname=(TextView)itemView.findViewById(R.id.songname_id);
            rt=(RelativeLayout)itemView.findViewById(R.id.list_btn);
            artistname=(TextView)itemView.findViewById(R.id.artistname);
            art=(ImageView)itemView.findViewById(R.id.art);
        }
    }


    public interface CustomItemClickListener {
        public void onItemClick(int position);
    }


}
