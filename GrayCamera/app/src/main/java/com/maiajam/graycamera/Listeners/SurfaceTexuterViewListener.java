package com.maiajam.graycamera.Listeners;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.TextureView;

import com.maiajam.graycamera.CameraConfigration.CameraConfigration;

public class SurfaceTexuterViewListener implements TextureView.SurfaceTextureListener {


    private Activity activity;

    public SurfaceTexuterViewListener(Activity activity) {
        this.activity = activity;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        CameraConfigration.openCamera(activity,width,height);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
