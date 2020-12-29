package com.example.smartsallinebottle;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        EasySplashScreen config =new EasySplashScreen(MainActivity.this)
                .withFullScreen()
                .withTargetActivity(LoginActivity.class)
                .withSplashTimeOut(4000)
                .withBackgroundColor(Color.parseColor("#1a1b29"))
                .withHeaderText("Team 4")
                .withFooterText("Copyright 2020")
                .withBeforeLogoText("Smart Saline")
                .withLogo(R.drawable.img1)
                .withAfterLogoText("Bottle System");

            config.getHeaderTextView().setTextColor(getResources().getColor(android.R.color.white));
            config.getFooterTextView().setTextColor(getResources().getColor(android.R.color.white));
            config.getBeforeLogoTextView().setTextColor(getResources().getColor(android.R.color.white));
            config.getBeforeLogoTextView().setPadding(4,4,4,4);
            config.getAfterLogoTextView().setTextColor(getResources().getColor(android.R.color.white));
            config.getAfterLogoTextView().setPadding(4,4,4,4);

        View easysplash = config.create();
        setContentView(easysplash);

    }
}
