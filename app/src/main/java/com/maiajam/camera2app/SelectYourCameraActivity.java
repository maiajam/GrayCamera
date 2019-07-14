package com.maiajam.camera2app;

import android.content.Intent;
import android.hardware.camera2.CameraCharacteristics;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.maiajam.camera2app.Helper.AppSharedPrefrnce;
import com.maiajam.camera2app.Helper.Constant;
import com.maiajam.camera2app.Helper.HelperMethods;

public class SelectYourCameraActivity extends AppCompatActivity {

    String[] cameraIds;
    RadioButton FronCam_RB,BackCam_RB,ExternalCam_Rb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_your_camera);


        FronCam_RB = (RadioButton)findViewById(R.id.radio_FrontCamera);
        BackCam_RB = (RadioButton)findViewById(R.id.radio_BackCamera);
        ExternalCam_Rb = (RadioButton)findViewById(R.id.radio_ExternalCamera);

    }


    public void onClickRadio(View view) {

        switch (view.getId())
        {
            case R.id.radio_BackCamera :
                AppSharedPrefrnce.setSelectedCameraId(getBaseContext(),CameraCharacteristics.LENS_FACING_BACK);
            break;
            case R.id.radio_ExternalCamera:
                AppSharedPrefrnce.setSelectedCameraId(getBaseContext(), Constant.EXTRANAL_CAMERA_ID);
                break;
            case R.id.radio_FrontCamera:
                AppSharedPrefrnce.setSelectedCameraId(getBaseContext(),CameraCharacteristics.LENS_FACING_BACK);
                break;
        }
        goToCameraActivity();
    }

    private void goToCameraActivity() {
        Intent i = new Intent(SelectYourCameraActivity.this,CameraActivity.class);
        startActivity(i);
    }
}
