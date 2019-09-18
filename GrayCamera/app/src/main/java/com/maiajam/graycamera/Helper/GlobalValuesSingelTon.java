package com.maiajam.graycamera.Helper;

import android.hardware.camera2.CameraDevice;

public class GlobalValuesSingelTon {

    private static GlobalValuesSingelTon instance ;
    private static CameraDevice mCameraDevice;

    public GlobalValuesSingelTon() {
    }

    public static GlobalValuesSingelTon getInstance() {
        if(instance == null)
        {
            instance = new GlobalValuesSingelTon();
        }
        return instance;
    }

    public CameraDevice getCameraDevice() {
        return mCameraDevice;
    }

    public static void setCameraDevice(CameraDevice cameraDevice) {
        mCameraDevice = cameraDevice;
    }
}
