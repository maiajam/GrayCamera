package com.maiajam.graycamera.Listeners;

import android.app.Activity;
import android.media.ImageReader;

public class ImageAvailableListener implements ImageReader.OnImageAvailableListener {


    Activity activity ;
    public ImageAvailableListener(Activity activity) {

        this.activity = activity ;
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        getCapturedImage(reader);
    }

    private void getCapturedImage(ImageReader reader) {

    }
}
