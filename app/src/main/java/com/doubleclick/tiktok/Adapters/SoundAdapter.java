package com.doubleclick.tiktok.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.doubleclick.tiktok.Model.SoundModel;
import com.doubleclick.tiktok.R;
import com.doubleclick.tiktok.VideoEditorFolder.PortraitCameraActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundViewHolder> {

    public SoundAdapter(List<SoundModel> soundModelList, Context context) {
        this.soundModelList = soundModelList;
        this.context = context;
    }

    private List<SoundModel>  soundModelList;
    Context context ;

    @NonNull
    @NotNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_sound,parent,false);
        SoundViewHolder soundViewHolder = new SoundViewHolder(view);
        return soundViewHolder; // or ->  return new SoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SoundAdapter.SoundViewHolder holder, int position) {

        SoundModel soundModel = soundModelList.get(position);
        holder.title.setText(soundModel.getSound_title());
        holder.itemView.setOnClickListener(view ->{
            Intent intent = new Intent(context, PortraitCameraActivity.class);
            intent.putExtra("soundUrl",soundModel.getSound_file());
            intent.putExtra("soundTitle",soundModel.getSound_title());
            context.startActivity(intent);
            Animatoo.animateSpin(context);

        } );

    }

    @Override
    public int getItemCount() {
        return soundModelList.size();
    }

    public class SoundViewHolder extends RecyclerView.ViewHolder {

        private TextView title ;

        public SoundViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Name_of_Song);

        }
    }
}
