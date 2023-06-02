package com.JvWorld.jcaller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.JvWorld.jcaller.someVariable.StcVariable;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StcVariable.isDarkMode(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(() -> {
            StcVariable.SPLASH_SCREEN = true;
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }, 2000);
    }

    @Override
    public void onBackPressed() {

    }
}