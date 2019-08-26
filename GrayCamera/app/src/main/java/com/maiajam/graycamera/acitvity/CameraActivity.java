package com.maiajam.graycamera.acitvity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maiajam.graycamera.Helper.HelperMethodes;
import com.maiajam.graycamera.R;
import com.maiajam.graycamera.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity {

    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startCameraFragment();
    }

    private void startCameraFragment() {
        cameraFragment = new CameraFragment();
        HelperMethodes.beginTransaction(getSupportFragmentManager(),R.id.main_Container_frame,cameraFragment);
    }
}
