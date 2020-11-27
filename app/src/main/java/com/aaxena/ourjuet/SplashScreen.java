package com.aaxena.ourjuet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        fireSplashScreen();
    }
    private void fireSplashScreen() {
        int splash_screen_time_out = 2100;
        new Handler().postDelayed(() -> {

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, splash_screen_time_out);
    }
}