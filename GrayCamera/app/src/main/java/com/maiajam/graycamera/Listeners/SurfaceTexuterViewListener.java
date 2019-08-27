package com.maiajam.graycamera.Listeners;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.TextureView;

import com.maiajam.graycamera.CameraConfigration.CameraConfigration;
import com.maiajam.graycamera.views.AutoTextureView;

import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.configureTransform;
import static com.maiajam.graycamera.Helper.HelperMethodes.openCamera;

public class SurfaceTexuterViewListener implements TextureView.SurfaceTextureListener {


    private final Handler backHandler;
    private final AutoTextureView cameraView;
    private Activity activity;

    public SurfaceTexuterViewListener(Activity activity, Handler mbackHandler, AutoTextureView textureView) {
        this.activity = activity;
        this.backHandler = mbackHandler;
        this.cameraView = textureView ;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

        openCamera(activity,width,height,backHandler,cameraView);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        configureTransform(width, height,activity,cameraView);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        //getCameraFrame();
    }

    private void getCameraFrame() {

    }
}
