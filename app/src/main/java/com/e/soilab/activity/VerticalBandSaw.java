package com.e.soilab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.soilab.R;

public class VerticalBandSaw extends AppCompatActivity
{

    ImageView img_Back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_bandsaw);
        initView();

        img_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void initView()
    {
        img_Back = findViewById(R.id.img_BackArrow);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(VerticalBandSaw.this, ActivityProfile.class);
        startActivity(intent);
    }
}

