package com.maiajam.graycamera.callBack;

import android.hardware.camera2.CameraDevice;

import java.util.concurrent.Semaphore;
import com.maiajam.graycamera.Helper.GlobalValuesSingelTon;

import static com.maiajam.graycamera.Helper.GlobalValuesSingelTon.getInstance;
import static com.maiajam.graycamera.Helper.HelperMethodes.createCameraPreviewSession;


public class CameraDeviceStateCallBack extends CameraDevice.StateCallback {

    // to prevent the app from exiting before closing the camera.
    private static Semaphore mCameraOpenCloseLock = new Semaphore(1);

    @Override
    public void onOpened(@androidx.annotation.NonNull CameraDevice camera) {
        // This method is called when the camera is opened.  We start camera preview here.
        mCameraOpenCloseLock.release();
       getInstance().setCameraDevice(camera);
        createCameraPreviewSession();
    }

    @Override
    public void onDisconnected(@androidx.annotation.NonNull CameraDevice camera) {
        mCameraOpenCloseLock.release();
        getInstance().setCameraDevice(null);
        camera.close();
    }

    @Override
    public void onError(@androidx.annotation.NonNull CameraDevice camera, int error) {
        mCameraOpenCloseLock.release();
        getInstance().setCameraDevice(null);
        camera.close();
    }
}
