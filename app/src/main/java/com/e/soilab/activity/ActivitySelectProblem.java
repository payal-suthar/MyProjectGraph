package com.e.soilab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.soilab.R;

public class ActivitySelectProblem extends AppCompatActivity
{
    ImageView img_notwarning,img_need,img_onfire,img_backarrow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_problem);

        initView();


        img_backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    public void initView()
    {
        img_notwarning = findViewById(R.id.img_NotWarning);
        img_need = findViewById(R.id.img_Need);
        img_onfire = findViewById(R.id.img_OnFire);
        img_backarrow = findViewById(R.id.img_BackArrow);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(ActivitySelectProblem.this, ActivityProfile.class);
        startActivity(intent);
    }
}
