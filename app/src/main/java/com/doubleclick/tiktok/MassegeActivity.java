package com.doubleclick.tiktok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class MassegeActivity extends AppCompatActivity {

    private Context context = MassegeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massege);
    }


    public void Close(View view) {

        Intent intent = new Intent(context,HomeActivity.class);
        startActivity(intent);
        Animatoo.animateSplit(context);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(context,HomeActivity.class);
        startActivity(intent);
        Animatoo.animateSplit(context);
        finish();
    }

}