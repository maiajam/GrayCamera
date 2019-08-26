package com.maiajam.graycamera.CameraConfigration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import com.maiajam.graycamera.Helper.CompareSizesByArea;
import com.maiajam.graycamera.Helper.Constant;
import com.maiajam.graycamera.Helper.HelperMethodes;
import com.maiajam.graycamera.Listeners.ImageAvailableListener;
import com.maiajam.graycamera.views.AutoTextureView;

import java.util.Arrays;
import java.util.Collections;

import static com.maiajam.graycamera.Helper.HelperMethodes.chooseOptimalSize;

public class CameraConfigration {

    public static final int REQUEST_CAMERA_PERMISSION = 100;
    private static CameraManager manager;
    private static int selectedCameraId;
    private static ImageReader mImageReader;
    private static ImageAvailableListener imageAvailableListener;
    private static Handler backHandler;
    private static Integer mSensorOrientation;
    private static int MAX_PREVIEW_WIDTH = 1920;
    private static int MAX_PREVIEW_HEIGHT = 1080;
    private static Size mPreviewSize;
    private static String mCameraId;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void openCamera(Activity activity, int width, int height, Handler backGroundHandler,AutoTextureView textureView) {
        backHandler = backGroundHandler;
        checkCameraPermission(activity);
        setUpCameraOutputs(activity, width, height,textureView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void checkCameraPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission(activity);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static void requestCameraPermission(Activity activity) {
        if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            //  new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
        } else {
            activity.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private static void setUpCameraOutputs(Activity activity, int width, int height, AutoTextureView CameraPreivewTexture) {

        manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {

            String[] IdList = manager.getCameraIdList();
            if (IdList.length > 2)
                selectedCameraId = HelperMethodes.getSelectedCameraId(activity);
            else
                selectedCameraId = (int) Constant.BACK_CAMERA_ID;

            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics characteristics
                        = manager.getCameraCharacteristics(cameraId);
                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {

                } else if (selectedCameraId == Constant.EXTRANAL_CAMERA_ID) {
                    if (facing != null && facing == CameraCharacteristics.LENS_FACING_BACK) {
                        continue;
                    }
                }

                StreamConfigurationMap map = characteristics.get(
                        CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }

                // For still image captures, we use the largest available size.
                Size largest = Collections.max(
                        Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                        new CompareSizesByArea());
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                        ImageFormat.JPEG, /*maxImages*/2);

                imageAvailableListener = new ImageAvailableListener(activity);
                mImageReader.setOnImageAvailableListener(
                        imageAvailableListener, backHandler);


                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();

                mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        // Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }

                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;

                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }

                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }

                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                        maxPreviewHeight, largest);

                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = activity.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    CameraPreivewTexture.setAspectRatio(
                            mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    CameraPreivewTexture.setAspectRatio(
                            mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }

                Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);


                //  manager.setTorchMode(cameraId,true);

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }
}
