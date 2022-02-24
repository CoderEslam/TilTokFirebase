package com.doubleclick.tiktok.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doubleclick.tiktok.Model.MediaObject;
import com.doubleclick.tiktok.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoViewHolder> {

    public DemoAdapter(List<MediaObject> mediaObjectList, Context context) {
        this.mediaObjectList = mediaObjectList;
        this.context = context;
    }

    //http://www.rojkharido.com/tiktok/posts.php

    List<MediaObject> mediaObjectList;
    Context context;

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_layout, parent, false);
        return new DemoViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {

        MediaObject mediaObject = mediaObjectList.get(position);
        Picasso.get().load(R.drawable.spin_black_cd).into(holder.song_cd);

    }

    @Override
    public int getItemCount() {
        return mediaObjectList.size();
    }

    public class DemoViewHolder extends RecyclerView.ViewHolder {

        private ImageView song_cd;

        public DemoViewHolder(@NonNull View itemView) {
            super(itemView);

            song_cd = itemView.findViewById(R.id.spinCd);
            Picasso.get().load(R.drawable.spin_black_cd).into(song_cd);

        }
    }
}
