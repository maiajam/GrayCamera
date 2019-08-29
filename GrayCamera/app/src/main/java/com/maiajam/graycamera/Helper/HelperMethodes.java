package com.maiajam.graycamera.Helper;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.maiajam.graycamera.views.AutoTextureView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.checkCameraPermission;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.configureTransform;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.mImageReader;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.mPreviewSize;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.openNow;
import static com.maiajam.graycamera.CameraConfigration.CameraConfigration.setUpCameraOutputs;
import static com.maiajam.graycamera.Helper.GlobalValuesSingelTon.getInstance;

public class HelperMethodes {
    private static Handler backHandler;
    private static Surface surface;
    private static CaptureRequest.Builder mPreviewRequestBuilder;
    private static CameraCaptureSession mCaptureSession;

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
        openNow(activity,backGroundHandler);
    }

    public static void  createCameraPreviewSession(AutoTextureView CameraPreivewTexture){

        try {

            SurfaceTexture texture = CameraPreivewTexture.getSurfaceTexture();
            assert texture != null;
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            // This is the output Surface we need to start preview.
            surface = new Surface(texture);
            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder =getInstance().getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            mPreviewRequestBuilder.addTarget(surface);
            mPreviewRequestBuilder.set(CaptureRequest.JPEG_QUALITY,(byte)100);

            // Here, we create a CameraCaptureSession for camera preview.
            getInstance().getCameraDevice().createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @RequiresApi(api = Build.VERSION_CODES.P)
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == getInstance().getCameraDevice()) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            mCaptureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                                mPreviewRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);

                                // Finally, we start displaying the camera preview.
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest,
                                        mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(
                                @NonNull CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
