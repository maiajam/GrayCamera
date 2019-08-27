package com.maiajam.graycamera.CameraConfigration;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
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
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

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
    private static Semaphore mCameraOpenCloseLock = new Semaphore(1); // to prevent the app from exiting before closing the camera.



    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkCameraPermission(Activity activity) {
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

    public static void setUpCameraOutputs(Activity activity, int width, int height, AutoTextureView CameraPreivewTexture) {

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
                checkDisplayRotation(displayRotation,swappedDimensions);

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

                fitAspectRatioToPrviewSize(activity.getResources().getConfiguration().orientation,CameraPreivewTexture);

                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }

    private static void fitAspectRatioToPrviewSize(int orientation,AutoTextureView CameraPreivewTexture) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            CameraPreivewTexture.setAspectRatio(
                    mPreviewSize.getWidth(), mPreviewSize.getHeight());
        } else {
            CameraPreivewTexture.setAspectRatio(
                    mPreviewSize.getHeight(), mPreviewSize.getWidth());
        }
    }

    private static void checkDisplayRotation(int displayRotation, boolean swappedDimensions) {
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
    }

    public static void configureTransform(int viewWidth, int viewHeight,Activity activity,AutoTextureView CameraPreivewTexture) {

        if (null == CameraPreivewTexture || null == mPreviewSize || null == activity) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        int screenHight = Resources.getSystem().getDisplayMetrics().heightPixels;
        int yShift = (viewHeight - screenHight)/2;
        int xShift = (viewWidth - screenWidth)/2;
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
            //  matrix.setTranslate(, 0);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
            matrix.setTranslate(0, -yShift);
        }


        //matrix.setTranslate(screenHight,screenHight);
        CameraPreivewTexture.setTransform(matrix);
    }

    public static void opeenNow(Activity activity,Handler mBackgroundHandler)
    {
        manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }
}
