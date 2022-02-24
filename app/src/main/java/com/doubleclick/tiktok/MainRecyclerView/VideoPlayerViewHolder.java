package com.doubleclick.tiktok.MainRecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.doubleclick.tiktok.Model.MediaObject;
import com.doubleclick.tiktok.Model.Responses.RetrivingData;
import com.doubleclick.tiktok.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoPlayerViewHolder extends RecyclerView.ViewHolder {

    FrameLayout media_container;
    TextView title;//,name,no_likes,no_comments;
    ImageView thumbnail, volumeControl,shareBtn,likeBtn,commentBtn,likeAnimate,download,soundDisk;
    ProgressBar progressBar;
    View parent;
    RequestManager requestManager;
    ;
    ImageView followBtn;
    CircleImageView profile_img;

    public VideoPlayerViewHolder(@NonNull View itemView) {
        super(itemView);
        parent = itemView;
        media_container = itemView.findViewById(R.id.media_container);
        thumbnail = itemView.findViewById(R.id.thumbnail);
        title = itemView.findViewById(R.id.discription);
//        name = itemView.findViewById(R.id.textView8);
        progressBar = itemView.findViewById(R.id.progressBar);
        volumeControl = itemView.findViewById(R.id.volume_control);
        shareBtn = itemView.findViewById(R.id.share);
        likeBtn = itemView.findViewById(R.id.like);
        commentBtn = itemView.findViewById(R.id.comment);
        followBtn = itemView.findViewById(R.id.AddPerson);
        profile_img = itemView.findViewById(R.id.ImagePerson);
        soundDisk = itemView.findViewById(R.id.spinCd);
//        no_likes = itemView.findViewById(R.id.textView9);
//        no_comments = itemView.findViewById(R.id.textView10);
//        likeAnimate = itemView.findViewById(R.id.imageView5);
//        download = itemView.findViewById(R.id.imageView23);
    }

    public void onBind(RetrivingData retrivingData, RequestManager requestManager) {
        this.requestManager = requestManager;
        parent.setTag(this);
        title.setText(retrivingData.getDescription()+",\n"+retrivingData.getDate());

//        this.requestManager.load(mediaObject.getThumbnail()).into(thumbnail);
        Glide
                .with(itemView.getContext())
                .load(retrivingData.getThumbnail())
                .centerCrop()
                .placeholder(R.drawable.download_1)
                .into(thumbnail);
        Toast.makeText(itemView.getContext(),""+retrivingData.getThumbnail(),Toast.LENGTH_LONG).show();
        Picasso.get().load(retrivingData.getThumbnail());

    }

}
