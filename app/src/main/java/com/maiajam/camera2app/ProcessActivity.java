package com.maiajam.camera2app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.maiajam.camera2app.Helper.ProcessThread;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProcessActivity extends AppCompatActivity {


    public static Bitmap capturedImage_bitmap;

    @BindView(R.id.processImageView_IndexFinger)
    ImageView processImageViewIndexFinger;
    @BindView(R.id.processImageView_MiddleFinger)
    ImageView processImageViewMiddleFinger;
    @BindView(R.id.processImageView_RingFinger)
    ImageView processImageViewRingFinger;
    @BindView(R.id.processImageView_BabyFinger)
    ImageView processImageViewBabyFinger;
    @BindView(R.id.processImageViewSource)
    ImageView processImageViewSource;
    @BindView(R.id.processButtonSave)
    Button processButtonSave;
    @BindView(R.id.processButtonQuery)
    Button processButtonQuery;
    @BindView(R.id.processButtonSettings)
    Button processButtonSettings;
    @BindView(R.id.processButtonPrevious)
    Button processButtonPrevious;
    @BindView(R.id.processButtonNext)
    Button processButtonNext;
    @BindView(R.id.processButtonNextSet)
    Button processButtonNextSet;
    private Mat newMat;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ButterKnife.bind(this);

        startProcess();
    }

    private void startProcess() {
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg != null)
                {
                    showImage((Mat) msg.obj);
                }
            }
        };
        ProcessThread proccesThread = new ProcessThread(getBaseContext(),handler);
        proccesThread.setImage(capturedImage_bitmap);
        proccesThread.start();
    }

    private void showImage(Mat mat) {

        Core.normalize(mat, mat, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
        processImageViewSource.setImageBitmap(convertMatToBitMap(mat));

    }

    private Bitmap convertMatToBitMap(Mat mat) {

        Bitmap bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, bitmap);
        return bitmap;
    }

    @OnClick({R.id.processButtonSave, R.id.processButtonQuery, R.id.processButtonSettings, R.id.processButtonPrevious, R.id.processButtonNext, R.id.processButtonNextSet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.processButtonSave:
                break;
            case R.id.processButtonQuery:
                break;
            case R.id.processButtonSettings:
                break;
            case R.id.processButtonPrevious:
                break;
            case R.id.processButtonNext:
                break;
            case R.id.processButtonNextSet:
                break;
        }
    }
}
