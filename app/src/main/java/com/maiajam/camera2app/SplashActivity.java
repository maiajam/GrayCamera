package com.maiajam.camera2app;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maiajam.camera2app.Helper.AppSharedPrefrnce;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        goToCameraScreen();
    }

    private void goToCameraScreen() {

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                Intent intent;
                if(AppSharedPrefrnce.isFirstVisit(getBaseContext()))
                {
                   AppSharedPrefrnce.setNotFirstVisit(getBaseContext());
                   intent = new Intent(SplashActivity.this,
                           com.maiajam.camera2app.SelectYourCameraActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    AppSharedPrefrnce.setNotFirstVisit(getBaseContext());
                    intent = new Intent(SplashActivity.this,
                            com.maiajam.camera2app.CameraActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }
}
