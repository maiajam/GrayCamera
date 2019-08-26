package com.maiajam.graycamera.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.maiajam.graycamera.CameraConfigration.CameraConfigration;
import com.maiajam.graycamera.Listeners.SurfaceTexuterViewListener;
import com.maiajam.graycamera.R;
import com.maiajam.graycamera.views.AutoTextureView;

public class CameraFragment extends Fragment {

    private View view;
    private AutoTextureView camraTextureView;
    private SurfaceTexuterViewListener surfaceListner;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;

    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  initialView(inflater,container);
    }

    private View initialView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_camera, container, false);
        camraTextureView =(AutoTextureView) view.findViewById(R.id.Camera_TextureView);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        checkSurfaceAvailable();
    }

    private void startBackgroundThread() {

            mBackgroundThread = new HandlerThread("CameraBackground");
            mBackgroundThread.start();
            mBackgroundHandler = new Handler(mBackgroundThread.getLooper());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkSurfaceAvailable() {
        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (camraTextureView.isAvailable()) {
            CameraConfigration.openCamera(getActivity(),camraTextureView.getWidth(),camraTextureView.getHeight(),mBackgroundHandler);
        } else {
            setSurfaceLisnter();
        }
    }

    private void setSurfaceLisnter() {

        surfaceListner = new SurfaceTexuterViewListener(getActivity());
        camraTextureView.setSurfaceTextureListener(surfaceListner);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CameraConfigration.REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
              /*  ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);*/
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

}
