package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ProcessThread extends Thread {

    Bitmap mImage;
    Context mContext;
    private Mat resultMat;
    private Handler mHandler;


    public ProcessThread(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void run() {
        super.run();

        Message message = new Message();
        resultMat = new Mat(mImage.getHeight(), mImage.getWidth(), CvType.CV_8UC1);
        Utils.bitmapToMat(mImage, resultMat);
        getFingerPrint(resultMat, message);

        mHandler.sendMessage(message);
    }

    private void getFingerPrint(Mat resultMat, Message message) {

        int rows = resultMat.rows();
        int cols = resultMat.cols();
        Mat returendMat = new Mat(rows, cols, CvType.CV_8UC1);
        // get graysScale
        Mat matGrayScale = new Mat(rows, cols, CvType.CV_8UC1);
        Imgproc.cvtColor(returendMat, matGrayScale, Imgproc.COLOR_RGB2GRAY);

        Imgproc.adaptiveThreshold(resultMat, returendMat, 255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 9, 2);

        message.obj = returendMat;
    }

    public void setImage(Bitmap image) {
        mImage = image;
    }
}
