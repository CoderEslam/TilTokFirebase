package com.doubleclick.tiktok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.doubleclick.tiktok.Adapters.SoundAdapter;
import com.doubleclick.tiktok.MainRecyclerView.VideoPlayerRecyclerAdapter;
import com.doubleclick.tiktok.Model.Responses.RetrivingData;
import com.doubleclick.tiktok.Model.SoundModel;
import com.doubleclick.tiktok.VideoEditorFolder.PortraitCameraActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SoundActivity extends AppCompatActivity {

    private Context context = SoundActivity.this;
    private RecyclerView recyclerViewSound;
    private List<SoundModel> soundModelList = new ArrayList<>();
    private SoundAdapter soundAdapter;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sound_avtivity);
        recyclerViewSound = findViewById(R.id.RecyclerViewSounds);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        storage = FirebaseStorage.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewSound.setLayoutManager(new GridLayoutManager(this,2));

//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));
//        soundModelList.add(new SoundModel("","",""));


        reference.child("ALL_SOUNDS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    SoundModel soundModel;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        soundModel = dataSnapshot.getValue(SoundModel.class);

                        long s = snapshot.getChildrenCount();
//                        Toast.makeText(SoundActivity.this,"SoundAvtivity  At  81  , " + s,Toast.LENGTH_LONG).show();
                        soundModelList.add(soundModel);

                    }

                    SoundAdapter soundAdapter = new SoundAdapter(soundModelList, SoundActivity.this);
                    recyclerViewSound.setAdapter(soundAdapter);
                    soundAdapter.notifyDataSetChanged();


                } else {
                    Toast.makeText(SoundActivity.this, "Network Field.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    public void Close(View view) {

        Intent intent = new Intent(context, PortraitCameraActivity.class);
        startActivity(intent);
        Animatoo.animateInAndOut(context);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,PortraitCameraActivity.class);
        startActivity(intent);
        Animatoo.animateInAndOut(context);
        finish();
    }
}