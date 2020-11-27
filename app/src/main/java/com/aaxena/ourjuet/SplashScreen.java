package com.aaxena.ourjuet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
        int splash_screen_time_out = 1900;
        new Handler().postDelayed(() -> {
            Intent i=new Intent(SplashScreen.this,Login.class);
            startActivity(i);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, splash_screen_time_out);
    }
}