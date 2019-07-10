package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.util.Size;
import android.view.View;

public class HelperMethods {

    public static int getScreenHight(View contentview) {
            contentview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            return contentview.getMeasuredHeight();
    }

    public static int getScreenWidth(View contentview) {

        contentview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return contentview.getMeasuredWidth();
    }

    public static Size[] getScreenSize(Context context, CameraDevice cameraDevice) {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        Size[] ScreenSizes = null;
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());

            if (characteristics != null) {
                ScreenSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
            }
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return ScreenSizes;
    }
}
