package com.maiajam.graycamera.Helper;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Size;

import com.maiajam.graycamera.views.AutoTextureView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.checkCameraPermission;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.configureTransform;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.setUpCameraOutputs;

public class HelperMethodes {
    private static Handler backHandler;

    public static int getSelectedCameraId(Activity activity) {
        return 1;
    }

    public static void beginTransaction(FragmentManager fragmentManager, int FrameId, Fragment fragment)
    {
        fragmentManager.beginTransaction().replace(FrameId,fragment).commit();
    }

    public static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                         int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        List<Size> bigEnough = new ArrayList<>();
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            return choices[0];
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void openCamera(Activity activity, int width, int height, Handler backGroundHandler, AutoTextureView textureView) {
        backHandler = backGroundHandler;
        checkCameraPermission(activity);
        setUpCameraOutputs(activity, width, height,textureView);
        configureTransform(width,height,activity,textureView);
        openNow();
    }
}
